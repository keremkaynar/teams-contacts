import { TestBed } from '@angular/core/testing';

import { TeamsContactsServiceService } from './teams-contacts-service.service';

describe('TeamsContactsServiceService', () => {
  let service: TeamsContactsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeamsContactsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
