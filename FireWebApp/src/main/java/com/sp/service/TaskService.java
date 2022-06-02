package com.sp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	private ArrayList<Integer> busyVehicules = new ArrayList<Integer>();
	private ArrayList<Integer> onWorkFire = new ArrayList<Integer>();
	private Map<VehicleDto, FireDto> vehicleFireMap = new HashMap<VehicleDto, FireDto>();
	
	@Async
	@Scheduled(initialDelay = 1000 * 30, fixedDelay=Long.MAX_VALUE)
	public void scheduleFixedRateTask() {
		ArrayList<VehicleDto> vehicles = vService.getOwnVehicles();
		FireDto[] fires = fService.getFires();
	    if(fires.length !=0) {
	    	int distance = 1000000000;
	    	if(!vehicles.isEmpty()) {		
	    		for(VehicleDto vehicle : vehicles) {
	    			FireDto interFire = new FireDto();
	    			Coord vCoord = new Coord(vehicle.getLon(),vehicle.getLat());
	    			for(FireDto fire : fires) {
	    				Coord fCoord = new Coord(fire.getLon(),fire.getLat());
	    				int newDistance = GisTools.computeDistance2(vCoord, fCoord);
	    				if(distance > newDistance) {
	    					distance = newDistance;
	    					interFire = fire;
	    				}
	    			}
	    			boolean busy = false;
	    			for(final Entry<VehicleDto, FireDto> entry : vehicleFireMap.entrySet()) {
	    			    final VehicleDto key =  entry.getKey();
	    			    final FireDto value =  entry.getValue();

	    			    if(vehicle.getId() == key.getId() && interFire.getId() == value.getId()) {
	    			    	busy = true;
	    			    }
	    			}
	    			if(!busy && interFire.getId() != null) {
	    				vehicleFireMap.put(vehicle, interFire);
	    			}
	    		}
	    	}
	    triggerEvent();
	    checkVehiculeStatus(vehicles);
		}
	}
	
	private void triggerEvent() {
		for(final Entry<VehicleDto, FireDto> entry : vehicleFireMap.entrySet()) {
		    final VehicleDto vehicle =  entry.getKey();
		    final FireDto fire =  entry.getValue();
		    
		    //System.out.println("key: " + vehicle.getId() + " value: " + fire.getId());
		    Coord fireCoord  = new Coord(fire.getLon(), fire.getLat());
		    deplacement(vehicle, fireCoord);
		}
	}
	

	private void intervention(VehicleDto vehicle, Coord fireCoord) {
		float liquidQuantity = vehicle.getLiquidQuantity();
		if(liquidQuantity > 0) {
			vehicle.setLiquidQuantity(liquidQuantity - vehicle.getType().getLiquidConsumption());
			saveChanges(vehicle);
		}
	}
	
	private void backToFacility(VehicleDto vehicle) {
		FacilityDto facility =  faService.getFacility(vehicle.getFacilityRefID());
		vehicle.setLat(facility.getLat()-facility.getLat()/100000);
		vehicle.setLon(facility.getLon()-facility.getLon()/100000);
		vehicle.setLiquidQuantity(vehicle.getType().getLiquidCapacity());
		saveChanges(vehicle);
	}
	
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

	
	private void deplacement(VehicleDto vehicle, Coord coordFinal) {
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		double dlat = coordFinal.getLat();
		double dlon = coordFinal.getLon();
		double distance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(dlon,dlat));
		System.out.println(distance);
		if( distance > 100) {
			if(vlat < dlat-dlat/100 || vlat > dlat+dlat/100){
				vehicle.setLat(vlat+dlat/100000);
			}
			else {
				vehicle.setLat(vlat-dlat/100000);
			}
			if(vlon < dlon-dlon/100 || vlon > dlon+dlon/100){
				vehicle.setLon(vlon+dlon/100000);
			}
			else {
				vehicle.setLon(vlon-dlon/100000);
			}
			saveChanges(vehicle);
		}
		else {
			intervention(vehicle, coordFinal);
		}
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
