package com.base.service.blockchain.synchash.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.core.context.utils.LoggerWrapper;
import com.base.core.context.utils.SpringUtils;
import com.base.core.framework.sql.service.impl.SqlBaseServiceImplV1Ext;
import com.base.service.blockchain.synchash.dao.SyncHashDao;
import com.base.service.blockchain.synchash.entity.SyncHashDO;
import com.base.service.blockchain.synchash.entity.SyncStatusEnum;
import com.base.service.blockchain.synchash.service.SyncHashService;
import com.gitee.magic.framework.base.context.Cache;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start
 */
@Service("syncHashService")
public class SyncHashServiceImpl extends SqlBaseServiceImplV1Ext<SyncHashDO, Long> implements SyncHashService {
	
	private final SyncHashDao syncHashDao;

	public SyncHashServiceImpl(@Qualifier("syncHashDao") SyncHashDao syncHashDao) {
		super(syncHashDao);
		this.syncHashDao = syncHashDao;
	}

	@Override
	public void save(String hash, String beanName) {
		save(hash,beanName,false);
	}

	@Override
	public void save(String hash, String beanName,Boolean isSync) {
		SyncHashDO sync=new SyncHashDO();
		sync.setHash(hash);
		sync.setBean(beanName);
		sync.setSyncStatus(SyncStatusEnum.INIT);
		save(sync);
		if(isSync) {
			SpringUtils.currentProxy(this).syncHashStatus(sync);
		}
	}
	
	@Override
	public void removeByHash(String hash) {
		remove("hash",hash);
	}
	
	@Override
	public void updateSyncStatus(String hash, SyncStatusEnum status) {
		UpdateWrapper wrapper = new UpdateWrapper();
		wrapper.eq("hash", hash).eq("syncStatus", String.valueOf(SyncStatusEnum.INIT))
		.set("syncStatus", String.valueOf(status));
		syncHashDao.executeUpdate(wrapper);
	}

	@Override
	public void handleUnsuccessed(String bean,Date endDate,int size) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("bean", bean).eq("syncStatus", String.valueOf(SyncStatusEnum.INIT));
		wrapper.le("created_date", endDate);
		wrapper.orderByAsc("id");
		wrapper.last("limit 1,"+size);
		List<SyncHashDO> result = syncHashDao.queryForList(wrapper);
		for (SyncHashDO sync : result) {
			try {
				SpringUtils.currentProxy(this).syncHashStatus(sync);
			}catch(Exception e) {
				LoggerWrapper.print(e,sync.getHash());
			}
		}
	}
    
    @Transactional(rollbackFor = Exception.class)
    public boolean syncHashStatus(SyncHashDO sync) {
        SyncHashTask task = (SyncHashTask) SpringUtils.getBean(sync.getBean());
        Cache cache = new Cache();
        if (!task.syncBeforeHandle(sync, cache)) {
            return false;
        }
        if (task.syncVerificationHash(sync, cache)) {
            task.syncHashSuccess(sync, cache);
            updateSyncStatus(sync.getHash(), SyncStatusEnum.SUCCESS);
            return true;
        } else {
            task.syncHashFail(sync, cache);
            updateSyncStatus(sync.getHash(), SyncStatusEnum.FAIL);
            return false;
        }
    }

}
