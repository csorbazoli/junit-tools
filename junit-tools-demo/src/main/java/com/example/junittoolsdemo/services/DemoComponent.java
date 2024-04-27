package com.example.junittoolsdemo.services;

import org.springframework.stereotype.Component;

import com.example.junittoolsdemo.utils.DemoUtils;

@Component
public class DemoComponent {

	private static DemoService STATIC_SERVICE;

	private DemoService service;
	private final DemoService finalService = new DemoService();

	public int doCalculation(int number) {
		return DemoUtils.calculateSomething(number);
	}

}
