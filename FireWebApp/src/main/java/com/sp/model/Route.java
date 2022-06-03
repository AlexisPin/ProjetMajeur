package com.sp.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	private ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
	private int distance;
	
	public List<ArrayList<Integer>> getRoute(){
		return routes;
	}
	
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Route [routes=" + routes + ", distance=" + distance + "]";
	}
	
	
}
