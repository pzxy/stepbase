package com.base.core.aop.service.logs;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.base.core.context.utils.ProxyService;
import com.base.core.head.constants.MdcConstants;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.head.vo.BaseVO;

/**
 * @author start
 */
@Order(1)
@Aspect
@Component
public class ControllerLogAspect {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	protected HttpServletRequest request;

	@Value("${base.log.on:true}")
	private Boolean on;
	@Value("${base.log.uri.exclude:}")
	private String[] uriExclude;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controller() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }
    
    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    public void restControllerAdvice() {
    }

    @Around("restControllerAdvice() || restController() || controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    	if (!on) {
            return joinPoint.proceed();
        }
        String uri = request.getRequestURI();
    	List<String> uris=Arrays.asList(uriExclude);
		if (uris.contains(uri)) {
            return joinPoint.proceed();
		}
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        if(result!=null) {
            if(BaseVO.class.isAssignableFrom(result.getClass())) {
            	BaseVO vo=(BaseVO)result;
            	
            	Http http=HttpHolder.getHttp(request);
            	ProxyService.saveLog(http, new Message(vo.getCode(),vo.getMessage()),Config.getFullName());
            	
            	MDC.put(MdcConstants.CODE, String.valueOf(vo.getCode()));
            	String message = String.valueOf(ConverterEditorUtils.converterObject(result));
        		if(LOGGER.isInfoEnabled()) {
                	LOGGER.info("response {}, cost: {}", message, duration);
                }
        	}else {
        		if(LOGGER.isInfoEnabled()) {
                	LOGGER.info("response {}, cost: {}", result, duration);
                }
        	}
        }else {
        	if(LOGGER.isInfoEnabled()) {
            	LOGGER.info("response void, cost: {}", duration);
            }
        }
        return result;
    }

}

