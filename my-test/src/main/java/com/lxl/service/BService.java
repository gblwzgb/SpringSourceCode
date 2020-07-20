package com.lxl.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BService {

	@Resource
	private AService aService;

}
