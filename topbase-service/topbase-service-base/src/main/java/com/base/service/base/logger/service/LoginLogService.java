package com.base.service.base.logger.service;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.base.logger.entity.LoginLogDO;
import com.gitee.magic.framework.base.context.Http;

/**
 * @author start 
 */
public interface LoginLogService extends SqlBaseService<LoginLogDO, Long> {
    
    /**
     * 生成LoginLog信息
     * @param userId	用户ID
     * @param requestIp	请求IP
     * @param source	来源
     * @param userType		用户类型
     * @return
     */
    LoginLogDO buildLog(Long userId,String requestIp,String source,String userType);

    /**
     * 授权检测
     * @param http
     * @return
     */
    LoginLogDO authorityCheck(Http http);

    /**
     * 获取登陆Log
     * @param accessId
     * @param userType
     * @return
     */
    LoginLogDO authority(String accessId,String userType);

    /**
     * 退出登录
     * @param accessId
     */
    void logout(String accessId);

}
