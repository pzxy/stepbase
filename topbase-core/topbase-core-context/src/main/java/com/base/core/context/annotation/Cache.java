package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存存储,推荐注解于Service层
 * @author start
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {

	/**
     * 锁的资源，key。
     * 支持spring El表达式
     */
    String key();

    /**
     * 缓存的资源 value。 支持spring El表达式
     */
    String value() default "'rediscache'";

    /**
     * 过期时间,单位秒,默认60秒
     */
    long expire() default 60;
    
    /**
     * 只覆盖现有缓存数据,不做读取缓存操作
     * @return
     */
    boolean merge() default false;

}

