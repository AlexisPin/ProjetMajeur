package com.sp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.model.dto.FireDto;
import com.sp.rest.FetchFire;

@Service
public class FireService {

    private FetchFire fetch = new FetchFire();
	
	public FireDto[]  getFires() {
		FireDto[]  fires = fetch.getFires();
		
		return fires;
	}

	public FireDto getFire(Integer id) {
		FireDto fire = fetch.getFire(id);
		return fire;
	}
	
	public List<Integer> getFiresId() {
		List<Integer>  fires = fetch.getFireId();
		
		return fires;
	}
}