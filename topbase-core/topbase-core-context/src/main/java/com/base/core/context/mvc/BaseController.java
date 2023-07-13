package com.base.core.context.mvc;

import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.result.BaseResponse;
import com.gitee.magic.framework.base.result.PageResponse;
import com.gitee.magic.framework.base.result.QueryResult;
import com.gitee.magic.framework.base.result.ResultResponse;
import com.gitee.magic.framework.head.constants.BaseCode;

/**
 * @author start
 */
public class BaseController extends AbstractController {

	/////////////////////////////////////////////////////////////

	/**
	 * 成功 @RestController中调用
	 */
	protected BaseResponse response() {
		return response(getHttp(), BaseCode.CODE_200, Config.getFullName());
	}

	/**
	 * 响应信息 @RestController or @RestControllerAdvice中调用
	 */
	protected BaseResponse response(Http http, String code, String name) {
		Message message = messageParse(code);
		try {
			return new BaseResponse(message, http.getRequestId(), name);
		} finally {
			saveLog(http, message, name);
		}
	}

	//////////////////// ResultResponse

	/**
	 * 单页，列表 @RestController中调用
	 */
	protected <T> ResultResponse<T> response(T result) {
		return response(getHttp(), result, Config.getFullName());
	}

	/**
	 * 单页，列表 @RestController中调用
	 */
	protected <T> ResultResponse<T> response(Http http, T result, String name) {
		Message message = messageParse(result != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			ResultResponse<T> response = new ResultResponse<T>(message, http.getRequestId(), name);
			response.setResult(result);
			return response;
		} finally {
			saveLog(http, message, name);
		}
	}

	//////////////////// PageResponse

	/**
	 * 分页，PageInfo @RestController中调用
	 */
	protected <T> PageResponse<T> response(QueryResult<T> query) {
		return response(getHttp(), query, Config.getFullName());
	}

	/**
	 * 分页，PageInfo @RestController中调用
	 */
	protected <T> PageResponse<T> response(Http http, QueryResult<T> query, String name) {
		Message message = messageParse(query != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			PageResponse<T> response = new PageResponse<T>(message, http.getRequestId(), name);
			if (query != null) {
				response.setResult(query.getResult());
				response.setPageInfo(query.getPageInfo());
			}
			return response;
		} finally {
			saveLog(http, message, name);
		}
	}

}
