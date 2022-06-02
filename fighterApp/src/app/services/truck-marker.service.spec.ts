import { TestBed } from '@angular/core/testing';

import { TruckMarkerService } from './truck-marker.service';

describe('TruckMarkerService', () => {
  let service: TruckMarkerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TruckMarkerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
