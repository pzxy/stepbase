package com.base.service.blockchain.synchash.service.impl;

import com.base.service.blockchain.synchash.entity.SyncHashDO;
import com.gitee.magic.framework.base.context.Cache;

/**
 * @author start
 */
public interface SyncHashTask {

	/**
	 * 前置处理返回true继续执行
	 * @param sync
	 * @param cache
	 * @return
	 */
	default boolean syncBeforeHandle(SyncHashDO sync,Cache cache) {
		return true;
	}

	/**
	 * 校验哈希
	 * @param sync
	 * @param cache
	 * @return
	 */
	boolean syncVerificationHash(SyncHashDO sync,Cache cache);

	/**
	 * 成功后执行
	 * @param sync
	 * @param cache
	 */
	void syncHashSuccess(SyncHashDO sync,Cache cache);

	/**
	 * 失败后执行
	 * @param sync
	 * @param cache
	 */
	default void syncHashFail(SyncHashDO sync,Cache cache) {
		
	}
	
}
