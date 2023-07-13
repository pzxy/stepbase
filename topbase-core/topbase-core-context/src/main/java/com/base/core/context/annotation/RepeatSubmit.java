package com.base.core.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防连续提交,推荐注解于Controller层插入更新数据的接口方法上
 *
 * @author Start
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 间隔时间(秒)
     *
     * @return
     */
    int interval() default 120;

    /**
     * 到期失效
     *
     * @return
     */
    boolean expired() default false;

}
