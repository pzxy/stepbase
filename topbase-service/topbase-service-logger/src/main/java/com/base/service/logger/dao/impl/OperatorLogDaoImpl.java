package com.base.service.logger.dao.impl;

import org.springframework.stereotype.Repository;

import com.base.core.framework.sql.dao.impl.SqlBaseDaoImpl;
import com.base.service.logger.dao.OperatorLogDao;
import com.base.service.logger.entity.OperatorLogDO;

/**
 * @author start 
 */
@Repository("operatorLogDao")
public class OperatorLogDaoImpl extends SqlBaseDaoImpl<OperatorLogDO, Long> implements OperatorLogDao {

    public OperatorLogDaoImpl() {
        super(OperatorLogDO.class);
    }

}
