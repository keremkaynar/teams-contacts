import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Contact } from '../../model/contact';
import { TeamsContactsService } from '../../service/teams-contacts-service.service';

@Component({
  selector: 'app-edit-contact',
  templateUrl: './edit-contact.component.html',
  styleUrls: ['./edit-contact.component.css']
})
export class EditContactComponent implements OnInit {
  contact: Contact | undefined;
  contactForm!: FormGroup;
  teamId: number | undefined;
  contactId: number | undefined;

  constructor(private router: Router, private route: ActivatedRoute, private teamsContactsService: TeamsContactsService) { }

  ngOnInit(): void {
    const teamIdStr = this.route.snapshot.paramMap.get('teamId');
    if(teamIdStr) {
	  this.teamId = Number(teamIdStr);
    }
    const contactIdStr = this.route.snapshot.paramMap.get('contactId');
	if(contactIdStr) {
		this.contactId = Number(contactIdStr);
		this.contact = this.teamsContactsService.findContactInLocal(this.contactId);
	}
	if(!this.contact) {
      this.contact = new Contact();
      this.contact.teamId = this.teamId;
	}
	this.contactForm = new FormGroup({
      firstName: new FormControl(this.contact!.firstName, [Validators.required, Validators.maxLength(50)]),
      lastName: new FormControl(this.contact!.lastName, [Validators.required, Validators.maxLength(50)]),
      mailAddress: new FormControl(this.contact!.mailAddress, [Validators.email]),
      teamId: new FormControl(this.contact!.teamId, [Validators.required])
    });
  }

  get firstName() { return this.contactForm.get('firstName'); }
  get lastName() { return this.contactForm.get('lastName'); }
  get mailAddress() { return this.contactForm.get('mailAddress'); }
  get formTeamId() { return this.contactForm.get('teamId'); }

  onSubmit(): void {
    this.contact!.firstName = this.contactForm.get('firstName')!.value;
    this.contact!.lastName = this.contactForm.get('lastName')!.value;
    this.contact!.mailAddress = this.contactForm.get('mailAddress')!.value;
    this.contact!.teamId = this.contactForm.get('teamId')!.value;
	if(this.contact!.id) {
		this.teamsContactsService.updateContact(this.contact!).subscribe(data => 
		{
		  this.teamsContactsService.updateContactInLocal(data);
          this.router.navigate(['/']);
        });
	} else {
	  this.teamsContactsService.createContact(this.contact!).subscribe(data => 
        {
	      this.teamsContactsService.addContactInLocal(data);
          this.router.navigate(['/']);
        });
    }
  }
}
