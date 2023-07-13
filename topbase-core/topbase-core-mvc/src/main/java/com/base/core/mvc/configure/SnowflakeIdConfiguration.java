package com.base.core.mvc.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gitee.magic.core.utils.SnowflakeIdWorker;
import com.gitee.magic.framework.base.constant.Config;

/**
 * @author start 
 */
@Configuration
public class SnowflakeIdConfiguration {
	
	@Bean
	public SnowflakeIdWorker getSnowflakeIdWorker(){
		return new SnowflakeIdWorker(Config.getBalancedWorkerId(),
				Config.getBalancedDataCenterId());
	}
	
}
