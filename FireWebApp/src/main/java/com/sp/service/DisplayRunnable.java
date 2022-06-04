package com.sp.service;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;

public class DisplayRunnable implements Runnable {


	boolean isEnd = false;
	private EmergencyManager emManager = new EmergencyManager();
	private VehicleDto vehicle;
	private Coord fireCoord;
	
	 private VehicleService vService; 
	 private FacilityService faService; 
	 private RouteService rService;
	 

	public DisplayRunnable(VehicleDto vehicle, Coord fireCoord,VehicleService vService,FacilityService faService,RouteService rService) {
		
		this.vehicle = vehicle;
		this.fireCoord = fireCoord;
		this.vService = vService;
		this.faService = faService;
		this.rService = rService;

	}

	@Override
	public void run() {
		while (!this.isEnd) {
			try {
				Thread.sleep(10000);
				emManager.checkVehiculeStatus(vehicle, fireCoord,vService,faService,rService);
				stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		this.isEnd = true;
	}

}
