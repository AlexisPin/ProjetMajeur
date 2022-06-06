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
    
    Map<Integer,Boolean> lastLineMap = new HashMap<Integer,Boolean>();
	
    Map<Integer,ArrayList<Double>> lineEndMap = new HashMap<Integer,ArrayList<Double>>();
    
    Map<Integer,Boolean> movingMap = new HashMap<Integer,Boolean>();
    
    
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
	
	
	public Map<Integer,Boolean> initLastLine(){
		ArrayList<VehicleDto> ownVehicles = getOwnVehicles();
		for (VehicleDto vehicle : ownVehicles) {
			lastLineMap.putIfAbsent(vehicle.getId(), false);
		}
		return lastLineMap;
	}
	
	
	public Boolean getLastLine(int vehicleId) {
		Map<Integer,Boolean> lastLine = initLastLine();
		return lastLine.get(vehicleId);
	}
	
	public void setLastLine(int vehicleId, boolean lastLine) {
		lastLineMap.replace(vehicleId, lastLine);
	}
	
	public Map<Integer,ArrayList<Double>> initLineEnd(){
		ArrayList<VehicleDto> ownVehicles = getOwnVehicles();
		for (VehicleDto vehicle : ownVehicles) {
			ArrayList<Double> r = new ArrayList<Double>();
			r.add(0.0);
			r.add(0.0);
			lineEndMap.putIfAbsent(vehicle.getId(),r);
		}
		return lineEndMap;
	}
	
	
	public ArrayList<Double> getLineEnd(int vehicleId) {
		Map<Integer,ArrayList<Double>> lineEnd = initLineEnd();
		return lineEnd.get(vehicleId);
	}
	
	public void setLineEnd(int vehicleId, ArrayList<Double> line) {
		lineEndMap.replace(vehicleId, line);
	}
	
	public Map<Integer,Boolean> initMoving(){
		ArrayList<VehicleDto> ownVehicles = getOwnVehicles();
		for (VehicleDto vehicle : ownVehicles) {
			movingMap.putIfAbsent(vehicle.getId(), false);
		}
		return movingMap;
	}
	
	
	public Boolean getMoving(int vehicleId) {
		Map<Integer,Boolean> moving = initMoving();
		return moving.get(vehicleId);
	}
	
	public void setMoving(int vehicleId, boolean lastLine) {
		movingMap.replace(vehicleId, lastLine);
	}
	
	
}