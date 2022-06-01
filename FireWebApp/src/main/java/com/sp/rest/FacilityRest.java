package com.sp.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.FacilityDto;
import com.sp.service.FacilityService;


@RestController
public class FacilityRest {
	
    @Autowired
    FacilityService fService;

    @RequestMapping(method=RequestMethod.GET,value="/facility")
    public FacilityDto[]  getFires() {
    	FacilityDto[]  facilities  = fService.getFacilities();
		return facilities;
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/facility/{id}")
    public FacilityDto getCard(@PathVariable String id) {
        FacilityDto  facility= fService.getFacility(Integer.valueOf(id));
        return facility;
    }
}