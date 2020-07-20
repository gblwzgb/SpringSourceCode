package com.lxl;

import com.lxl.config.AnnotationConfig;
import com.lxl.service.AService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new
				AnnotationConfigApplicationContext(AnnotationConfig.class);
		AService aService = ac.getBean(AService.class);
		System.out.println(aService);
	}

}
