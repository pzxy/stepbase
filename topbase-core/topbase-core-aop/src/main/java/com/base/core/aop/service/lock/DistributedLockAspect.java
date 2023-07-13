package com.base.core.aop.service.lock;

import java.lang.reflect.Method;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.base.core.aop.service.AbstractAopAspect;
import com.base.core.context.annotation.DistributeLock;

/**
 * @author start 
 */
@Order(900)
@Aspect
@Component
public class DistributedLockAspect extends AbstractAopAspect {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Autowired
    private IDistributedLock distributedLock;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.base.core.context.annotation.DistributeLock)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistributeLock action = method.getAnnotation(DistributeLock.class);
        String key = getKey(action.key(),action.value(),method,pjp.getArgs());
    	String value = UUID.randomUUID().toString();
        boolean lock = distributedLock.lock(key,value, action.keepMills(), action.retryCount(), action.sleepMills());
        if (!lock) {
        	if(action.timeOutException()) {
                throw new DistributedLockException("get lock : " + key+" failed");
        	}else {
        		if(LOGGER.isInfoEnabled()) {
            		LOGGER.info("get lock : {} failed",key);
            	}
                return null;
        	}
        }
        //得到锁,执行方法，释放锁
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("get lock : {} success",key);
    	}
    	try {
        	return pjp.proceed();
    	}finally {
    		boolean releaseResult = distributedLock.releaseLock(key,value);
        	if(LOGGER.isDebugEnabled()) {
        		LOGGER.debug("release lock :{} , {} ",key, (releaseResult ? " success" : " failed"));
        	}
    	}
    }

}

