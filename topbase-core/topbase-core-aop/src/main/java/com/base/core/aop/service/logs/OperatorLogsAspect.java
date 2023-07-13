package com.base.core.aop.service.logs;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.base.core.context.utils.ProxyService;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.CacheContext;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.head.constants.BaseCode;

/**
 * @author start 
 */
@Order
@Aspect
@Component
public class OperatorLogsAspect {
	
	@Autowired
	protected HttpServletRequest request;

	@Pointcut("@annotation(com.base.core.context.annotation.OperatorLogs)")
	private void lockPoint() {
	}

	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		ProxyService.saveLog(HttpHolder.getHttp(request), Message.parse(BaseCode.CODE_200), Config.getFullName());
		CacheContext.inactiveUser();
		return result;
	}

}
