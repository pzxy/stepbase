package com.base.core.context.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.base.core.context.mvc.NotException;
import com.base.core.head.constants.MdcConstants;
import com.gitee.magic.core.utils.StackTraceInfo;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.valid.ValidException;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.rest.BusinessRestException;
import com.gitee.magic.framework.base.spring.PropertyConfigurer;
import com.gitee.magic.framework.head.exception.BusinessAdviceException;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * 日志工具类
 *
 * @author start
 */
public class LoggerWrapper {

	protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerWrapper.class);

	public static Throwable getCause(Throwable e) {
		if(e.getCause()==null) {
			return e;
		}
		Throwable cause=e.getCause();
		while(cause.getCause()!=null) {
			cause=cause.getCause();
		}
		return cause;
	}
	
	public static void print(Throwable e) {
		print(e, "");
	}
	
	public static void print(Throwable e, String append) {
		print(e,append,true);
	}
	
	public static void print(Throwable e,Boolean isSend) {
		print(e, "",isSend);
	}
	
	/**
	 * 日志打印
	 * @param e 子异常
	 * @param append 异常追加日志
	 * @param isSend true:只发送符合要求的 false:不发送 
	 */
	public static void print(Throwable e, String append,Boolean isSend) {
		if (!checkPrintLogger(e)) {
			return;
		}
		StringBuilder logBuffer = new StringBuilder();
		logBuffer.append(StackTraceInfo.getStackTrace(e));
		logBuffer.append("\r\n");
		String requestId=MDC.get(MdcConstants.REQUESTID);
		logBuffer.append("[requestId]\r\n\t" + requestId);
		if (!StringUtils.isEmpty(append)) {
			logBuffer.append("\r\n");
			logBuffer.append("[log]\r\n\t" + append);
		}
		//获取源头的cause
		Throwable lastCause=LoggerWrapper.getCause(e);
		MDC.put(MdcConstants.EXCEPTION, lastCause.getClass().getName());
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(logBuffer.toString());
		}
		if (!isSend) {
			return;
		}
		if(!checkSendLogger(e)||!checkSendLogger(lastCause)) {
			return;
		}
		StringBuilder title = new StringBuilder();
		title.append("Error>>");
		title.append(Config.getFullName());
		title.append("[" + Config.getBalancedDataCenterId());
		title.append(",");
		title.append(Config.getBalancedWorkerId() + "]");
		try {
			title.append("(" + InetAddress.getLocalHost().getHostAddress() + ")");
		} catch (UnknownHostException e1) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e1.getMessage());
			}
		}
		ProxyService.send(title.toString(), logBuffer.toString());
	}
	
	public static boolean checkPrintLogger(Throwable e) {
		if (e.getClass().equals(ValidException.class)) {
			return false;
		}
		if(e.getClass().equals(BusinessException.class)) {
			return false;
		}
		if(e.getClass().equals(BusinessRestException.class)) {
			return false;
		}
		return true;
	}

	public static boolean checkSendLogger(Throwable e) {
		Class<?> exeCls = e.getClass();
		if (exeCls.equals(NotException.class)) {
			return false;
		}
		if (exeCls.equals(BusinessAdviceException.class)) {
			return false;
		}
		//过滤不推送的异常
		String ignoreExceptions=PropertyConfigurer.getString("base.ignore.exceptions");
		if(!StringUtils.isEmpty(ignoreExceptions)) {
			String[] excList=ignoreExceptions.split(",");
			for(String s:excList) {
				if (s.equals(exeCls.getName())) {
					return false;
				}
			}
		}
		// 远程主机强迫关闭了一个现有的连接
		String clientAbortException="org.apache.catalina.connector.ClientAbortException";
		if (clientAbortException.equals(exeCls.getName())) {
			return false;
		}
		return true;
	}

}
