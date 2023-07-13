package com.base.core.context.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.valid.ValidException;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.rest.BusinessRestException;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * 日志工具类
 *
 * @author start
 */
@Deprecated
public class LoggerUtils {

	protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtils.class);

	public static void printErrorLog(Http http, Throwable e) {
		printErrorLog(http, null, e);
	}

	public static void printErrorLog(Http http, String traceInfo, Throwable e) {
		StringBuilder errorLog = new StringBuilder();
		if (http != null) {
			errorLog.append("[RequestId]\r\n\t\t" + http.getRequestId() + ";");
			errorLog.append("\r\n\t[RequestURL]\r\n\t\t" + http.getRequest().getRequestURL() + ";");
			errorLog.append("\r\n\t[RequestURI_Method]\r\n\t\t" + http.getRequest().getRequestURI() + "_"
					+ http.getRequest().getMethod() + ";");
			String queryString = http.getRequest().getQueryString();
			if (!StringUtils.isEmpty(queryString)) {
				errorLog.append("\r\n\t[QueryString]\r\n\t\t" + queryString + ";");
			}
			if (!http.isFileUpload()) {
				String body = http.getRequestBodyEncode();
				if (!StringUtils.isEmpty(body)) {
					errorLog.append("\r\n\t[RequestBody]\r\n\t\t" + body + ";");
				}
			}
		}
		printErrorLog(traceInfo, e, errorLog.toString());
	}

	public static void printErrorLog(Throwable e) {
		printErrorLog(null, e, null);
	}

	public static void printErrorLog(String traceInfo, Throwable e) {
		printErrorLog(traceInfo, e, null);
	}

	/**
	 * 打印异常日志
	 *
	 * @param traceInfo
	 * @param e
	 */
	public static void printErrorLog(String traceInfo, Throwable e, String append) {
		if (e == null) {
			return;
		}
		if (e.getClass().equals(ValidException.class) || e.getClass().equals(BusinessException.class)
				|| e.getClass().equals(BusinessRestException.class)) {
			// 过滤ValidException、BusinessException、RestBusinessException不处理
			return;
		}
		printAllErrorLog(traceInfo,e,append);
	}
	
	public static void printAllErrorLog(Throwable e) {
		printAllErrorLog(null,e,null);
	}

	/**
	 * 打印所有异常信息
	 * @param traceInfo
	 * @param e
	 * @param append
	 */
	public static void printAllErrorLog(String traceInfo, Throwable e, String append) {
		// 异常信息
		StringBuilder logBuffer = new StringBuilder();
		StringWriter sw = new StringWriter();
		try(PrintWriter pw = new PrintWriter(sw)){
			e.printStackTrace(pw);
		}
		logBuffer.append(sw);
		if (!StringUtils.isEmpty(traceInfo)) {
			logBuffer.append("\r\n[StackTrace]\r\n\t" + traceInfo + ";");
		}
		if (!StringUtils.isEmpty(append)) {
			logBuffer.append("\r\n[Append]\r\n\t" + append);
		}
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(logBuffer.toString());
		}
		if (!checkThrowableContinue(e)) {
			return;
		}
		// 发送给管理员
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

	public static boolean checkThrowableContinue(Throwable e) {
		Class<?> exeCls = e.getClass();
//		if (exeCls.equals(ConnectException.class)) {
//			return false;
//		}
//		if (exeCls.equals(SocketTimeoutException.class)) {
//			return false;
//		}
		// 远程主机强迫关闭了一个现有的连接
		String clientAbortException="org.apache.catalina.connector.ClientAbortException";
		if (clientAbortException.equals(exeCls.getName())) {
			return false;
		}
		return true;
	}

}
