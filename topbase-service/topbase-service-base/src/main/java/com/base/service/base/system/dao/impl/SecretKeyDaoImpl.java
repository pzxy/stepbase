package com.base.service.base.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImpl;
import com.base.service.base.system.dao.SecretKeyDao;
import com.base.service.base.system.entity.SecretKeyDO;

/**
 * @author start 
 */
@Repository("secretKeyDao")
public class SecretKeyDaoImpl extends SqlBaseDaoImpl<SecretKeyDO, Long> implements SecretKeyDao {

    public SecretKeyDaoImpl() {
        super(SecretKeyDO.class);
    }

}