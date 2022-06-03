package com.sp.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.project.model.dto.VehicleDto;
import com.sp.model.Route;

import net.minidev.json.parser.JSONParser;


public class FetchRoute {
	
	RestTemplate restTemplate;
	
	
	
	public FetchRoute() {
		this.restTemplate = new RestTemplate();
	}
	
	public ResponseEntity<String> getRoute(double lon_start,double lat_start,double lon_end,double lat_end) {

		String URL_Route = "https://api.mapbox.com/directions/v5/mapbox/driving/" + lon_start + "," + lat_start + ";" + lon_end + "," + lat_end + "?geometries=geojson&access_token=pk.eyJ1IjoiYWRyaWxhcCIsImEiOiJjbDN2Y2N0eGsweWk5M3NueXB0OW50Y29pIn0.J9hlPan0oV4A0KKHgd4CLg";
		ResponseEntity<String> result = restTemplate.getForEntity(URL_Route, String.class);
		
		
		return result;
	}
}

