package com.sp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.model.dto.Coord;
import com.project.model.dto.FacilityDto;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import com.project.tools.GisTools;

@EnableAsync
@Component
public class TaskService {

	@Autowired
	FireService fService;
	
	@Autowired
	VehicleService vService;
	
	@Autowired
	FacilityService faService;
	
	private ArrayList<VehicleDto> busyVehicules = new ArrayList<VehicleDto>();
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void scheduleFixedRateTask() {
		ArrayList<VehicleDto> vehicles = vService.getOwnVehicles();
		ArrayList<VehicleDto> availableVehicules = vehicles;
		FireDto[] fires = fService.getFires();
	    if(fires.length !=0) {
	    	int distance = 1000000;
	    	Coord interFireCoord = new Coord();
	    	VehicleDto interVehicle = new VehicleDto();
	    	if(!availableVehicules.isEmpty()) {		
	    		for(VehicleDto vehicle : availableVehicules) {
	    			Coord vCoord = new Coord(vehicle.getLon(),vehicle.getLat());
	    			int nbFires = 0;
	    			for(FireDto fire : fires) {
	    				nbFires+=1;
	    				Coord fCoord = new Coord(fire.getLon(),fire.getLat());
	    				int newDistance = GisTools.computeDistance2(vCoord, fCoord);
	    				if(distance > newDistance && !busyVehicules.contains(vehicle)) {
	    					distance = newDistance;
	    					interFireCoord = fCoord;
	    					interVehicle = vehicle;
	    				}
	    				if(nbFires == fires.length) {
	    					intervention(interVehicle, interFireCoord);
	    					busyVehicules.add(vehicle);
	    				}
	    			}
	    		}
	    	}
	    	checkVehiculeStatus(vehicles);
	    }
	}   
	@Async
	private void intervention(VehicleDto vehicle, Coord fireCoord) {
		float liquidQuantity = vehicle.getLiquidQuantity();
		if(liquidQuantity > 0) {
		vehicle.setLat(fireCoord.getLat()-fireCoord.getLat()/100000);
		vehicle.setLon(fireCoord.getLon()-fireCoord.getLon()/100000);
		vehicle.setLiquidQuantity(liquidQuantity - vehicle.getType().getLiquidConsumption());
		saveChanges(vehicle);
		}
	}
	
	@Async
	private void backToFacility(VehicleDto vehicle) {
		FacilityDto facility =  faService.getFacility(vehicle.getFacilityRefID());
		vehicle.setLat(facility.getLat()-facility.getLat()/100000);
		vehicle.setLon(facility.getLon()-facility.getLon()/100000);
		vehicle.setLiquidQuantity(vehicle.getType().getLiquidCapacity());
		saveChanges(vehicle);
	}
	
	@Async
	private void checkVehiculeStatus(ArrayList<VehicleDto> vehicles) {
		
		for(VehicleDto vehicle : vehicles) {
			float liquidQuantity = vehicle.getLiquidQuantity();
			if(liquidQuantity <= 0) {
				backToFacility(vehicle);
			}
		}
	}
	
	private void saveChanges(VehicleDto vehicle) {
    	vService.updateVehicle("bd4dd8f2-c28d-46ba-a342-9d9b99259a67", vehicle.getId(), vehicle);
	}
}
