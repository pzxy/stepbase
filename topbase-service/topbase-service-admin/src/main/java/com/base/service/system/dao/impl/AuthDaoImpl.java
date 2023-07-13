package com.base.service.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImplV1Ext;
import com.base.service.system.dao.AuthDao;
import com.base.service.system.entity.AuthDO;

/**
 * @author start 
 */
@Repository("authDao")
public class AuthDaoImpl extends SqlBaseDaoImplV1Ext<AuthDO,Long>implements AuthDao {

	public AuthDaoImpl() {
		super(AuthDO.class);
	}

}
