package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求调用次数限制一段时间内最大调用次数推荐注解于Service层
 *
 * @author Start
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
	
	/**
	 * 一定时间内只允许调用
	 * @return
	 */
	int max() default 1;
	
	/**
     * 唯一，key。
     * 支持spring El表达式
     */
    String key();

    /**
     * 缓存的资源 value。 支持spring El表达式
     */
    String value() default "'requestlimit'";

    /**
     * 间隔时间(秒)
     *
     * @return
     */
    int interval() default 120;

}
