package com.example.junittoolsdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.junittoolsdemo.models.DemoObject;
import com.example.junittoolsdemo.services.DemoService;

@RestController
@RequestMapping("/demo")
public class DemoController {

	private final DemoService service;

	public DemoController(DemoService service) {
		this.service = service;
	}

	@GetMapping("/test/{name}")
	public DemoObject greet(@PathVariable String name) {
		DemoObject ret = new DemoObject();
		ret.setDemoString(service.doSomething(name));
		ret.setDemoInt(name.length());
		return ret;
	}

	@PostMapping("/update")
	public String update(@RequestHeader(required = false) String demoHeader, @RequestBody DemoObject object) {
		service.updateObject(object, demoHeader);
		return object.getDemoString();
	}

}