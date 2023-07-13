package com.base.core.aop.service.lock;

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
import com.base.core.context.annotation.AsyncLock;

/**
 * @author start 
 */
@Order(1001)
@Aspect
@Component
public class AsyncLockAspect extends AbstractAopAspect {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLockAspect.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.base.core.context.annotation.AsyncLock)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        AsyncLock action = method.getAnnotation(AsyncLock.class);
        String key = getKey(action.key(),action.value(),method,pjp.getArgs());
        if(redisTemplate.hasKey(key)) {
    		if(LOGGER.isDebugEnabled()) {
        		LOGGER.debug("AsyncLock key {} ",key);
        	}
    		return null;
    	}
        redisTemplate.opsForValue().set(key, 1, action.timeout(),TimeUnit.SECONDS);
        try {
        	return pjp.proceed();
        }finally {
        	redisTemplate.delete(key);
        }
    }

}

