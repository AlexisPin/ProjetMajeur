package com.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.dto.VehicleDto;
import com.sp.rest.FetchVehicle;

@Service
public class VehicleService {

	
	@Autowired
	FacilityService fService;
	
    private FetchVehicle fetch = new FetchVehicle();
	
    Map<Integer,Boolean> workingVehicle = new HashMap<Integer,Boolean>();
    
    Map<Integer,Boolean> vehicleOnLine = new HashMap<Integer,Boolean>();
    
	public VehicleDto[] getVehicles() {
		VehicleDto[] vehicles = fetch.getVehicles();
		return vehicles;
	}
	public ArrayList<VehicleDto> getOwnVehicles() {
		VehicleDto[] vehicles = getVehicles();
		ArrayList<VehicleDto> ownVehicle = new ArrayList<>();
		for(VehicleDto vehicle : vehicles) {
			if(fService.getOwnFacilities().contains(vehicle.getFacilityRefID())) {
				ownVehicle.add(vehicle);
			}
		}
		return ownVehicle;
	}
	
	public ArrayList<VehicleDto> getOpponentsVehicles() {
		VehicleDto[] vehicles = getVehicles();
		ArrayList<VehicleDto> opponentVehicle = new ArrayList<>();
		for(VehicleDto vehicle : vehicles) {
			if(!fService.getOwnFacilities().contains(vehicle.getFacilityRefID())) {
				opponentVehicle.add(vehicle);
			}
		}
		return opponentVehicle;
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
	
	public Map<Integer,Boolean> initWorkingVehicle(){
		ArrayList<VehicleDto> ownVehicles = getOwnVehicles();
		for (VehicleDto vehicle : ownVehicles) {
			workingVehicle.putIfAbsent(vehicle.getId(), false);
		}
		return workingVehicle;
	}
	
	public Boolean getWorkingVehicle(int vehicleId) {
		Map<Integer,Boolean> workingVehicle = initWorkingVehicle();
		return workingVehicle.get(vehicleId);
	}
	
	public void setWorkingVehicle(int vehicleId,boolean working) {
		workingVehicle.replace(vehicleId, working);
	}
	
	public Map<Integer,Boolean> initVehicleOnLine(){
		ArrayList<VehicleDto> ownVehicles = getOwnVehicles();
		for (VehicleDto vehicle : ownVehicles) {
			vehicleOnLine.putIfAbsent(vehicle.getId(), false);
		}
		return vehicleOnLine;
	}
	
	
	public void setVehicleOnLine(int vehicleId, boolean line) {
		vehicleOnLine.replace(vehicleId, line);
	}
	
	public Boolean getVehicleOnLine(int vehicleId) {
		Map<Integer,Boolean> vehicleOnLine = initVehicleOnLine();
		return vehicleOnLine.get(vehicleId);
	}
	 
}