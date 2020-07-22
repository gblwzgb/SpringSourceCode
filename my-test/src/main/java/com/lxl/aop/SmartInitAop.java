package com.lxl.aop;

import com.lxl.service.AService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

//@Component
public class SmartInitAop implements BeanPostProcessor, PriorityOrdered {

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("SmartInitAop注册成功");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("AService")) {
			System.out.println("替换");
			return new AService();
		}
		return bean;
	}
}
