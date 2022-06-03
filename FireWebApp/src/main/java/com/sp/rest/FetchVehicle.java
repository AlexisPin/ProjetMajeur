package com.sp.rest;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.VehicleDto;


public class FetchVehicle {

	static final String URL_VEHICLE = "http://vps.cpe-sn.fr:8081/vehicle";
	
	RestTemplate restTemplate;

    public FetchVehicle() {
		this.restTemplate = new RestTemplate();
    }
    
    
	public VehicleDto getVehicle(Integer id) {
		// Send request with GET method and default Headers.
		ResponseEntity<VehicleDto> result = restTemplate.getForEntity(URL_VEHICLE+"/"+id, VehicleDto.class);
		
		VehicleDto vehicle= result.getBody();
		return vehicle;
	}

	public VehicleDto[] getVehicles() {

		// Send request with GET method and default Headers.
		ResponseEntity<VehicleDto[]> result = restTemplate.getForEntity(URL_VEHICLE, VehicleDto[].class);
		
		VehicleDto[] vehicles= result.getBody();
		return vehicles;
	}

	public VehicleDto updateVehicle(String uuid, Integer id,VehicleDto vDto ) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		// Data attached to the request.
		HttpEntity<?> requestBody = new HttpEntity<>(vDto, headers);
		
		// Send request with PUT method.
		restTemplate.postForEntity(URL_VEHICLE+"/"+uuid+"/", requestBody, VehicleDto.class);
		return vDto;
	}

	public VehicleDto addVehicle(String uuid, VehicleDto vDto) {

			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

			// Data attached to the request.
			HttpEntity<?> requestBody = new HttpEntity<>(vDto, headers);
			
			// Send request with PUT method.
			restTemplate.postForEntity(URL_VEHICLE+"/"+uuid, requestBody, VehicleDto.class );
			return vDto;
			
	}
	
	public VehicleDto deleteVehicle(String uuid, Integer id) {
		
		VehicleDto vehicle =  getVehicle(id);
		restTemplate.delete(URL_VEHICLE+"/"+uuid+"/"+id, new Object[] {});
		return vehicle;
	}


	public void deleteAllVehicle(String uuid) {
		restTemplate.delete(URL_VEHICLE+"/"+uuid, new Object[] {});
	}

}