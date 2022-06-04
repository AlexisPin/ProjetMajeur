package com.sp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class FetchRoute {

	RestTemplate restTemplate;


	public FetchRoute() {
		this.restTemplate = new RestTemplate();
	}

	public ResponseEntity<String> getRoute(double lon_start,double lat_start,double lon_end,double lat_end) {
		
		String URL_Route = "https://api.mapbox.com/directions/v5/mapbox/driving/" + lon_start +"," + lat_start + ";" + lon_end + "," + lat_end + "?geometries=geojson&access_token=pk.eyJ1IjoiYWRyaWxhcCIsImEiOiJjbDN2Y2N0eGsweWk5M3NueXB0OW50Y29pIn0.J9hlPan0oV4A0KKHgd4CLg";
		
		ResponseEntity<String> result = restTemplate.getForEntity(URL_Route, String.class);


		return result;
	}
}