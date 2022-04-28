import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TeamsContactsListComponent } from './components/teams-contacts-list/teams-contacts-list.component';
import { EditTeamComponent } from './components/edit-team/edit-team.component';
import { EditContactComponent } from './components/edit-contact/edit-contact.component';

const routes: Routes = [
  { path: '', component: TeamsContactsListComponent},
  { path: 'editTeam', component: EditTeamComponent },
  { path: 'editContact', component: EditContactComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
