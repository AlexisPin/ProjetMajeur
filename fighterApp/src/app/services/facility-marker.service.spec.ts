import { TestBed } from '@angular/core/testing';

import { FacilityMarkerService } from './facility-marker.service';

describe('FacilityMarkerService', () => {
  let service: FacilityMarkerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacilityMarkerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
