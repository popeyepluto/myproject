package com.homework.converter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homework.converter.service.JsonToSoqlService;

@RestController
@RequestMapping("/api")
public class JsonToSoqlController {
	
	@Autowired
	private JsonToSoqlService jsonToSqlService;
	
	@PostMapping("/convert")
	public String getData(@RequestBody String json) {
		return jsonToSqlService.convert(json);
	}

}
