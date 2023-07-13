package com.base.core.mvc.web;

import org.springframework.web.method.HandlerMethod;

import com.base.core.context.annotation.AuthenticationCheck;
import com.base.core.context.annotation.LoginAuthorityCheck;
import com.base.core.context.annotation.OpenAuthorityCheck;
import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.annotation.SecretKeyAuthorityCheck;

/**
 * @author  start
 */
public interface MethodAuthCheck {

	/**
	 * 检测方法是否为授权,如有新授权方法请重写该方法
	 * @param handlerMethod
	 * @return
	 */
	default boolean isMethodAuthCheck(HandlerMethod handlerMethod) {
    	return handlerMethod.hasMethodAnnotation(SecretKeyAuthorityCheck.class)||
    			handlerMethod.hasMethodAnnotation(LoginAuthorityCheck.class)||
    			handlerMethod.hasMethodAnnotation(AuthenticationCheck.class)||
    			handlerMethod.hasMethodAnnotation(OpenAuthorityCheck.class)||
    			handlerMethod.hasMethodAnnotation(RestfulCheck.class);
	}
	
}
