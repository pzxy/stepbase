package com.base.core.mvc.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import com.base.core.head.constants.MdcConstants;
import com.base.core.mvc.bigdata.TzBigData;
import com.gitee.magic.framework.base.constant.Config;

/**
 * 原始请求日志拦截
 * @author  start
 */
public class LogInterceptor {

	@Value("${base.log.on:true}")
	private Boolean on;
	@Value("${base.log.uri.exclude:}")
	private String[] uriExclude;
	
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    public void requestLog(HttpServletRequest request) {
    	//TODO:溯源上报大数据
    	String projectName=Config.getSystemName();
    	String topStr="top";
    	String filetStr="filet";
    	if(projectName.startsWith(topStr)||
    			projectName.startsWith(filetStr)) {
    		TzBigData send=new TzBigData();
        	send.send(request,Config.getSystemName(), Config.getActive());
    	}
    	if(!on) {
    		return;
    	}
    	String uri = request.getRequestURI();
    	List<String> uris=Arrays.asList(uriExclude);
    	//过滤不记录日志的URI
		if (uris.contains(uri)) {
			return;
		}
		//重新设置uri，/error 路径可在这里拦截
    	MDC.put(MdcConstants.URI, request.getRequestURI());
    	if (LOGGER.isInfoEnabled()) {
			LOGGER.info(LoggerRequestWrapper.getRequestLog(request));
		}
    }
    

}