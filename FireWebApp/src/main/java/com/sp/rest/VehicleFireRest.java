package com.sp.rest;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.VehicleDto;
import com.sp.service.VehicleFireService;


@RestController
public class VehicleFireRest {
	
    @Autowired
    VehicleFireService vfService;

    @RequestMapping(method=RequestMethod.GET,value="/vehicle/dest/coord")
    public Map<Integer, VehicleDto>  getVehiclesFiresCoord() {
    	return vfService.getVehiclesFiresCoord();
    }
}

