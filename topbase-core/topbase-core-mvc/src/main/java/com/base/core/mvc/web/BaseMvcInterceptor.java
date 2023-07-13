package com.base.core.mvc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.base.core.context.annotation.AuthenticationCheck;
import com.base.core.context.annotation.LoginAuthorityCheck;
import com.base.core.context.annotation.OpenAuthorityCheck;
import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.annotation.SecretKeyAuthorityCheck;
import com.base.core.head.constants.CodeResVal;
import com.base.core.mvc.business.CommonBusiness;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * 基础拦截器
 * @author start
 *
 */
public class BaseMvcInterceptor implements HandlerInterceptor,MethodAuthCheck {

	@Autowired
	private LogInterceptor logInterceptor;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	logInterceptor.requestLog(request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (isMethodAuthCheck(handlerMethod)) {
            	//测试环境下不允许调用过期接口方法
                if(Config.isTestActive()) {
                    if(handlerMethod.hasMethodAnnotation(Deprecated.class)) {
                        throw new BusinessException(CodeResVal.CODE_10000);
                    }
                }
                MethodParameterValid.valid(request,handlerMethod);
                Http http = HttpHolder.getHttp(request);
                CommonBusiness.checkRequestContentType(http);
                if (handlerMethod.hasMethodAnnotation(OpenAuthorityCheck.class)) {
                	openAuthorityCheck(http,handlerMethod);
                }else if (handlerMethod.hasMethodAnnotation(LoginAuthorityCheck.class)) {
                	loginAuthorityCheck(http,handlerMethod);
                }else if (handlerMethod.hasMethodAnnotation(AuthenticationCheck.class)) {
                	authenticationCheck(http,handlerMethod);
                }else if (handlerMethod.hasMethodAnnotation(SecretKeyAuthorityCheck.class)) {
                	secretKeyAuthorityCheck(http,handlerMethod);
                }else if (handlerMethod.hasMethodAnnotation(RestfulCheck.class)) {
                	restfulCheck(http,handlerMethod);
                }else {
                	otherCheck(request,handlerMethod);
                }
            }else {
            	noCheck(request,handlerMethod);
            }
        }
        return true;
    }
    
    
    @Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
     * 开放平台
     * @param http
     * @param handlerMethod
     */
    public void loginAuthorityCheck(Http http,HandlerMethod handlerMethod) {
    	throw new ApplicationException("Override loginAuthorityCheck Method,Please");
    }
    
    /**
     * 前端用户
     * @param http
     * @param handlerMethod
     */
    public void openAuthorityCheck(Http http,HandlerMethod handlerMethod) {
    	throw new ApplicationException("Override openAuthorityCheck Method,Please");
    }
    
    /**
     * 后台管理
     * @param http
     * @param handlerMethod
     */
    public void authenticationCheck(Http http,HandlerMethod handlerMethod) {
    	throw new ApplicationException("Override authenticationCheck Method,Please");
    	
    }
    
    /**
     * 安全验证
     * @param http
     * @param handlerMethod
     */
    public void secretKeyAuthorityCheck(Http http,HandlerMethod handlerMethod) {
    	throw new ApplicationException("Override secretKeyCheck Method,Please");
    }
    
    /**
     * Rest处理
     * @param http
     * @param handlerMethod
     */
    public void restfulCheck(Http http,HandlerMethod handlerMethod) {
    	http.setAccessId("-");
        http.requestCheck(false);
    }

    /**
     * 其它检测处理
     * @param request
     * @param handlerMethod
     */
    public void otherCheck(HttpServletRequest request,HandlerMethod handlerMethod) {
    	throw new ApplicationException("Override otherCheck Method,Please");
    }
    
    /**
     * 无处理
     * @param request
     * @param handlerMethod
     */
    public void noCheck(HttpServletRequest request,HandlerMethod handlerMethod) {
    }

}
