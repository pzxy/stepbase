package com.base.core.aop.service.cache;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.base.core.aop.service.AbstractAopAspect;
import com.base.core.context.annotation.Cache;

/**
 * @author start 
 */
@Order(1000)
@Aspect
@Component
public class RedisCacheAspect extends AbstractAopAspect {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.base.core.context.annotation.Cache)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Cache action = method.getAnnotation(Cache.class);
        String key = getKey(action.key(),action.value(),method,pjp.getArgs());
        Object returnValue=null;
        if(!action.merge()) {
        	if(redisTemplate.hasKey(key)) {
        		if(LOGGER.isDebugEnabled()) {
            		LOGGER.debug("get cache key {} ",key);
            	}
        		returnValue=redisTemplate.opsForValue().get(key);
        		if(returnValue!=null) {
        			return returnValue;
        		}
        	}
        }
    	returnValue=pjp.proceed();
    	if(returnValue!=null) {
            if(action.expire()>0) {
            	redisTemplate.opsForValue().set(key, returnValue, action.expire(),TimeUnit.SECONDS);
            }else {
            	redisTemplate.opsForValue().set(key, returnValue);
            }
    	}
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("set cache key {} ",key);
    	}
    	return returnValue;
    }

}

