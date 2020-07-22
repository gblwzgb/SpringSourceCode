package com.lxl.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationAOP {

	@Pointcut("execution(public * com.lxl.service.AService.*(..))")
	public void matchCondition() {}

	//使用matchCondition这个切入点进行增强
	@Around("matchCondition()")
	public void cut(ProceedingJoinPoint joinPoint) {
		System.out.println("切到了");
	}

}
