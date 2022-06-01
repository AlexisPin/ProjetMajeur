package com.sp.service;

import org.springframework.stereotype.Service;

import com.project.model.dto.FacilityDto;
import com.sp.rest.FetchFacility;

@Service
public class FacilityService {

    private FetchFacility fetch = new FetchFacility();
	
	public FacilityDto[]  getFacilities() {
		FacilityDto[]  facilities = fetch.getFacilities();
		return facilities;
	}

	public FacilityDto getFacility(Integer id) {
		FacilityDto facility = fetch.getFacility(id);
		return facility;
	}
	
	
}