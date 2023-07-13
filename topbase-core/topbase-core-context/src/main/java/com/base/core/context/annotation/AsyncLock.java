package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步锁
 * @author start
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AsyncLock {

	/**
     * 锁的资源，key。
     * 支持spring El表达式
     */
    String key();
	
    /**
     * 锁的资源，value。
     * 支持spring El表达式
     */
    String value() default "'asynclock'";

    /**
     * 超时时间(秒)
     *
     * @return
     */
    int timeout() default 120;

}

