package com.base.core.aop.service.lock;

import com.gitee.magic.core.exception.ApplicationException;

/**
 * 分布式锁异常
 * @author Start
 */
public class DistributedLockException extends ApplicationException {

	private static final long serialVersionUID = 1L;
	
	public DistributedLockException(String message) {
		super(message);
	}
	
}