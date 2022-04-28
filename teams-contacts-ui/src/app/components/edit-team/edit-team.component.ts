import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Team } from '../../model/team';
import { TeamsContactsService } from '../../service/teams-contacts-service.service';

@Component({
  selector: 'app-edit-team',
  templateUrl: './edit-team.component.html',
  styleUrls: ['./edit-team.component.css']
})
export class EditTeamComponent implements OnInit {
  team: Team | undefined;
  teamForm!: FormGroup;

  constructor(private router: Router, private route: ActivatedRoute, private teamsContactsService: TeamsContactsService) { }

  ngOnInit(): void {
	const teamId = this.route.snapshot.paramMap.get('teamId');
	if(teamId) {
		this.team = this.teamsContactsService.findTeamInLocal(Number(teamId));
	}
	if(!this.team) {
      this.team = new Team();
	}
	this.teamForm = new FormGroup({
      name: new FormControl(this.team!.name, [Validators.required, Validators.maxLength(50)]),
    });
  }

  get name() { return this.teamForm.get('name'); }

  onSubmit(): void {
    this.team!.name = this.teamForm.get('name')!.value;
	if(this.team!.id) {
		this.teamsContactsService.updateTeam(this.team!).subscribe(data => {
			this.teamsContactsService.updateTeamInLocal(data);
			this.router.navigate(['/']);
		});
	} else {
	  this.teamsContactsService.createTeam(this.team!).subscribe(data => {
		this.teamsContactsService.addTeamInLocal(data);
		this.router.navigate(['/']);
      });
    }
  }
}
