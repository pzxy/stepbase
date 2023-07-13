package com.base.core.aop.service.submit;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

import com.base.core.aop.service.cache.RedisCacheAspect;
import com.base.core.aop.service.lock.IDistributedLock;
import com.base.core.context.annotation.RepeatSubmit;
import com.base.core.context.mvc.Constants;
import com.gitee.magic.core.utils.codec.Md5;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
@Order(-1000)
@Aspect
@Component
public class RepeatSubmitAspect {

	private static final int S=1000;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

	@Autowired
	private HttpServletRequest request;
    @Autowired
    private IDistributedLock distributedLock;

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.base.core.context.annotation.RepeatSubmit)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RepeatSubmit action = method.getAnnotation(RepeatSubmit.class);
        Http http=HttpHolder.getHttp(request);
        String param0="repeatsubmit:";
        String param1 = Md5.md5(
                http.getAccessId() +
                http.getMethod()+
                http.getRequest().getRequestURI()+
                http.getRequest().getQueryString()+
                http.getRequestBodyEncode());
        String key = Constants.getKey(param0,param1);
    	String value = UUID.randomUUID().toString();
    	if(!distributedLock.lock(key ,value , action.interval()*S,0,0)) {
    		throw new BusinessException(BaseCode.CODE_429);
    	}
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("repeat submit {} ",key);
    	}
    	try {
    		return pjp.proceed();
    	}finally {
//    		try {
            	//解除请求限制
    			if(!action.expired()) {
    				distributedLock.releaseLock(key,value);
            	}
//            } catch (Exception e) {
//                LoggerUtils.printErrorLog(StackTraceInfo.getTraceInfo(), e);
//            }
    	}
    }

}

