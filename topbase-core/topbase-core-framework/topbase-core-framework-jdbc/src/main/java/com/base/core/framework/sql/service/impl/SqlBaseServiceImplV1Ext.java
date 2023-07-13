package com.base.core.framework.sql.service.impl;

import java.io.Serializable;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.BaseV1Ext;

/**
 * @author start
 */
public class SqlBaseServiceImplV1Ext<T extends BaseV1Ext, PK extends Serializable> extends SqlBaseServiceImpl<T,PK> {
	
	@SuppressWarnings("unused")
	private final SqlBaseDao<T,PK> baseDao;
	
	public SqlBaseServiceImplV1Ext(SqlBaseDao<T,PK> baseDao){
		super(baseDao);
		this.baseDao=baseDao;
	}

	
}