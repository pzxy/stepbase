package com.base.core.framework.sql.service.impl;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.BaseV2Ext;
import com.base.core.framework.sql.service.SqlBaseServiceStreamV2Ext;
import com.base.core.head.vo.EntityVO;

/**
 * @author start
 */
public class SqlBaseServiceStreamImplV2Ext<T extends BaseV2Ext, VO extends EntityVO>
		extends SqlBaseServiceStreamImplV1Ext<T, VO> implements SqlBaseServiceStreamV2Ext<T, VO> {

	@SuppressWarnings("unused")
	private final SqlBaseDao<T, Long> baseDao;

	public SqlBaseServiceStreamImplV2Ext(SqlBaseDao<T, Long> baseDao,Class<VO> prototype) {
		super(baseDao,prototype);
		this.baseDao = baseDao;
	}

}