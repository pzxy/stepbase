package com.base.service.logger.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.base.core.context.utils.LoggerWrapper;
import com.base.core.framework.sql.service.impl.SqlBaseServiceImpl;
import com.base.service.logger.dao.OperatorLogDao;
import com.base.service.logger.entity.OperatorLogDO;
import com.base.service.logger.service.OperatorLogService;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.utils.IpUtils;

/**
 * @author start 
 */
@EnableAsync
@Service("operatorLogService")
public class OperatorLogServiceImpl extends SqlBaseServiceImpl<OperatorLogDO, Long> implements OperatorLogService {

	@SuppressWarnings("unused")
	private OperatorLogDao operatorLogDao;

	public OperatorLogServiceImpl(@Qualifier("operatorLogDao") OperatorLogDao operatorLogDao) {
		super(operatorLogDao);
		this.operatorLogDao = operatorLogDao;
	}

	@Async
	@Override
	public void saveLog(
			String accessId, 
			String name, 
			String method,
			String uri, 
			String queryString,
			JsonObject requestHeader,
			String requestBody, 
			String responseBody,
			String requestId,
			String requestIp) {
		OperatorLogDO operatorLog = new OperatorLogDO();
		operatorLog.setAccessId(accessId);
		operatorLog.setName(name);
		operatorLog.setMethod(method);
		operatorLog.setUri(uri);
		operatorLog.setQueryString(queryString);
		operatorLog.setRequestHeader(requestHeader);
		operatorLog.setRequestContent(requestBody);
		operatorLog.setResponseContent(responseBody);
		operatorLog.setRequestId(requestId);
		operatorLog.setRequestIp(requestIp);
		operatorLog.setLocalIp(IpUtils.getLocalIpByNetcard());
		operatorLog.setServerTime(new Date());
		operatorLog.setWorkerId(Config.getBalancedWorkerId());
		operatorLog.setDataCenterId(Config.getBalancedDataCenterId());
		try {
			save(operatorLog);
		} catch (Exception e) {
			LoggerWrapper.print(e);
		}
	}

}
