package com.base.common.mvc;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.base.core.mvc.web.DefaultWebMvcConfiguration;
import com.base.core.mvc.web.RequserAgainFilter;

/**
 * 默认扩展配置可重写
 * @author start
 *
 */
public class DefaultWebMvcExtConfiguration extends DefaultWebMvcConfiguration {
	
	@Bean
	public ExceptionExtController exceptionExtController() {
		return new ExceptionExtController();
	}
	
	@Bean
	public FilterRegistrationBean<RequserAgainFilter> requserAgainFilter() {
	    FilterRegistrationBean<RequserAgainFilter> filter = new FilterRegistrationBean<>();
	    filter.addUrlPatterns("/*");
	    filter.setFilter(new RequserAgainFilter());
	    return filter;
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedHeaders("*")
		.allowedMethods("*")
		.allowedOrigins("*")
		.maxAge(1800);
	}

}
