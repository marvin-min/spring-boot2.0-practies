package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice(basePackages="com.example.demo")
@RestControllerAdvice
public class RateLimitHandler {

	@ResponseBody
	@ExceptionHandler(Throwable.class)
	public Map<String,Object> tooManyRequestException(){
		Map<String,Object> response = new HashMap<>();
		response.put("hasError",true);
		response.put("msg", "今日票数已经用完");
		return response;
	}

}
