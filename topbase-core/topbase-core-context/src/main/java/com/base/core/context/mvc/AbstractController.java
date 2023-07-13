package com.base.core.context.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.base.core.context.utils.LocalizationMessage;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.context.HttpHolder;

/**
 * @author start
 */
public abstract class AbstractController {

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	/**
	 * 获取当前http请求对象
	 * 
	 * @return
	 */
	protected Http getHttp() {
		return HttpHolder.getHttp(request);
	}

	/**
	 * 获取Http中的缓存对象
	 * 
	 * @param prototype
	 * @return
	 */
	protected <T> T requestCache(Class<T> prototype) {
		return getHttp().getCache(prototype);
	}

	protected Message messageParse(String message) {
    	String language=request.getHeader("language");
		Message msg=Message.parse(message);
		return LocalizationMessage.getMessage(msg, language);
	}

	/**
     * 记录操作日志
     *
     * @param http
     * @param message
     */
    protected void saveLog(Http http, Message message,String name) {
//    	ProxyService.saveLog(http, message,name);
    }

}
