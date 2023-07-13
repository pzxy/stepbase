package com.base.service.blockchain.synchash.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImplV1Ext;
import com.base.service.blockchain.synchash.dao.SyncHashDao;
import com.base.service.blockchain.synchash.entity.SyncHashDO;

/**
 * @author start
 */
@Repository("syncHashDao")
public class SyncHashDaoImpl extends SqlBaseDaoImplV1Ext<SyncHashDO,Long>implements SyncHashDao {

	public SyncHashDaoImpl() {
		super(SyncHashDO.class);
	}

}
