package com.lxl.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.lxl")
@EnableAspectJAutoProxy
public class AnnotationConfig {
}
