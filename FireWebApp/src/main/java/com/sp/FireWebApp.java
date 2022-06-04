package com.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FireWebApp {
	
	public static void main(String[] args) {
		SpringApplication.run(FireWebApp.class,args);
	}
}
