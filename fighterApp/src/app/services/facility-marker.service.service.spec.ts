import { TestBed } from '@angular/core/testing';

import { FacilityMarker.ServiceService } from './facility-marker.service.service';

describe('FacilityMarker.ServiceService', () => {
  let service: FacilityMarker.ServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacilityMarker.ServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
