package com.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sp.rest.FetchRoute;


@Service
public class RouteService {
	private FetchRoute fetch = new FetchRoute();
	
	Map<Integer,ArrayList<ArrayList<Double>>> routeMap = new HashMap<Integer,ArrayList<ArrayList<Double>>>();
	
	
	public ArrayList<ArrayList<Double>> getRoutes(double lon_start, double lat_start, double lon_end, double lat_end) {		
		ResponseEntity<String> result = fetch.getRoute(lon_start,lat_start,lon_end,lat_end);
		String route = result.getBody();
	    JSONObject routeJson = new JSONObject(route);   
	    JSONArray routeArray = routeJson.getJSONArray("routes").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
	    ArrayList<ArrayList<Double>> coordList = new ArrayList<ArrayList<Double>>();
	    JSONArray coordArray;
	    if (routeArray != null) { 
	    	  for (int i=0;i<routeArray.length();i++){ 
	    	  coordArray = routeArray.getJSONArray(i);
	    	  ArrayList<Double> LatLon = new ArrayList<Double>();
	    	  for (int j=0;j<coordArray.length();j++) {
	    		  LatLon.add(coordArray.getDouble(j));	  		 
	    	  }
	    	  coordList.add(LatLon); 
	    	   }
	    }	   	
		return coordList;
	}
	
	public Double getDistance(double lon_start, double lat_start, double lon_end, double lat_end) {
		ResponseEntity<String> result = fetch.getRoute(lon_start,lat_start,lon_end,lat_end);
		String route = result.getBody();
	    JSONObject routeJson = new JSONObject(route); 
		double distance = routeJson.getJSONArray("routes").getJSONObject(0).getDouble("distance");
		return distance;
	}
	
	public void putRoute(int vehicleId,double lonStart, double latStart, double lonEnd, double latEnd) {
	 	ArrayList<ArrayList<Double>> route = getRoutes(lonStart,latStart,lonEnd,latEnd);
	 	routeMap.put(vehicleId, route);
	}
	
	public ArrayList<ArrayList<Double>> getRoute(int vehicleId){
		return routeMap.get(vehicleId);
	}
	
	public void setRoute(int vehicleId,ArrayList<ArrayList<Double>> route) {
		routeMap.replace(vehicleId, route);
	}
	
	public void deleteRoute(int vehicleId) {
		routeMap.remove(vehicleId);
	}
	
	
	public Map<Integer, ArrayList<ArrayList<Double>>> getAllRoute() {
		return this.routeMap;
	}
	
}
