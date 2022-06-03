package com.sp.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sp.rest.FetchRoute;


@Service
public class RouteService {
	private FetchRoute fetch = new FetchRoute();
	
	public ArrayList<ArrayList<Double>> getRoutes() {
		double lon_start =  4.851302121492669;
		double lat_start =  45.72207975807445;
		double lon_end =  4.892309906993183;
		double lat_end =  45.716935733116834;
		
		ResponseEntity<String> result = fetch.getRoute(lon_start,lat_start,lon_end,lat_end);
		String test = result.getBody();
	    JSONObject routeJson = new JSONObject(test); 
	    
	    JSONArray array = routeJson.getJSONArray("routes");
	    JSONObject OUI;
	    OUI = array.getJSONObject(0).getJSONObject("geometry");
	    JSONArray coordString = OUI.getJSONArray("coordinates");
	    ArrayList<ArrayList<Double>> coordList = new ArrayList<ArrayList<Double>>();
	    JSONArray NON;
	    if (coordString != null) { 
	    	  for (int i=0;i<coordString.length();i++){ 
	    	  NON = coordString.getJSONArray(i);
	    	  ArrayList<Double> LatLon = new ArrayList<Double>();
	    	  for (int j=0;j<NON.length();j++) {
	    		  LatLon.add(NON.getDouble(j));	  		 
	    	  }
	    	  coordList.add(LatLon); 
	    	   }
	    }	   	
		return coordList;
	}
}
