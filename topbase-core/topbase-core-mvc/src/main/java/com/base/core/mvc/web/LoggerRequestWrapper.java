package com.base.core.mvc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import com.base.core.context.utils.LoggerWrapper;
import com.base.core.mvc.business.CommonBusiness;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.utils.IpUtils;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * 日志工具类
 *
 * @author start
 */
public class LoggerRequestWrapper extends LoggerWrapper {

	public static void print(HttpServletRequest request, Throwable e) {
		print(request,e,true);
	}

	public static void print(HttpServletRequest request, Throwable e,Boolean isSend) {
		String appendLog=getRequestLog(request);
		print(e, appendLog.toString(),isSend);
	}
	
	public static String getRequestLog(HttpServletRequest request) {
    	String uri = request.getRequestURI();
		List<String> requestLogs = new ArrayList<>();
		requestLogs.add("uri:" + uri);
		requestLogs.add("method:" + request.getMethod());
		requestLogs.add("remoteAddr:" + IpUtils.getRequestIp(request));
		requestLogs.add(getQuery(request));
		requestLogs.add(getHeader(request));
		if ("post".equalsIgnoreCase(request.getMethod()) 
				|| "put".equalsIgnoreCase(request.getMethod())
				|| "delete".equalsIgnoreCase(request.getMethod())) {
			if (CommonBusiness.isDefaultContentType(request.getContentType())) {
				if(request.getContentLength()>0) {
					try {
						String body = new String(IoUtils.inputStreamConvertBytes(request.getInputStream(), -1));
						requestLogs.add("body:" + body);
					} catch (IOException e) {
						throw new ApplicationException(e);
					}
				}
			}
		}
		return "request "+StringUtils.listToString(requestLogs, ";");
	}
	
	public static String getQuery(HttpServletRequest request) {
		Map<String,String> data = new HashMap<>(5);
		Map<String, String[]> query = request.getParameterMap();
		if (!CollectionUtils.isEmpty(query)) {
			for (String name : query.keySet()) {
				List<String> values = new ArrayList<>();
				for (String v : query.get(name)) {
					values.add(v);
				}
				data.put(name, StringUtils.listToString(values));
			}
		}
		return "query:" + new JsonObject(data);
	}

	public static String getHeader(HttpServletRequest request) {
		Map<String,String> data = new HashMap<>(5);
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			List<String> headers = new ArrayList<>();
			Enumeration<String> headerValues = request.getHeaders(name);
			while (headerValues.hasMoreElements()) {
				headers.add(headerValues.nextElement());
			}
			data.put(name, StringUtils.listToString(headers));
		}
		return "header:" + new JsonObject(data);
	}

}
