package com.sp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.model.dto.VehicleDto;

@Service
public class VehicleFireService {

	
	@Autowired
	EmergencyManager emService;
	
	

	
	public Map<Integer, VehicleDto>  getVehiclesFiresCoord() {
		
		Map<Integer, VehicleDto> vehicleFireMap = emService.getVehicleFireMap();
		
		return vehicleFireMap;
	}

	
}