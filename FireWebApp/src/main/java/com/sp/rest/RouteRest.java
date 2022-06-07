package com.sp.rest;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sp.service.RouteService;

@CrossOrigin 
@RestController
public class RouteRest {

	@Autowired
	RouteService rService;
	
	 @RequestMapping(method=RequestMethod.GET,value="/routes/{id}")
	    public ArrayList<ArrayList<Double>>  getRoute(@PathVariable String id) {
		 ArrayList<ArrayList<Double>>  routes  = rService.getRoute(Integer.valueOf(id));
			return routes;
	    }
	 @RequestMapping(method=RequestMethod.GET,value="/routes")
	    public Map<Integer,ArrayList<ArrayList<Double>>>  getAllRoute() {
		 Map<Integer,ArrayList<ArrayList<Double>>>  routes  = rService.getAllRoute();
			return routes;
	    }
	 
	 
}
