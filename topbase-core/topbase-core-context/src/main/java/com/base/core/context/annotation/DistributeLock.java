package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 * @author start
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributeLock {

	/**
     * 锁的资源，key。
     * 支持spring El表达式
     */
    String key();
	
    /**
     * 锁的资源，value。
     * 支持spring El表达式
     */
    String value() default "'distributelock'";

    /**
     * 加锁时间,单位毫秒
     */
    long keepMills() default 10000;

    /**
     * 重试次数
     */
    int retryCount() default 5;

    /**
     * 重试的间隔时间,单位毫秒
     */
    long sleepMills() default 400;
    
    /**
     * 超时是否报异常
     * @return
     */
    boolean timeOutException() default true;

}

