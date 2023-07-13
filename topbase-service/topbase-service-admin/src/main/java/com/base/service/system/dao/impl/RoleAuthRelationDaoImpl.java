package com.base.service.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImplV1Ext;
import com.base.service.system.dao.RoleAuthRelationDao;
import com.base.service.system.entity.RoleAuthRelationDO;

/**
 * @author start 
 */
@Repository("roleAuthRelationDao")
public class RoleAuthRelationDaoImpl extends SqlBaseDaoImplV1Ext<RoleAuthRelationDO,Long>implements RoleAuthRelationDao {

	public RoleAuthRelationDaoImpl() {
		super(RoleAuthRelationDO.class);
	}

}
