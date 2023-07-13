package com.base.core.context.mvc;

import com.gitee.magic.core.json.JsonObject;

/**
 * @author start
 */
public interface RequestLog {

	/**
	 * 保存日志
	 * @param accessId
	 * @param name
	 * @param uri
	 * @param method
	 * @param queryString
	 * @param requestHeader
	 * @param requestBody
	 * @param responseBody
	 * @param requestId
	 * @param requestIp
	 */
	void saveLog(
			String accessId, 
			String name, 
			String uri, 
			String method, 
			String queryString,
			JsonObject requestHeader,
			String requestBody, 
			String responseBody,
			String requestId,
			String requestIp);
	
}
