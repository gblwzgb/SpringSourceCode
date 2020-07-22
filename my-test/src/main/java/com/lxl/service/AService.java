package com.lxl.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AService {

	@Resource
	private BService bService;

	public void say(){
		System.out.println("我是AService");
	}

}
