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
public class EmergencyManager {

	@Autowired
	FireService fService;
	
	@Autowired
	VehicleService vService;
	
	@Autowired
	FacilityService faService;
	
	@Autowired
	RouteService rService;
	
	private ArrayList<Integer> onWorkFire = new ArrayList<Integer>();
	private ArrayList<Integer> onWorkVehicle = new ArrayList<Integer>();
	private Map<Integer,VehicleDto > vehicleFireMap = new HashMap<Integer,VehicleDto>();
	

	
	public Map<Integer, VehicleDto> getVehicleFireMap() {
		return vehicleFireMap;
	}
	
	@Scheduled(fixedRate = 1000)
	public void scheduleFixedRateTask() {
		ArrayList<VehicleDto> vehicles = vService.getOwnVehicles();
		FireDto[] fires = fService.getFires();
	    if(fires.length !=0) {
	    	System.out.println(fires.length);
	    	double distance = 1000000000;
	    	double efficiency = 0;
	    	if(!vehicles.isEmpty()) {		
	    		for(VehicleDto vehicle : vehicles) {
	    			FireDto interFire = new FireDto();
	    			VehicleDto interVehicle = new VehicleDto();
	    			for(FireDto fire : fires) {
	    				double newEfficiency = vehicle.getLiquidType().getEfficiency(fire.getType());
	    				if(!onWorkFire.contains(fire.getId()) && !onWorkVehicle.contains(vehicle.getId())) {
	    					Coord fCoord = new Coord(fire.getLon(),fire.getLat());
	    					double newDistance = calculDistance(vehicle, fCoord.getLat(),fCoord.getLon());
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
    			if(interFire.getId() != null && interVehicle.getId() != null) {
    				vehicleFireMap.put(interFire.getId(),interVehicle);
    				onWorkVehicle.add(interVehicle.getId());
    				onWorkFire.add(interFire.getId());
    			}
				vehicle.setCrewMember(vehicle.getType().getVehicleCrewCapacity());
	    	}			
	    		for(Integer onWork : onWorkFire) {
    				if(!fService.getFiresId().contains(onWork)) {
						onWorkVehicle.remove(vehicleFireMap.get(onWork).getId());
						vehicleFireMap.remove(onWork);
						onWorkFire.remove(onWork);
    				}	
    			}
	    		triggerEvent();

	    }
	} else {
		System.out.println("Pas de feu !");
	}
}
	
	private void triggerEvent() {
		for(final Entry<Integer,VehicleDto> entry : vehicleFireMap.entrySet()) {
			final  Integer fireId =  entry.getKey();
			final VehicleDto vehicle =  entry.getValue();
			FireDto fire = fService.getFire(fireId);
			Coord fireCoord  = new Coord(fire.getLon(), fire.getLat());
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
	
	private void backToFacility(VehicleDto vehicle) {
		FacilityDto facility =  faService.getFacility(vehicle.getFacilityRefID());
		Coord facilityCoord = new Coord(facility.getLon(),facility.getLat());
		route(vehicle, facilityCoord,false);
	}
	
	
	private void checkVehiculeStatus(VehicleDto vehicle,Coord fireCoord) {
			float liquidQuantity = vehicle.getLiquidQuantity();
			double distance = calculDistance(vehicle, fireCoord.getLat(),fireCoord.getLon());	
			if(liquidQuantity <= 0 || !checkFuelQuantity(vehicle, distance*2)) {
				backToFacility(vehicle);
			} else {
				route(vehicle, fireCoord,true);
			}
	}
	
	private void saveChanges(VehicleDto vehicle) {
    	vService.updateVehicle("1e9f18a6-4096-4369-ad25-9b3c8451fe27", vehicle.getId(), vehicle);
	}
	
	private void route(VehicleDto vehicle, Coord coordFinal, Boolean intervention) {
		int vehicleId = vehicle.getId();
		ArrayList<ArrayList<Double>> route;
		
		
		ArrayList<Double>lineEnd = null;
		boolean lastLine;
		lastLine = false;
		
		
		if (!vService.getWorkingVehicle(vehicleId)) {
			double latStart = vehicle.getLat();
			double lonStart = vehicle.getLon();
			double latEnd = coordFinal.getLat();
			double lonEnd = coordFinal.getLon();
			
			vService.setWorkingVehicle(vehicleId, true);
			vService.setVehicleOnLine(vehicleId, true);
			rService.putRoute(vehicleId, lonStart, latStart, lonEnd, latEnd);
			route = rService.getRoute(vehicleId);
			lineEnd = route.remove(0);
			lineEnd = route.get(0);
			rService.setRoute(vehicleId, route);
			if (route.isEmpty()) {
				vService.setWorkingVehicle(vehicleId, false);
				rService.deleteRoute(vehicleId);
			}
			
		}
		else {
			if(!vService.getVehicleOnLine(vehicleId)) {
				route = rService.getRoute(vehicleId);
				lineEnd = route.get(0);
				rService.setRoute(vehicleId, route);
				if (route.isEmpty()) {
					vService.setWorkingVehicle(vehicleId, false);
					rService.deleteRoute(vehicleId);
					lastLine = true;
				}
			}
			
		}
		deplacement(vehicle,lineEnd,true,lastLine);
		
		
	}

	private void deplacement(VehicleDto vehicle,ArrayList<Double> lineEnd, Boolean intervention, Boolean lastLine) {
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		double dlat = lineEnd.get(0);
		double dlon = lineEnd.get(1);
		
		
		double distance = calculDistance(vehicle, dlat,dlon);
		
		double deltaLat =  vlat - dlat;
		double deltaLon = vlon - dlon;
		
		float maxSpeedMS = (float) (vehicle.getType().getMaxSpeed()*2 / 3.60);
	
		double coeff = 10;
		double latTick = deltaLat /coeff;
		double lonTick = deltaLon / coeff;
		int deltaError = 1;
		
		int travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick));

		
		 while(travelledDistance < Math.round(maxSpeedMS)-deltaError ||travelledDistance > Math.round(maxSpeedMS)+deltaError) { 
		      int deltaDistance =travelledDistance - Math.round(maxSpeedMS); 
		      if(deltaDistance < 0) { coeff-=0.1; 
		      }
		      else { 
		    	  coeff +=0.1; 
		    	  } 
		      latTick = deltaLat / coeff; 
		      lonTick = deltaLon / coeff; 
		      travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick)); 
		 }
		
		if (lastLine) {
			if( distance > 100) {
				vehicle.setLat(vlat-latTick);
				vehicle.setLon(vlon-lonTick);
				vehicle.setFuel(vehicle.getFuel() - (travelledDistance*vehicle.getType().getFuelConsumption())/1000F);
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
		else {
			if( distance > 10) {
				vehicle.setLat(vlat-latTick);
				vehicle.setLon(vlon-lonTick);
				vehicle.setFuel(vehicle.getFuel() - (travelledDistance*vehicle.getType().getFuelConsumption())/1000F);
				saveChanges(vehicle);
			}
			else {
				vehicle.setLat(dlat);
				vehicle.setLon(dlon);
				vService.setVehicleOnLine(vehicle.getId(), false);
			}
		}
	
	}
	
	private boolean checkFuelQuantity(VehicleDto vehicle, double distanceTotal) {
		float consumptionTotal = (float) ((distanceTotal*vehicle.getType().getFuelConsumption())/1000F);
		boolean ret = true;
		if(vehicle.getFuel() < consumptionTotal) {
			backToFacility(vehicle);
			ret = false;
		}
		return ret;	
	}
	
	private double calculDistance(VehicleDto vehicle, double lon, double lat) {
		
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		double distance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(lon,lat));
		return distance;
		
	}
	
	
}
