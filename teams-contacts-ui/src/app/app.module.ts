import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTableExporterModule } from 'mat-table-exporter';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TeamsContactsListComponent } from './components/teams-contacts-list/teams-contacts-list.component';
import { TeamsContactsService } from './service/teams-contacts-service.service';
import { EditTeamComponent } from './components/edit-team/edit-team.component';
import { EditContactComponent } from './components/edit-contact/edit-contact.component';

@NgModule({
  declarations: [
    AppComponent,
    TeamsContactsListComponent,
    EditTeamComponent,
    EditContactComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatTableModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatTableExporterModule
  ],
  providers: [TeamsContactsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
