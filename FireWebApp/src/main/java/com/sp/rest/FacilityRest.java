package com.sp.rest;


import java.util.List;

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
    public FacilityDto[]  getFacilities() {
    	FacilityDto[]  facilities  = fService.getFacilities();
		return facilities;
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/facility/{id}")
    public FacilityDto getFacility(@PathVariable String id) {
        FacilityDto  facility= fService.getFacility(Integer.valueOf(id));
        return facility;
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/own/facility")
    public List<Integer> getOwnFacility() {
        List<Integer>  facility= fService.getOwnFacilities();
        return facility;
    }
}