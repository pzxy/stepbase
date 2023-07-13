package com.base.core.context.utils;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.core.context.message.MessageProxy;
import com.base.core.context.mvc.RequestLog;
import com.base.core.head.ao.MessageSysAO;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.spring.SpringContext;

/**
 * @author start 
 */
public class ProxyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class);

	public static void saveLog(Http http, Message message, String name) {
		// 只记录已生成请求编号的记录
		if (StringUtils.isEmpty(http.getRequestId())) {
			return;
		}
		JsonObject requestHeader = new JsonObject();
		Enumeration<String> headers = http.getRequest().getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			requestHeader.put(headerName, http.getHeaderValue(headerName));
		}
		String[] beans = SpringContext.getAppContext().getBeanNamesForType(RequestLog.class);
		for (String bean : beans) {
			RequestLog rquestLog = (RequestLog) SpringUtils.getBean(bean);
			rquestLog.saveLog(http.getAccessId(), name, http.getRequest().getMethod(),
					http.getRequest().getRequestURI(), http.getRequest().getQueryString(), requestHeader,
					http.getRequestBodyEncode(), String.valueOf(message.getCode() + "-" + message.getMessage()),
					http.getRequestId(), http.getRequestIp());
		}
	}

	/**
	 * 发送消息
	 */
	public static void send(String title, String content) {
		MessageSysAO dto = new MessageSysAO();
		dto.setTitle(title);
		dto.setContent(content);
		try {
			MessageProxy.send(dto);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage());
			}
		}
	}

}
