package com.base.core.mvc.web;

import org.slf4j.MDC;

import com.base.core.head.constants.MdcConstants;
import com.gitee.magic.core.utils.StringUtils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 
 * @author start
 *
 */
public class LogRequestIdConfig extends ClassicConverter {
	
    @Override
    public String convert(ILoggingEvent event) {
    	String requestId=MDC.get(MdcConstants.REQUESTID);
    	if(StringUtils.isEmpty(requestId)) {
    		requestId=StringUtils.random();
    		MDC.put(MdcConstants.REQUESTID, requestId);
    	}
    	return requestId;
    }
    
}
