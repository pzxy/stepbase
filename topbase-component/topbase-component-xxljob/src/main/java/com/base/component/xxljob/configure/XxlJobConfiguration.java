package com.base.component.xxljob.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * @author start 
 */
@Configuration
public class XxlJobConfiguration {
	
	@Bean
	@ConfigurationProperties(prefix = "xxl.job")
	public XxlJobSpringExecutor xxljob() {
		XxlJobSpringExecutor executor=new XxlJobSpringExecutor();
		executor.setPort(9999);
		executor.setLogRetentionDays(30);
		return executor;
	}
	
}
