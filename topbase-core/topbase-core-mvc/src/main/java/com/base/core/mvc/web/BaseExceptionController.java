package com.base.core.mvc.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import com.base.core.context.mvc.BaseV1Controller;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.head.vo.BaseVO;

/**
 * @author start
 */
public class BaseExceptionController extends BaseV1Controller implements MethodAuthCheck {
	
	protected Object response(Message msg, HandlerMethod handlerMethod, Throwable e) {
        return response(msg, handlerMethod, e,true);
    }
	
	protected Object response(Message msg, HandlerMethod handlerMethod, Throwable e,Boolean isSend) {
        return response(Config.getFullName(),msg, handlerMethod, e,isSend);
    }
	
	protected Object response(String name, Message msg, HandlerMethod handlerMethod, Throwable e) {
		return response(name,msg,handlerMethod,e,true);
	}

    protected Object response(String name, Message msg, HandlerMethod handlerMethod, Throwable e,Boolean isSend) {
        try {
        	LoggerRequestWrapper.print(request,e,isSend);
        	if (isMethodAuthCheck(handlerMethod)) {
            	Http http = getHttp();
                return response(http, msg, name);
            } else {
            	String requestId=HttpHolder.getRequestId(request);
            	return new BaseVO(msg.getCode(), msg.getMessage(), requestId, name);
            }
        }finally {
            int sc=0;
            int ec=1000;
        	if(msg.getCode()>sc&&msg.getCode()<ec) {
            	response.setStatus(msg.getCode());
    	    }else {
    	    	if(BaseVO.class.isAssignableFrom(handlerMethod.getMethod().getReturnType())) {
    	    	    //TODO:返回200兼容老版本
    		    	response.setStatus(HttpStatus.OK.value());
    	    	}else {
    		    	response.setStatus(HttpStatus.BAD_REQUEST.value());
    	    	}
    	    }
        }
    }
    
}
