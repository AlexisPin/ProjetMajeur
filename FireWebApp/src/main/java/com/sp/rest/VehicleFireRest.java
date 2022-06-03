package com.sp.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.FireDto;
import com.sp.service.FireService;


@RestController
public class VehicleFireRest {
	
    @Autowired
    FireService fService;

    @RequestMapping(method=RequestMethod.GET,value="/vehicle/dest/coord")
    public void  getVehiclesFiresCoord() {
    	
    }
    

}