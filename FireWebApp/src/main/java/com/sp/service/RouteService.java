package com.sp.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.model.dto.FireDto;
import com.sp.model.Route;
import com.sp.rest.FetchFire;
import com.sp.rest.FetchRoute;


@Service
public class RouteService {
	private FetchRoute fetch = new FetchRoute();
	
	public ArrayList<ArrayList<Float>> getRoutes() {
		ResponseEntity<String> result = fetch.getRoute();
		String test = result.getBody();
	    JSONObject routeJson = new JSONObject(test); 
	    
	    JSONArray array = routeJson.getJSONArray("routes");
	    JSONObject OUI;
	    OUI = array.getJSONObject(0).getJSONObject("geometry");
	    JSONArray coordString = OUI.getJSONArray("coordinates");
	    ArrayList<ArrayList<Float>> coordList = new ArrayList<ArrayList<Float>>();
	    JSONArray NON;
	    if (coordString != null) { 
	    	  for (int i=0;i<coordString.length();i++){ 
	    	  NON = coordString.getJSONArray(i);
	    	  ArrayList<Float> LatLon = new ArrayList<Float>();
	    	  for (int j=0;j<NON.length();j++) {
	    		  LatLon.add(NON.getFloat(j));	  		 
	    	  }
	    	  coordList.add(LatLon); 
	    	   }
	    }	   	
		return coordList;
	}
}
