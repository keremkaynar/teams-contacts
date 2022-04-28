import { Component, OnInit, ViewChild, OnDestroy, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material/paginator';
import {MatTable} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {merge, Subscription, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { Team } from '../../model/team';
import { Contact } from '../../model/contact';
import { TeamsContactsService } from '../../service/teams-contacts-service.service';

@Component({
  selector: 'app-teams-contacts-list',
  templateUrl: './teams-contacts-list.component.html',
  styleUrls: ['./teams-contacts-list.component.css']
})
export class TeamsContactsListComponent implements OnInit, OnDestroy, AfterViewInit {
  displayedTeamColumns: string[] = ['select', 'id', 'name'];
  teamSelection =  new SelectionModel<Team>(false, []);
  
  displayedContactColumns: string[] = ['select', 'id', 'firstName', 'lastName', 'mailAddress', 'teamId'];
  contactSelection =  new SelectionModel<Contact>(false, []);
  isLoadingContacts = true;
  contactResultsLength = 0;

  teams: Team[] | undefined;
  teamContacts: Contact[] = []; 
  teamContactsSubscription!: Subscription;  

  selectedTeam: Team | undefined;
  selectedContact: Contact | undefined;

  @ViewChild('teamsTable')
    teamsTable!: MatTable<Team>;
  @ViewChild('contactsTable')
    contactsTable!: MatTable<Contact>;

  @ViewChild(MatPaginator)
    contactsPaginator!: MatPaginator;
  @ViewChild('contactsSorter')
    contactsSorter!: MatSort;

  constructor(public router: Router, private cdref: ChangeDetectorRef, private teamsContactsService: TeamsContactsService) { }
    
  ngOnDestroy(): void {
    if(this.teamContactsSubscription) {
	  this.teamContactsSubscription.unsubscribe();    
    }
  }
   
  ngOnInit(): void {
	this.teams = this.teamsContactsService.getTeams();
	if(!this.teams) {
	  this.teamsContactsService.findAllTeams().subscribe(data => {
        this.teams = data._embedded?.teams || [];
        this.teamsContactsService.setTeams(this.teams!);
      });
    }
  }

  ngAfterViewInit(): void {
    const lastSelectedTeamId = sessionStorage.getItem('selectedTeamId');
    if(lastSelectedTeamId) {
	  const lastSelectedTeam = this.teamsContactsService.findTeamInLocal(Number(lastSelectedTeamId));
      if(lastSelectedTeam) {
        const lastContactsPageIndex = sessionStorage.getItem('contactsPageIndex');
        if(lastContactsPageIndex) {
	      this.contactsPaginator.pageIndex = Number(lastContactsPageIndex);
          this.updateTeamSelection(lastSelectedTeam, false);
          this.cdref.detectChanges();
        }
      }
    }
  }

  findPagedContactsByTeam(team:Team) {
    // If the user changes the sort order, reset back to the first page.
    this.contactsSorter.sortChange.subscribe(() => (this.contactsPaginator.pageIndex = 0));

    if(this.teamContactsSubscription) {
      this.teamContactsSubscription.unsubscribe();
    }

    this.teamContactsSubscription = merge(this.contactsSorter.sortChange, this.contactsPaginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingContacts = true;
          sessionStorage.setItem('contactsPageIndex', JSON.stringify(this.contactsPaginator.pageIndex));
          return this.teamsContactsService.findPagedContactsByTeamId(
	        team.id!, 
            this.contactsPaginator.pageIndex, 
            this.contactsPaginator.pageSize
          )
          .pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingContacts = false;
          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          this.contactResultsLength = data.totalCount;
          return data.contacts;
        }),
      )
      .subscribe(data => {
	    this.teamContacts = data;
        this.teamsContactsService.setContacts(data);
      });
  }

  updateTeamSelection(team:Team, resetPageIndex: boolean): void {
    this.teamSelection.toggle(team);
    if(this.teamSelection.isSelected(team)) {
      this.selectedTeam = team;
      sessionStorage.setItem('selectedTeamId', JSON.stringify(this.selectedTeam.id));
      if(resetPageIndex) {
	    this.contactsPaginator.pageIndex = 0;
      }
      this.findPagedContactsByTeam(team);	
    } else {
      this.selectedTeam = undefined;
      sessionStorage.removeItem('selectedTeamId');
      this.teamContacts = [];
	}
    this.contactsTable.renderRows();
  }

  updateContactSelection(contact:Contact): void {
    this.contactSelection.toggle(contact);
    if(this.contactSelection.isSelected(contact)) {
      this.selectedContact = contact;
    }
  }

  createContact(): void {
	this.contactResultsLength++;
    this.router.navigate(['/editContact', { teamId: this.selectedTeam!.id }]);	
  }

  updateContact(): void {
    this.router.navigate(['/editContact', { contactId: this.selectedContact!.id, teamId: this.selectedTeam!.id }]);	
  }

  removeTeam(): void {
    this.teamsContactsService.deleteTeam(this.selectedTeam!.id!).subscribe(data => 
      {
	    this.teamsContactsService.removeTeamInLocal(this.selectedTeam!.id!);
        this.teamsContactsService.removeAllContactsInLocal();
        this.teamContactsSubscription.unsubscribe();
        this.teamContacts = [];
        this.selectedTeam = undefined;
        this.selectedContact = undefined;
        this.teamsTable.renderRows();
        this.contactsTable.renderRows();
    });
  }

  createTeam(): void {
    this.router.navigate(['/editTeam']);	
  }

  updateTeam(): void {
    this.router.navigate(['/editTeam', { teamId: this.selectedTeam!.id }]);	
  }

  removeContact(): void {
    this.teamsContactsService.deleteContact(this.selectedContact!.id!).subscribe(data => 
    {
	  this.teamsContactsService.removeContactInLocal(this.selectedContact!.id!);
      this.contactResultsLength--;
      if(this.contactsPaginator.hasNextPage() || this.teamContacts!.length == 0) {
        if(this.teamContacts!.length == 0 && this.contactsPaginator.pageIndex > 0) {
	      this.contactsPaginator.pageIndex = this.contactsPaginator.pageIndex - 1;
          sessionStorage.setItem('contactsPageIndex', JSON.stringify(this.contactsPaginator.pageIndex));
        }
        if(this.contactResultsLength > 0) {
          this.findPagedContactsByTeam(this.selectedTeam!);
        } else {
	      if(this.teamContactsSubscription) {
            this.teamContactsSubscription.unsubscribe();
          }
        }
      }
      this.selectedContact = undefined;
      this.contactsTable.renderRows();
    });
  }
}
