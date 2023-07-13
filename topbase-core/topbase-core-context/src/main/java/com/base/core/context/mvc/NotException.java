package com.base.core.context.mvc;

import com.gitee.magic.core.exception.ApplicationException;

/**
 * 无需处理的异常
 * @author start
 *
 */
public class NotException extends ApplicationException {
	
	private static final long serialVersionUID = 1L;
	
	public NotException(Throwable e) {
		super(e);
	}
	
}
