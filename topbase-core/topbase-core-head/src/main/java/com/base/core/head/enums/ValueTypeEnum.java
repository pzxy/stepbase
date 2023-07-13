package com.base.core.head.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author start 
 */
public enum ValueTypeEnum {
	/**
	 *字符
	 */
	@Schema(title="字符")
	String,
	/**
	 *JSON
	 */
	@Schema(title="JSON")
	JSONObject,
	/**
	 * JSON数组
	 */
	@Schema(title="JSON数组")
	JSONArray
}
