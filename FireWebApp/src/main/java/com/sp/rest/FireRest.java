package com.sp.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.FireDto;
import com.sp.service.FireService;

@CrossOrigin
@RestController
public class FireRest {
	
    @Autowired
    FireService fService;

    @RequestMapping(method=RequestMethod.GET,value="/fire")
    public FireDto[]  getFires() {
    	FireDto[]  fires  = fService.getFires();
		return fires;
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/fire/{id}")
    public FireDto getCard(@PathVariable String id) {
        FireDto c= fService.getFire(Integer.valueOf(id));
        return c;
    }
}