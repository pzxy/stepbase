package com.base.service.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImplV1Ext;
import com.base.service.system.dao.UserRoleRelationDao;
import com.base.service.system.entity.UserRoleRelationDO;

/**
 * @author start 
 */
@Repository("userRoleRelationDao")
public class UserRoleRelationDaoImpl extends SqlBaseDaoImplV1Ext<UserRoleRelationDO,Long>implements UserRoleRelationDao {

	public UserRoleRelationDaoImpl() {
		super(UserRoleRelationDO.class);
	}

}
