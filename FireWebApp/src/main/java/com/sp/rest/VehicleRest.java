package com.sp.rest;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.dto.VehicleDto;
import com.sp.service.VehicleService;

@CrossOrigin 
@RestController
public class VehicleRest {
    @Autowired
    VehicleService vService;
	
    @RequestMapping(method=RequestMethod.POST,value="/vehicle/{uuid}")
    public VehicleDto addVehicle(@PathVariable String uuid, @RequestBody VehicleDto vDto) {
    	return vService.addVehicle(uuid, vDto);
    	
    }
    
    @RequestMapping(method=RequestMethod.PUT,value="/vehicle/{uuid}/{id}")
    public VehicleDto updateVehicle(@PathVariable String uuid,@PathVariable String id, @RequestBody VehicleDto vDto) {
		return vService.updateVehicle(uuid, Integer.valueOf(id), vDto);
        
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/vehicle")
    public VehicleDto[] getVehicles() {
		return vService.getVehicles();
        
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/own/vehicle")
    public ArrayList<VehicleDto> getOwnVehicles() {
		return vService.getOwnVehicles();
        
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/opponent/vehicle")
    public ArrayList<VehicleDto> getOpponentsVehicles() {
		return vService.getOpponentsVehicles();
        
    }
    
    @RequestMapping(method=RequestMethod.GET,value="/vehicle/{id}")
    public VehicleDto getVehicle(@PathVariable String id) {
		return vService.getVehicle(Integer.valueOf(id));
        
    }
    
    
    
    
    @RequestMapping(method=RequestMethod.DELETE,value="/vehicle/{uuid}/{id}")
    public VehicleDto addVehicle(@PathVariable String uuid,@PathVariable String id) {
		return vService.deleteVehicle(uuid,Integer.valueOf(id));
        
    }
    
    @RequestMapping(method=RequestMethod.DELETE,value="/vehicle/{uuid}")
    public void addVehicle(@PathVariable String uuid) {
		 vService.deleteAllVehicle(uuid);
        
    }
}