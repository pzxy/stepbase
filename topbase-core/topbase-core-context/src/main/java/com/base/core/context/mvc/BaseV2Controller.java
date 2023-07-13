package com.base.core.context.mvc;

import java.util.List;

import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public class BaseV2Controller extends AbstractController{

	/////////////////////////////////////////////////////////////

	/**
	 * 成功 @RestController中调用
	 */
	protected void response() {
		response(getHttp(), BaseCode.CODE_200, Config.getFullName());
	}

	/**
	 * 响应信息 @RestController or @RestControllerAdvice中调用
	 */
	protected void response(Http http, String code, String name) {
		Message message = messageParse(code);
		saveLog(http, message, name);
	}

	//////////////////// PoJoVO

	/**
	 * 单页 @RestController中调用
	 */
	protected <T> T response(T data) {
		return response(getHttp(), data, Config.getFullName());
	}

	/**
	 * 单页 @RestController中调用
	 */
	protected <T> T response(Http http, T data, String name) {
		Message message = messageParse(data != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			return data;
		} finally {
			saveLog(http, message, name);
		}
	}

	////////////////////ListVO

	/**
	 * 列表 @RestController中调用
	 */
	protected <T> List<T> response(List<T> data) {
		return response(getHttp(), data, Config.getFullName());
	}

	/**
	 * 列表 @RestController中调用
	 */
	protected <T> List<T> response(Http http, List<T> data, String name) {
		Message message = messageParse(data != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			return data;
		} finally {
			saveLog(http, message, name);
		}
	}

	//////////////////// PageVO

	/**
	 * 分页 @RestController中调用
	 */
	protected <T> QueryVO<T> response(QueryVO<T> query) {
		return response(getHttp(), query, Config.getFullName());
	}

	/**
	 * 分页 @RestController中调用
	 */
	protected <T> QueryVO<T> response(Http http, QueryVO<T> query, String name) {
		Message message = messageParse(query != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			return query;
		} finally {
			saveLog(http, message, name);
		}
	}

}