package com.base.core.mvc.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import com.base.core.context.mvc.NotException;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.valid.ValidException;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.rest.BusinessRestException;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessAdviceException;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * 全局异常处理
 * @author user
 *
 */
@RestControllerAdvice
public class ExceptionController extends BaseExceptionController {
	
    /////////////////////////////////////////以下为异常捕获处理----->>
    
    @ExceptionHandler(NotException.class)
    public void handlerNotException(HandlerMethod handler, NotException e) {
    	LoggerRequestWrapper.print(request,e,false);
    }

    @ExceptionHandler(ValidException.class)
    public Object handlerVerifyException(HandlerMethod handler, ValidException e) {
        return response(messageParse(e.getMessage()), handler, e);
    }

    @ExceptionHandler(BusinessException.class)
    public Object handlerBusinessException(HandlerMethod handler, BusinessException e) {
    	Message msg=messageParse(e.getMessage());
    	if(e.getArgs()==null) {
    		msg.setMessage(msg.getMessage());
    	}else {
    		msg.setMessage(String.format(msg.getMessage(), e.getArgs()));
    	}
        return response(msg, handler, e);
    }

    @ExceptionHandler(BusinessAdviceException.class)
    public Object handlerBusinessAdviceException(HandlerMethod handler, BusinessAdviceException e) {
    	Message msg=messageParse(e.getMessage());
    	if(e.getArgs()==null) {
    		msg.setMessage(msg.getMessage());
    	}else {
    		msg.setMessage(String.format(msg.getMessage(), e.getArgs()));
    	}
        return response(msg, handler, e);
    }

    @ExceptionHandler(BusinessRestException.class)
    public Object handlerBusinessRestException(HandlerMethod handler, BusinessRestException e) {
    	Message message=new Message(e.getCode(),e.getMsg());
        return response(e.getName(), message, handler, e);
    }

    @ExceptionHandler(ApplicationException.class)
    public Object handlerApplicationException(HandlerMethod handler, ApplicationException e) {
        return response(messageParse(BaseCode.CODE_500), handler, e);
    }

    @ExceptionHandler(Exception.class)
    public Object handlerException(HandlerMethod handler, Exception e) {
        return response(messageParse(BaseCode.CODE_500), handler, e);
    }

}
