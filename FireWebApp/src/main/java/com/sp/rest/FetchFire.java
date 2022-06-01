package com.sp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.FireDto;

public class FetchFire {

	static final String URL_FIRES = "http://vps.cpe-sn.fr:8081/fire";
	static final String URL_DISTANCE = "http://vps.cpe-sn.fr:8081/fire/distance";
	
	RestTemplate restTemplate;

    public FetchFire() {
		this.restTemplate = new RestTemplate();
    }
    
	public FireDto[] getFires() {

		// Send request with GET method and default Headers.
		ResponseEntity<FireDto[] > result = restTemplate.getForEntity(URL_FIRES, FireDto[] .class);
		
		FireDto[] fires = result.getBody();
		return fires;
	}
	
	public FireDto getFire(int id) {

		// Send request with GET method and default Headers.
		ResponseEntity<FireDto> result = restTemplate.getForEntity(URL_FIRES+"/"+id, FireDto.class);
		
		FireDto fire= result.getBody();
		return fire;
	}
	
	public Integer getDistanceBetweenCoord(Integer xFire,Integer yFire,Integer xVhcl,Integer yVhcl ) {

		// Send request with GET method and default Headers.
		ResponseEntity<Integer> result = restTemplate.getForEntity(URL_DISTANCE+"?latCoord1=" +xFire + "&latCoord2=" +xVhcl +"&lonCoord1="+yFire+"0&lonCoord2="+yVhcl+"", Integer.class);
		 
		Integer distance = result.getBody();
		return distance;
	}
	
	
}