package com.sp.service;

import java.util.ArrayList;
import java.util.Arrays;
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
	private ArrayList<FireDto> onWorkFire = new ArrayList<FireDto>();
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
	    				FireDto interFire = new FireDto();
	    				int newDistance = GisTools.computeDistance2(vCoord, fCoord);
	    				if(distance > newDistance && !busyVehicules.contains(vehicle) && !onWorkFire.contains(fire)) {
	    					distance = newDistance;
	    					interFireCoord = fCoord;
	    					interFire = fire;
	    					interVehicle = vehicle;
	    				}
	    				if(nbFires == fires.length) {
	    					nbFires = 0;
	    					onWorkFire.add(interFire);
	    					intervention(interVehicle, interFireCoord);
	    					busyVehicules.add(vehicle);
	    				}
	    				for(FireDto workFire : onWorkFire) {
	    					if(!Arrays.asList(fires).contains(workFire)) {
	    						onWorkFire.remove(workFire);
	    						busyVehicules.remove(vehicle);
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	    checkVehiculeStatus(vehicles);
	}   
	@Async
	private void intervention(VehicleDto vehicle, Coord fireCoord) {
		Coord vCoord = new Coord(vehicle.getLat(),vehicle.getLon());
		float liquidQuantity = vehicle.getLiquidQuantity();
		if(liquidQuantity > 0) {
		//vehicle.setLat(fireCoord.getLat()-fireCoord.getLat()/100000);
		//vehicle.setLon(fireCoord.getLon()-fireCoord.getLon()/100000);

		double distance = measure(vCoord, fireCoord);
		System.out.println(distance);
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
	
	private void deplacement(VehicleDto vehicle, Coord fireCoord) {
		
		
	}
	
	private double measure(Coord vCoord, Coord fCoord){ 
	    double R = 6378.137; // Radius of earth in KM
	    double dLat = vCoord.getLat() * Math.PI / 180 - fCoord.getLat() * Math.PI / 180;
	    double dLon = vCoord.getLon() * Math.PI / 180 - fCoord.getLon() * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(vCoord.getLat() * Math.PI / 180) * Math.cos(fCoord.getLat() * Math.PI / 180) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = R * c;
	    return d * 1000; // meters
	}
}
