package com.sp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FacilityDto;

public class FetchFacility {

	static final String URL_FACILITY = "http://vps.cpe-sn.fr:8081/facility";

	RestTemplate restTemplate;

    public FetchFacility() {
    	this.restTemplate = new RestTemplate();
	}
    
	public FacilityDto[] getFacilities() {

		// Send request with GET method and default Headers.
		ResponseEntity<FacilityDto[] > result = restTemplate.getForEntity(URL_FACILITY, FacilityDto[] .class);
		
		FacilityDto[] fires = result.getBody();
		return fires;
	}
	
	public FacilityDto getFacility(int id) {

		// Send request with GET method and default Headers.
		ResponseEntity<FacilityDto> result = restTemplate.getForEntity(URL_FACILITY+"/"+id, FacilityDto.class);
		
		FacilityDto fire= result.getBody();
		return fire;
	}
	
	
}