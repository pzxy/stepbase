package com.base.core.mvc.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.base.core.head.constants.MdcConstants;
import com.base.core.mvc.business.CommonBusiness;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.context.CacheContext;
import com.gitee.magic.framework.base.context.HttpHolder;
import com.gitee.magic.framework.base.utils.IpUtils;

/**
 * @author  start
 */
public class RequserAgainFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		CacheContext.activeUser();
    	//requestId用于链路监控先取header中的值
    	String requestId=request.getHeader(HttpHolder.REQUESTID);
    	if(StringUtils.isEmpty(requestId)) {
    		requestId=HttpHolder.getRequestId(request);
    	}else {
    		request.setAttribute(HttpHolder.REQUESTID, requestId);
    	}
    	MDC.put(MdcConstants.REQUESTID, requestId);
    	MDC.put(MdcConstants.URI, request.getRequestURI());
    	MDC.put(MdcConstants.METHOD, request.getMethod());
    	MDC.put(MdcConstants.WORKID, String.valueOf(Config.getBalancedWorkerId()));
    	MDC.put(MdcConstants.DATACENTERID, String.valueOf(Config.getBalancedDataCenterId()));
    	MDC.put(MdcConstants.REQUESTIP, IpUtils.getRequestIp(request));
    	try {
    		MDC.put(MdcConstants.LOCALIP, IpUtils.getLocalIpByNetcard());
    	}catch(Exception e) {
    		// /etc/hosts配置问题会导致获取异常
    	}
    	try {
    		String contentType=request.getContentType();
            if (CommonBusiness.isDefaultContentType(contentType)) {
                ServletRequest requestWrapper = new BodyStringReaderHttpServletRequestWrapper(request);
                filterChain.doFilter(requestWrapper, response);
            } else {
                filterChain.doFilter(request, response);
            }
    	}finally {
    		CacheContext.inactiveUser();
    	}
    }
    
    @Override
    public void destroy() {
        MDC.clear();
    }

}