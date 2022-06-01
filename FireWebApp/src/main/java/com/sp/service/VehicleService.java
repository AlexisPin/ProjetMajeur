package com.sp.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.project.model.dto.VehicleDto;
import com.sp.rest.FetchVehicle;

@Service
public class VehicleService {

    private FetchVehicle fetch = new FetchVehicle();
	
	public VehicleDto[] getVehicles() {
		VehicleDto[] vehicles = fetch.getVehicles();
		return vehicles;
	}
	public ArrayList<VehicleDto> getOwnVehicles() {
		VehicleDto[] vehicles = getVehicles();
		ArrayList<VehicleDto> ownVehicle = new ArrayList<>();
		for(VehicleDto vehicle : vehicles) {
			if(vehicle.getFacilityRefID() == 80) {
				ownVehicle.add(vehicle);
			}
		}
		return ownVehicle;
	}


	public VehicleDto getVehicle(Integer id) {
		VehicleDto vehicle = fetch.getVehicle(id);
		return vehicle;
	}
	
	public VehicleDto addVehicle(String uuid, VehicleDto vDto) {
		VehicleDto vehicle = fetch.addVehicle(uuid, vDto);
		return vehicle;
	}
	
	public VehicleDto updateVehicle(String uuid,Integer id, VehicleDto vDto) {
		VehicleDto vehicle = fetch.updateVehicle(uuid,id, vDto);
		return vehicle;
	}

	public VehicleDto deleteVehicle(String uuid, Integer id) {
		return fetch.deleteVehicle(uuid,id);
	}

	public void deleteAllVehicle(String uuid) {
		fetch.deleteAllVehicle(uuid);
	}
}