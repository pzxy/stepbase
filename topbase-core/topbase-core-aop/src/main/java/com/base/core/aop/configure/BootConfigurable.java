package com.base.core.aop.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author start 
 */
@Configurable
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@ComponentScan(basePackages = {"com.base.core.aop.service"})
public class BootConfigurable {

}
