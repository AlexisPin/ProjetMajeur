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
	
	static final String URL_Route = "https://api.mapbox.com/directions/v5/mapbox/cycling/-84.518641,39.134270;-84.512023,39.102779?geometries=geojson&access_token=pk.eyJ1IjoiYWRyaWxhcCIsImEiOiJjbDN2Y2N0eGsweWk5M3NueXB0OW50Y29pIn0.J9hlPan0oV4A0KKHgd4CLg";
	
	public FetchRoute() {
		this.restTemplate = new RestTemplate();
	}
	
	public ResponseEntity<String> getRoute() {

		
		ResponseEntity<String> result = restTemplate.getForEntity(URL_Route, String.class);
		
		
		return result;
	}
}

