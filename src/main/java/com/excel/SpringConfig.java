package com.excel;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan({"com.excel.tag*", "com.excel.beanProcessor*", "com.excel.generate*", "com.excel.processor*"})
public class SpringConfig {
}
