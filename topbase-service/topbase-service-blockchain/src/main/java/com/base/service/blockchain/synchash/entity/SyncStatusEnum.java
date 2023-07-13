package com.base.service.blockchain.synchash.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author start
 */
public enum SyncStatusEnum {
	/**
	 * 初始
	 */
	@Schema(title="初始")
	INIT,
	/**
	 * 成功
	 */
	@Schema(title="成功")
	SUCCESS,
	/**
	 * 失败
	 */
	@Schema(title="失败")
	FAIL
}
