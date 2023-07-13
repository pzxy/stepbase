package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 客户端(APP、H5、小程序、桌面)等应用API-(用户登录后)
 *
 * @author start
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAuthorityCheck {
	
}
