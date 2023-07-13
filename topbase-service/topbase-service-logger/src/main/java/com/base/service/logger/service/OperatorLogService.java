package com.base.service.logger.service;

import com.base.core.context.mvc.RequestLog;
import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.logger.entity.OperatorLogDO;

/**
 * @author start 
 */
public interface OperatorLogService extends SqlBaseService<OperatorLogDO, Long>,RequestLog {

}
