package com.example.junittoolsdemo.services;

import org.springframework.stereotype.Component;

import com.example.junittoolsdemo.utils.DemoUtils;

@Component
public class DemoComponent {

	public int doCalculation(int number) {
		return DemoUtils.calculateSomething(number);
	}

}
