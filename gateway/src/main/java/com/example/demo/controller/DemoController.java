package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	
	@GetMapping("/home")
	public String home() throws InterruptedException {
		System.out.println("do something, need 1s to finish");
		Thread.sleep(1000);
		System.out.println("things done");
		return "HELlo";
	}
	

}
