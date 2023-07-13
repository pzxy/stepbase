package com.base.service.base.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImpl;
import com.base.service.base.system.dao.ConfigureDao;
import com.base.service.base.system.entity.ConfigureDO;

/**
 * @author start 
 */
@Repository("configureDao")
public class ConfigureDaoImpl extends SqlBaseDaoImpl<ConfigureDO, Long> implements ConfigureDao {

    public ConfigureDaoImpl() {
        super(ConfigureDO.class);
    }

}
