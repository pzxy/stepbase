package com.base.core.context.mvc;

import java.util.List;

import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.vo.BaseVO;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.ObjectVO;
import com.gitee.magic.framework.head.vo.PageVO;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public class BaseV1Controller extends AbstractController{

	/////////////////////////////////////////////////////////////

	/**
	 * 成功 @RestController中调用
	 */
	protected BaseVO response() {
		return response(getHttp(), messageParse(BaseCode.CODE_200), Config.getFullName());
	}

	/**
	 * 响应信息 @RestController or @RestControllerAdvice中调用
	 */
	protected BaseVO response(Http http, Message message, String name) {
		try {
			return new BaseVO(message.getCode(), message.getMessage(), http.getRequestId(), name);
		} finally {
			saveLog(http, message, name);
		}
	}

	//////////////////// PoJoVO

	/**
	 * 单页 @RestController中调用
	 */
	protected <T> ObjectVO<T> response(T data) {
		return response(getHttp(), data, Config.getFullName());
	}

	/**
	 * 单页 @RestController中调用
	 */
	protected <T> ObjectVO<T> response(Http http, T data, String name) {
		Message message = messageParse(data != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			ObjectVO<T> response = new ObjectVO<T>(message.getCode(), message.getMessage(), http.getRequestId(),
					name);
			response.setData(data);
			return response;
		} finally {
			saveLog(http, message, name);
		}
	}

	////////////////////ListVO

	/**
	 * 列表 @RestController中调用
	 */
	protected <T> ListVO<T> response(List<T> data) {
		return response(getHttp(), data, Config.getFullName());
	}

	/**
	 * 列表 @RestController中调用
	 */
	protected <T> ListVO<T> response(Http http, List<T> data, String name) {
		Message message = messageParse(data != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			ListVO<T> response = new ListVO<T>(message.getCode(), message.getMessage(), http.getRequestId(), name);
			response.setData(data);
			return response;
		} finally {
			saveLog(http, message, name);
		}
	}

	//////////////////// PageVO

	/**
	 * 分页 @RestController中调用
	 */
	protected <T> PageVO<T> response(QueryVO<T> query) {
		return response(getHttp(), query, Config.getFullName());
	}

	/**
	 * 分页 @RestController中调用
	 */
	protected <T> PageVO<T> response(Http http, QueryVO<T> query, String name) {
		Message message = messageParse(query != null ? BaseCode.CODE_200 : BaseCode.CODE_204);
		try {
			PageVO<T> response = new PageVO<T>(message.getCode(), message.getMessage(), http.getRequestId(), name);
			if (query != null) {
				response.setData(query.getData());
				response.setTotal(query.getTotal());
			}
			return response;
		} finally {
			saveLog(http, message, name);
		}
	}

}