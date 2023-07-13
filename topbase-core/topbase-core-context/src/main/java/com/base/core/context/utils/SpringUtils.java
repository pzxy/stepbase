package com.base.core.context.utils;

import org.springframework.aop.framework.AopContext;

import com.gitee.magic.framework.base.spring.SpringContext;

/**
 * @author start 
 */
public class SpringUtils extends SpringContext {

    public static <T> T getBean(Class<T> prototype) {
        return SpringContext.getAppContext().getBean(prototype);
    }

    public static Object getBean(String name) {
        return SpringContext.getAppContext().getBean(name);
    }
    
    /**
	 * 获取当前AOP代理,开启配置
	 * <aop:aspectj-autoproxy expose-proxy="true"/>
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static <T>T currentProxy(T obj) {
		Object context=AopContext.currentProxy();
		if(context!=null) {
			return (T)context;
		}else {
			return obj;
		}
	}

}
