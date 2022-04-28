import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamsContactsListComponent } from "./TeamsContactsListComponent";

describe('TeamsContactsListComponent', () => {
  let component: TeamsContactsListComponent;
  let fixture: ComponentFixture<TeamsContactsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamsContactsListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamsContactsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
