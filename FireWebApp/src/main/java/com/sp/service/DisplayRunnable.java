package com.sp.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;

public class DisplayRunnable implements Runnable {

	

	boolean isEnd = false;
	private EmergencyManager emManager = new EmergencyManager();
	private VehicleDto vehicle;
	private Coord fireCoord;
	
	 private VehicleService vService; 
	 private FacilityService faService; 
	 private FireService fService;
	 
	/*
	 * @Autowired FireService fService;
	 * 
	 * @Autowired VehicleService vService;
	 * 
	 * @Autowired FacilityService faService;
	 */

	public DisplayRunnable(VehicleDto vehicle, Coord fireCoord,VehicleService vService,FacilityService faService,FireService fService) {
		
		this.vehicle = vehicle;
		this.fireCoord = fireCoord;
		this.vService = vService;
		this.faService = faService;
		this.fService = fService;

	}

	@Override
	public void run() {
		while (!this.isEnd) {
			try {
				Thread.sleep(10000);
				//System.out.println("runnable : " + this.vService);
				emManager.checkVehiculeStatus(vehicle, fireCoord,vService,faService,fService);
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
