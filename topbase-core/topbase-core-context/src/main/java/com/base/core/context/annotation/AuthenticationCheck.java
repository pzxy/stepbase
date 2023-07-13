package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 后台管理
 *
 * @author start
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationCheck {
	
	/**
	 * 是否授权校验
	 * @return
	 */
	boolean value() default false;
	
}
