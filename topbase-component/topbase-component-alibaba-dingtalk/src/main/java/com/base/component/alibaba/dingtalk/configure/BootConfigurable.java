package com.base.component.alibaba.dingtalk.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {
		"com.base.component.alibaba.dingtalk.service"
		})
public class BootConfigurable {

}
