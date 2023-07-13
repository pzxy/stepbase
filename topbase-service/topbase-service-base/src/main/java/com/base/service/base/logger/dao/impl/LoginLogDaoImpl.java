package com.base.service.base.logger.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImplV1Ext;
import com.base.service.base.logger.dao.LoginLogDao;
import com.base.service.base.logger.entity.LoginLogDO;

/**
 * @author start 
 */
@Repository("loginLogDao")
public class LoginLogDaoImpl extends SqlBaseDaoImplV1Ext<LoginLogDO, Long> implements LoginLogDao {

    public LoginLogDaoImpl() {
        super(LoginLogDO.class);
    }

}
