package com.base.service.blockchain.synchash.service;

import java.util.Date;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.blockchain.synchash.entity.SyncHashDO;
import com.base.service.blockchain.synchash.entity.SyncStatusEnum;

/**
 * @author start
 */
public interface SyncHashService extends SqlBaseService<SyncHashDO,Long> {

	/**
	 * 保存
	 * @param hash
	 * @param beanName
	 */
	void save(String hash,String beanName);

	/**
	 * 保存
	 * @param hash
	 * @param beanName
	 * @param isSync
	 */
	void save(String hash,String beanName,Boolean isSync);
	
	/**
	 * 根据hash删除
	 * @param hash
	 */
	void removeByHash(String hash);

	/**
	 * 更新HASH状态
	 * @param hash
	 * @param status
	 */
	void updateSyncStatus(String hash,SyncStatusEnum status);

	/**
	 * 定时任务同步未成功的Hash
	 * @param bean
	 * @param endDate
	 * @param size 每次处理的最大数量
	 */
	void handleUnsuccessed(String bean,Date endDate,int size);
	
}
