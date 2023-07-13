package com.base.component.alibaba.aliyun.configure;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author start 
 */
@Configurable
@ComponentScan(basePackages = {
		"com.base.component.alibaba.aliyun.oss",
		"com.base.component.alibaba.aliyun.sms"
		})
public class BootConfigurable {

}
