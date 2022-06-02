package com.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	private ArrayList<Integer> onWorkFire = new ArrayList<Integer>();
	private ArrayList<Integer> onWorkVehicle = new ArrayList<Integer>();
	private Map<VehicleDto, FireDto> vehicleFireMap = new HashMap<VehicleDto, FireDto>();
	
	@Scheduled(fixedRate = 1000)
	public void scheduleFixedRateTask() {
		ArrayList<VehicleDto> vehicles = vService.getOwnVehicles();
		FireDto[] fires = fService.getFires();
	    if(fires.length !=0) {
	    	int distance = 1000000000;
	    	double efficiency = 0;
	    	if(!vehicles.isEmpty()) {		
	    		for(VehicleDto vehicle : vehicles) {
	    			FireDto interFire = new FireDto();
	    			VehicleDto interVehicle = new VehicleDto();
	    			Coord vCoord = new Coord(vehicle.getLon(),vehicle.getLat());
	    			for(FireDto fire : fires) {
	    				double newEfficiency = vehicle.getLiquidType().getEfficiency(fire.getType());
	    				if(!onWorkFire.contains(fire.getId()) && !onWorkVehicle.contains(vehicle.getId())) {
	    					Coord fCoord = new Coord(fire.getLon(),fire.getLat());    					
	    					int newDistance = GisTools.computeDistance2(vCoord, fCoord);
	    					//TO-DO check efficiency of vehicle on this fire 
	    					if(efficiency <= newEfficiency) {
	    						efficiency = newEfficiency;
		    					if(distance > newDistance) {
		    						distance = newDistance;
		    						interFire = fire;
		    						interVehicle = vehicle;
		    					}
	    					}
	    				}
	    			}
	    			if(interFire.getId() != null) {
	    					vehicleFireMap.put(interVehicle, interFire);
	    					onWorkVehicle.add(interVehicle.getId());
	    					onWorkFire.add(interFire.getId());
	    			}
	    		triggerEvent();
	    		
	    		}
	    	}
	    }
	}
	
	private void triggerEvent() {
		for(final Entry<VehicleDto, FireDto> entry : vehicleFireMap.entrySet()) {
		    final VehicleDto vehicle =  entry.getKey();
		    final FireDto fire =  entry.getValue();
		    Coord fireCoord  = new Coord(fire.getLon(), fire.getLat());
		    //System.out.println(onWorkFire);
			//System.out.println("vehicule : " + vehicle.getId() + "sur le feu : " + fire.getId());
		    checkVehiculeStatus(vehicle,fireCoord);
		}
	}
	

	private void intervention(VehicleDto vehicle) {
		float liquidQuantity = vehicle.getLiquidQuantity();
		if(liquidQuantity > 0) {
			vehicle.setLiquidQuantity(Math.nextDown(liquidQuantity - vehicle.getType().getLiquidConsumption()));
			saveChanges(vehicle);
		}
	}
	
	private void backToFacility(VehicleDto vehicle,Coord initialVehCoord) {
		FacilityDto facility =  faService.getFacility(vehicle.getFacilityRefID());
		Coord facilityCoord = new Coord(facility.getLon(),facility.getLat());
		deplacement(vehicle, facilityCoord,false);
	}
	
	
	private void checkVehiculeStatus(VehicleDto vehicle,Coord fireCoord) {
			float liquidQuantity = vehicle.getLiquidQuantity();
			float fuelQuantity = vehicle.getFuel();
			final Coord initialVehCoord = new Coord(vehicle.getLon(),vehicle.getLat());
			if(liquidQuantity <= 0 || fuelQuantity <= 0) {
				backToFacility(vehicle,initialVehCoord);
			} else {
				deplacement(vehicle, fireCoord,true);
			}
	}
	
	private void saveChanges(VehicleDto vehicle) {
    	vService.updateVehicle("bd4dd8f2-c28d-46ba-a342-9d9b99259a67", vehicle.getId(), vehicle);
	}

	
	private void deplacement(VehicleDto vehicle, Coord coordFinal, Boolean intervention) {
		
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		double dlat = coordFinal.getLat();
		double dlon = coordFinal.getLon();

		double distance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(dlon,dlat));
		
		double deltaLat =  vlat - dlat;
		double deltaLon = vlon - dlon;
		
		float maxSpeedMS = (float) (vehicle.getType().getMaxSpeed() / 3.60);
	
		double coeff = 10;
		double latTick = deltaLat /coeff;
		double lonTick = deltaLon / coeff;
		int deltaError = 1;
		int travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick));

		while(travelledDistance < Math.round(maxSpeedMS)-deltaError || travelledDistance > Math.round(maxSpeedMS)+deltaError) {
			int deltaDistance =  travelledDistance - Math.round(maxSpeedMS);
			if(deltaDistance < 0) {
				coeff -=0.1;
			}else {
				coeff +=0.1;
			}
			latTick = deltaLat / coeff;
			lonTick = deltaLon / coeff;
			travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick));
		}
		
		
		if( distance > 100) {
			vehicle.setLat(vlat-latTick);
			vehicle.setLon(vlon-lonTick);
			vehicle.setFuel(vehicle.getFuel() - vehicle.getType().getFuelConsumption());
			saveChanges(vehicle);
		}
		else {
			if(intervention) {
				intervention(vehicle);
			}
			else {
				vehicle.setLiquidQuantity(vehicle.getType().getLiquidCapacity());
				vehicle.setFuel(vehicle.getType().getFuelCapacity());
			}
		}
	}
}
