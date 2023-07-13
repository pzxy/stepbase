package com.base.core.framework.sql.service.impl;

import java.io.Serializable;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.BaseV2Ext;

/**
 * @author start
 */
public class SqlBaseServiceImplV2Ext<T extends BaseV2Ext, PK extends Serializable> extends SqlBaseServiceImplV1Ext<T,PK> {
	
	@SuppressWarnings("unused")
	private final SqlBaseDao<T,PK> baseDao;
	
	public SqlBaseServiceImplV2Ext(SqlBaseDao<T,PK> baseDao){
		super(baseDao);
		this.baseDao=baseDao;
	}
	
//	@Override
//	public int remove(List<PK> ids) {
//		return set(ids, "deleted", baseDao.getSnowflakeIdWorkerNextId());
//	}
//
//	@Override
//	public int remove(String fieldName,Object value) {
//		UpdateWrapper wrapper=new UpdateWrapper();
//		wrapper.eq(fieldName, value).set("deleted", baseDao.getSnowflakeIdWorkerNextId());
//		return baseDao.executeUpdate(wrapper);
//	}
	
}