package com.sp.service;

import org.springframework.stereotype.Service;

import com.project.model.dto.VehicleDto;
import com.sp.rest.FetchVehicle;

@Service
public class VehicleService {

    private FetchVehicle fetch = new FetchVehicle();
	
	public VehicleDto[] getVehicles() {
		VehicleDto[] fires = fetch.getVehicles();
		
		return fires;
	}

	public VehicleDto getVehicle(Integer id) {
		VehicleDto fire = fetch.getVehicle(id);
		return fire;
	}
	
	public VehicleDto addVehicle(Integer uuid, VehicleDto vDto) {
		VehicleDto vehicle = fetch.addVehicle(uuid, vDto);
		return vehicle;
	}
	
	public VehicleDto updateVehicle(Integer uuid,Integer id, VehicleDto vDto) {
		VehicleDto vehicle = fetch.updateVehicle(uuid,id, vDto);
		return vehicle;
	}
}