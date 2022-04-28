import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Team } from '../model/team';
import { Contact } from '../model/contact';
import { ContactsResult } from '../model/contacts-result';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamsContactsService {
  private teamsUrl: string;
  private contactsUrl: string;

  private teams: Team[] | undefined;
  private contacts: Contact[] = [];

  constructor(private http: HttpClient) {
    this.teamsUrl = 'http://localhost:9090/teams';
    this.contactsUrl = 'http://localhost:9090/contacts';
  }
  
  getTeams(): Team[] | undefined {
    return this.teams;
  }

  setTeams(teams:Team[]) {
    this.teams = teams;
  }

  getContacts(): Contact[] {
    return this.contacts;
  }

  setContacts(contacts:Contact[]) {
    this.contacts = contacts;
  }

  findTeamInLocal(teamId:number): Team | undefined {
    if(this.teams) {
	  for (let team of this.teams!) {
        if (team.id == teamId) {
	      return team;
        }
      }
    }
    return undefined;
  }

  updateTeamInLocal(updatedTeam:Team): void {
    for (let [i, team] of this.teams!.entries()) {
      if (team.id == updatedTeam.id) {
        this.teams!.splice(i, 1, updatedTeam);
        break;
      }
    }
  }

  removeTeamInLocal(teamId:number): void {
    for (let [i, team] of this.teams!.entries()) {
      if (team.id == teamId) {
        this.teams!.splice(i, 1);
        break;
      }
    }
  }

  addTeamInLocal(team:Team): void {
    this.teams!.push(team);
  }

  findContactInLocal(contactId:number): Contact | undefined {
    for (let contact of this.contacts!) {
      if (contact.id == contactId) {
	    return contact;
      }
    }
    return undefined;
  }
  
  updateContactInLocal(updatedContact:Contact): void {
    for (let [i, contact] of this.contacts!.entries()) {
      if (contact.id == updatedContact.id) {
        this.contacts!.splice(i, 1, updatedContact);
        break;
      }
    }
  }

  removeContactInLocal(contactId:number): void {
    for (let [i, contact] of this.contacts!.entries()) {
      if (contact.id == contactId) {
        this.contacts!.splice(i, 1);
        break;
      }
    }
  }

  addContactInLocal(contact:Contact): void {
    this.contacts!.push(contact);
  }

  createTeam(team:Team): Observable<Team> {
    return this.http.post<Team>(this.teamsUrl, team);
  }

  updateTeam(team:Team): Observable<Team> {
    return this.http.put<Team>(this.teamsUrl+ "/" + team.id, team);
  }

  deleteTeam(teamId:number): Observable<Team> {
    return this.http.delete<Team>(this.teamsUrl + "/" + teamId);
  }

  removeAllContactsInLocal():void {
    this.contacts = [];	
  }

  findAllTeams(): Observable<any> {
    return this.http.get<any>(this.teamsUrl);
  }

  createContact(contact:Contact): Observable<Contact> {
    return this.http.post<Contact>(this.contactsUrl, contact);
  }

  updateContact(contact:Contact): Observable<Contact> {
    return this.http.put<Contact>(this.contactsUrl+ "/" + contact.id, contact);
  }

  deleteContact(contactId:number): Observable<Contact> {
    return this.http.delete<Contact>(this.contactsUrl + "/" + contactId);
  }

  findPagedContactsByTeamId(teamId:number, pageIndex:number, pageSize:number): Observable<ContactsResult> {
    let params = new HttpParams();
    params = params.append("pageIndex", pageIndex);
    params = params.append("pageSize", pageSize);
    return this.http.get<ContactsResult>(this.teamsUrl + "/" + teamId + "/contacts", {params: params});
  }
}
