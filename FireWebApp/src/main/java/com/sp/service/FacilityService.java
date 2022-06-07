package com.sp.service;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<Integer> getOwnFacilities() {
		FacilityDto[]  facilities = getFacilities();
		List<Integer>  ownFacilities = new ArrayList<Integer>();
		for(FacilityDto facility : facilities) {
			if(facility.getName().indexOf("Cas2") != -1) {	
				ownFacilities.add(facility.getId());
			}
		}
		return ownFacilities;
	}
}