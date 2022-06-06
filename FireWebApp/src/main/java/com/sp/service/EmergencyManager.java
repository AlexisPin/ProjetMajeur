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
	
	
	DisplayRunnable dRunnable;
	private Thread displayThread;
	
	
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
	    	}			
	    		for(Integer onWork : onWorkFire) {
    				if(!fService.getFiresId().contains(onWork)) { 					
    					int vehicleId = vehicleFireMap.get(onWork).getId();
    					//System.out.println("Le véhicule : " + vehicleId + " arrete de bosser"); ad
						onWorkVehicle.remove(vehicleId);
						vehicleFireMap.remove(onWork);
						onWorkFire.remove(onWork);
						vService.setWorkingVehicle(vehicleId, false);
						rService.deleteRoute(vehicleId);
						vService.setLastLine(vehicleId, false);
						vService.setVehicleOnLine(vehicleId, false);
						
    				}	
    			}
	    		triggerEvent();

	    }
	} 
}
	
	private void triggerEvent() {
		
		/*
		for(final Entry<Integer,VehicleDto> entry : vehicleFireMap.entrySet()) {
			final  Integer fireId =  entry.getKey();
			final VehicleDto vehicle =  entry.getValue();
			System.out.println("véhicule : " + vehicle.getId() + " feu : " + fireId);
			
		}
		*/
		
		
		for(final Entry<Integer,VehicleDto> entry : vehicleFireMap.entrySet()) {
			final  Integer fireId =  entry.getKey();
			final VehicleDto vehicle =  entry.getValue();
			FireDto fire = fService.getFire(fireId);
			Coord fireCoord  = new Coord(fire.getLon(), fire.getLat());
			//checkVehiculeStatus(vehicle,fireCoord);
			
			this.dRunnable=new DisplayRunnable(vehicle, fireCoord,vService,faService,rService);
			displayThread=new Thread(dRunnable);
			displayThread.start();
		}
	}
	

	private void intervention(VehicleDto vehicle,VehicleService vService, RouteService rService) {
		float liquidQuantity = vehicle.getLiquidQuantity();
		boolean timer = false;
		if(liquidQuantity > 0) {
			//vehicle.setLiquidQuantity(Math.nextDown(liquidQuantity - vehicle.getType().getLiquidConsumption()));
			vehicle.setLiquidQuantity(Math.nextDown(liquidQuantity - 2));
			if(vehicle.getLiquidQuantity() <= 0) {
				//System.out.println("vehicle : " + vehicle.getId() + " plus de liquide" ); //ad
				vService.setWorkingVehicle(vehicle.getId(), false);
			}
			timer = false;
			while(!timer) {
				timer = saveChanges(vehicle,vService);
			}
			vService.setMoving(vehicle.getId(), false);
		}
	}
	
	private void backToFacility(VehicleDto vehicle,VehicleService vService,FacilityService faService,RouteService rService) {
		FacilityDto facility =  faService.getFacility(vehicle.getFacilityRefID());
		Coord facilityCoord = new Coord(facility.getLon(),facility.getLat());
		/*
		vehicle.setLat(45.77036991);
		vehicle.setLon(4.88580151);
		saveChanges(vehicle,vService);
		*/
		route(vehicle, facilityCoord,false, vService, rService);
	}
	
	
	public void checkVehiculeStatus(VehicleDto vehicle,Coord fireCoord,VehicleService vService,FacilityService faService,RouteService rService) {
			
			//backToFacility(vehicle, vService, faService, rService);
			if(!vService.getMoving(vehicle.getId())) {
				float liquidQuantity = vehicle.getLiquidQuantity();
				double distance = calculDistance(vehicle, fireCoord.getLat(),fireCoord.getLon());
				//System.out.println("vehicle : " + vehicle.getId() + " liquide : " + vehicle.getLiquidQuantity()); //ad
				if(liquidQuantity <= 0 || !checkFuelQuantity(vehicle, distance*2, vService, faService, rService)) {
					//System.out.println("vehicle : " + vehicle.getId() + " rentre à la base" ); //ad
					backToFacility(vehicle, vService, faService, rService);	
				} else {
					//System.out.println("vehicle : " + vehicle.getId() + " part affronter un feu" ); //ad
					route(vehicle, fireCoord,true, vService, rService);
					
				}
			}
			
			
	}
	
	private boolean saveChanges(VehicleDto vehicle,VehicleService vService) {
    	vService.updateVehicle("1e9f18a6-4096-4369-ad25-9b3c8451fe27", vehicle.getId(), vehicle);
    	return true;
	}
	
	private void route(VehicleDto vehicle, Coord coordFinal, Boolean intervention,VehicleService vService,RouteService rService) {
		int vehicleId = vehicle.getId();
		ArrayList<ArrayList<Double>> route;
		
		
		ArrayList<Double>lineEnd = null;
		
		lineEnd = vService.getLineEnd(vehicleId);
	
		vService.setMoving(vehicleId, true);
		
		//System.out.println("vehicle : " + vehicleId + " work : " + vService.getWorkingVehicle(vehicleId)); //add
		
		
		
		if (!vService.getWorkingVehicle(vehicleId)) {
			double latStart = vehicle.getLat();
			double lonStart = vehicle.getLon();
			double latEnd = coordFinal.getLat();
			double lonEnd = coordFinal.getLon();
			
			
			//System.out.println("vehicule : " + vehicleId + " coord : " + latStart + " " + lonStart + " dest coord : " + latEnd + " " + lonEnd); //ad
			
			
			
			vService.setWorkingVehicle(vehicleId, true);
			vService.setVehicleOnLine(vehicleId, true);
			vService.setLastLine(vehicleId, false);
			rService.deleteRoute(vehicleId);
			
			rService.putRoute(vehicleId, lonStart, latStart, lonEnd, latEnd);
			route = rService.getRoute(vehicleId);
			//System.out.println("1 vehicle : " + vehicleId + " route : " + route); ad
			
			lineEnd = route.remove(0);
			vService.setLineEnd(vehicleId, lineEnd);
			//System.out.println("LineEnd premier : " + lineEnd);
			vehicle.setLat(lineEnd.get(1));
			vehicle.setLon(lineEnd.get(0));
			
			//System.out.println("LineEnd start : " + lineEnd);
			rService.setRoute(vehicleId, route);
			if (route.isEmpty()) {
			//	System.out.println("la route est vide du premier coup");
				vService.setLastLine(vehicleId, true);
				vService.setLineEnd(vehicleId, lineEnd);
			}
			else {
				lineEnd = route.get(0);
				//System.out.println("véhicule : " + vehicleId + " LineEnd : " + lineEnd);
				vService.setLineEnd(vehicleId, lineEnd);
			}
			deplacement(vehicle,intervention,vService, rService);
			
		}
		else {
			
			if(!vService.getVehicleOnLine(vehicleId)) {
				route = rService.getRoute(vehicleId);
				//System.out.println("2 vehicle : " + vehicleId + " route : " + route); //ad
				//System.out.println("LineEnd en cours: " + lineEnd);
				if (route.isEmpty()) {
				//	System.out.println("la route est vide");
				vService.setLastLine(vehicleId, true);
				}
				else {
					lineEnd = route.remove(0);
					rService.setRoute(vehicleId, route);
					vService.setLineEnd(vehicleId, lineEnd);
				}
				deplacement(vehicle,intervention, vService, rService);
			}
			else {
				if(vService.getLastLine(vehicleId)) {
					deplacement(vehicle,intervention, vService, rService);
				}
				else {
					route = rService.getRoute(vehicleId);
					//System.out.println("3 vehicle : " + vehicleId + " route : " + route); //ad
					lineEnd = route.get(0);
					//System.out.println("LineEnd en cours: " + lineEnd);
					rService.setRoute(vehicleId, route);
					vService.setLineEnd(vehicleId, lineEnd);
					deplacement(vehicle,intervention, vService, rService);
				}
				
			}
			
		}
	}

	private void deplacement(VehicleDto vehicle, Boolean intervention,VehicleService vService, RouteService rService) {
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		
		
		ArrayList<Double> lineEnd = vService.getLineEnd(vehicle.getId());
		double dlat = lineEnd.get(1);
		double dlon = lineEnd.get(0);		  
	
		boolean timer = false;
		
		double distance = calculDistance(vehicle, dlat,dlon);
		double deltaLat =  vlat - dlat;
		double deltaLon = vlon - dlon;
		
		float maxSpeedMS = (float) (vehicle.getType().getMaxSpeed()*2 / 3.60);
	
		double coeff = 20;
		double latTick = deltaLat /coeff;
		double lonTick = deltaLon / coeff;
		int deltaError = 2;
		
		int travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick));

		/* while(travelledDistance > Math.round(maxSpeedMS)-deltaError || travelledDistance < Math.round(maxSpeedMS)+deltaError) { 
		      int deltaDistance =travelledDistance - Math.round(maxSpeedMS); 
		      if(deltaDistance < 0) { 
		    	  coeff-=0.1; 
		      }
		      else { 
		    	  coeff +=0.1; 
		    	  } 
		      latTick = deltaLat / coeff; 
		      lonTick = deltaLon / coeff; 
		      
		      travelledDistance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(vlon-lonTick,vlat-latTick)); 
		 }*/
		
		if (vService.getLastLine(vehicle.getId())) {
			if( distance > 100) {
				vehicle.setLat(vlat-latTick);
				vehicle.setLon(vlon-lonTick);
				vehicle.setFuel(vehicle.getFuel() - (travelledDistance*vehicle.getType().getFuelConsumption())/1000F);
				timer = false;
				while(!timer) {
					timer = saveChanges(vehicle,vService);
				}
				vService.setMoving(vehicle.getId(), false);
			}
			else {
				
				if(intervention) {
					//System.out.println("Le véhicule : " + vehicle.getId() + " est sur un feu"); ad
					intervention(vehicle,vService,rService);
				}
				else {
					//System.out.println("Le véhicule : " + vehicle.getId() + " est à la caserne"); ad
					//System.out.println("refuel : " + vehicle.getId());
					vehicle.setLiquidQuantity(vehicle.getType().getLiquidCapacity());
					if (vehicle.getLiquidQuantity() == vehicle.getType().getLiquidCapacity() ) {
						//System.out.println("vehicle : " + vehicle.getId() + " rempli retour à la guerre" ); ad
						vService.setWorkingVehicle(vehicle.getId(), false);
					}
					vehicle.setFuel(vehicle.getType().getFuelCapacity());
					timer = false;
					while(!timer) {
						timer = saveChanges(vehicle,vService);
					}
					vService.setMoving(vehicle.getId(), false);
				}
			}
		}
		else {
			
			if( distance > 10) {
				vehicle.setLat(vlat-latTick);
				vehicle.setLon(vlon-lonTick);
				vehicle.setFuel(vehicle.getFuel() - (travelledDistance*vehicle.getType().getFuelConsumption())/1000F);
				
				/*if(!intervention) {
					System.out.println("Le véhicule : " + vehicle.getId() + " se dirige vers la caserne");
				}
				else {
					System.out.println("Le véhicule : " + vehicle.getId() + " se dirige vers un feu");
				}*/
				timer = false;
				while(!timer) {
					timer = saveChanges(vehicle,vService);
				}
				vService.setMoving(vehicle.getId(), false);
				
			}
			else {	
				vehicle.setLat(dlat);
				vehicle.setLon(dlon);
				vService.setVehicleOnLine(vehicle.getId(), false);
				timer = false;
				while(!timer) {
					timer = saveChanges(vehicle,vService);
				}
			
				vService.setMoving(vehicle.getId(), false);
			}
		}
	
	}
	
	private boolean checkFuelQuantity(VehicleDto vehicle, double distanceTotal,VehicleService vService,FacilityService faService,RouteService rService) {
		float consumptionTotal = (float) ((distanceTotal*vehicle.getType().getFuelConsumption())/1000F);
		boolean ret = true;
		if(vehicle.getFuel() < consumptionTotal) {
			backToFacility(vehicle, vService, faService,rService);
			ret = false;
		}
		return ret;	
	}
	
	private double calculDistance(VehicleDto vehicle, double lon, double lat) {
		
		double vlat = vehicle.getLat();
		double vlon = vehicle.getLon();
		double distance = GisTools.computeDistance2(new Coord(vlon,vlat),new Coord(lat,lon));
		return distance;
		
	}
	
	
}
