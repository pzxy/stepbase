package com.base.core.mvc.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.base.core.mvc.web.httpmessage.JsonHttpMessageConverter;
import com.base.core.mvc.web.httpmessage.SerializeHttpMessageConverter;
import com.base.core.mvc.web.httpmessage.TextHttpMessageConverter;

/**
 * 默认配置可重写
 * @author start
 *
 */
public class DefaultWebMvcConfiguration implements WebMvcConfigurer {
	
	@Bean
	public LogInterceptor logInterceptor() {
		return new LogInterceptor();
	}
	
	@Bean
	public ExceptionController exceptionController() {
		return new ExceptionController();
	}

	@Bean
	public RequestParamArgumentResolver requestParamArgumentResolver() {
		return new RequestParamArgumentResolver();
	}
	
	@Bean
	public RequestParamArgumentValidResolver requestParamArgumentValidResolver() {
		return new RequestParamArgumentValidResolver();
	}

	@Order(100)
	@Bean
	public JsonHttpMessageConverter jsonHttpMessageConverter() {
		return new JsonHttpMessageConverter();
	}

	@Order(110)
	@Bean
	public TextHttpMessageConverter textHttpMessageConverter() {
		return new TextHttpMessageConverter();
	}

	@Order(120)
	@Bean
	public SerializeHttpMessageConverter serializeHttpMessageConverter() {
		return new SerializeHttpMessageConverter();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(requestParamArgumentResolver());
		argumentResolvers.add(requestParamArgumentValidResolver());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jsonHttpMessageConverter());
		converters.add(serializeHttpMessageConverter());
		converters.add(textHttpMessageConverter());
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("index.html").addResourceLocations("classpath:/static/doc/");
        registry.addResourceHandler("static/**").addResourceLocations("classpath:/static/");
    }

}
