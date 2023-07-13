package com.base.core.aop.service.submit;

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
import com.base.core.aop.service.cache.RedisCacheAspect;
import com.base.core.context.annotation.RequestLimit;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
@Order(-900)
@Aspect
@Component
public class RequestLimitAspect extends AbstractAopAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

	@Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.base.core.context.annotation.RequestLimit)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RequestLimit action = method.getAnnotation(RequestLimit.class);
        String key = getKey(action.key(),action.value(),method,pjp.getArgs());
        Integer requestCount=1;
        if(redisTemplate.hasKey(key)) {
        	requestCount=redisTemplate.opsForValue().get(key);
        	if(requestCount==null) {
        		requestCount=1;
        	}
        }
        if(requestCount>action.max()) {
    		throw new BusinessException(BaseCode.CODE_429);
        }
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("request limit {} ",key);
    	}
    	Object result=pjp.proceed();
		//更新计数值
    	redisTemplate.opsForValue().set(key, requestCount+1);
    	if(requestCount==1) {
    		//初始设置有效期
        	redisTemplate.expire(key, action.interval(),TimeUnit.SECONDS);
    	}
    	return result;
    }

}

