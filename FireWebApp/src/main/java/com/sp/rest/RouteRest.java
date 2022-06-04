package com.sp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sp.service.RouteService;

import java.util.ArrayList;

@RestController
public class RouteRest {

	@Autowired
	RouteService rService;
	
	 @RequestMapping(method=RequestMethod.GET,value="/routes/{lonS}/{latS}/{lonE}/{latE}")
	    public Double  getFires(@PathVariable double lonS,@PathVariable double latS,@PathVariable double lonE,@PathVariable double latE) {
		 Double  routes  = rService.getDistance(lonS,latS,lonE,latE);
			return routes;
	    }
}
