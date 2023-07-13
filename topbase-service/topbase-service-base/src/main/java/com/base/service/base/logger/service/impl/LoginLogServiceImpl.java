package com.base.service.base.logger.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.base.core.context.mvc.Constants;
import com.base.core.framework.sql.service.impl.SqlBaseServiceImplV1Ext;
import com.base.core.head.constants.CodeResVal;
import com.base.service.base.logger.dao.LoginLogDao;
import com.base.service.base.logger.entity.LoginLogDO;
import com.base.service.base.logger.service.LoginLogService;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.Base64;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends SqlBaseServiceImplV1Ext<LoginLogDO, Long>
        implements LoginLogService {

    @SuppressWarnings("unused")
	private LoginLogDao loginLogDao;

    @Autowired
    private RedisTemplate<String, LoginLogDO> redisTemplate;

    @Value("${LOGINLOG_ACTIVATION_HOUR:240}")
    private int activationHour;

    public LoginLogServiceImpl(@Qualifier("loginLogDao") LoginLogDao loginLogDao) {
        super(loginLogDao);
        this.loginLogDao = loginLogDao;
    }

	@Override
	public LoginLogDO buildLog(Long userId, String requestIp, String source, String userType) {
		LoginLogDO loginLog = new LoginLogDO();
        loginLog.setUserId(userId);
        loginLog.setRequestIp(requestIp);
        loginLog.setSource(source);
        loginLog.setUserType(userType);
    	loginLog.setAccessId(StringUtils.random());
    	loginLog.setAccessKey(Base64.encode(StringUtils.random().getBytes()));
        save(loginLog);
        //Log对象存入Redis中
        String loginKey = getLoginLogCacheKey(loginLog.getAccessId());
        redisTemplate.opsForValue().set(loginKey, loginLog);
        //设置有效期
    	redisTemplate.expire(loginKey, activationHour,TimeUnit.HOURS);
        return loginLog;
	}
	
	@Override
    public LoginLogDO authorityCheck(Http http) {
        http.requestCheck();
        //获取登陆Key
        String loginKey = getLoginLogCacheKey(http.getAccessId());
        LoginLogDO log = redisTemplate.opsForValue().get(loginKey);
        if (log == null) {
            throw new BusinessException(CodeResVal.CODE_401);
        }
        //签名检测
        http.signaturesHmacSha1Check(log.getAccessKey());
        //延长有效期
    	redisTemplate.expire(loginKey, activationHour,TimeUnit.HOURS);
        return log;
    }
    
    @Override
    public LoginLogDO authority(String accessId,String userType) {
    	//获取登陆Key
        String loginKey = getLoginLogCacheKey(accessId);
        LoginLogDO log = redisTemplate.opsForValue().get(loginKey);
        if (log == null) {
            throw new BusinessException(CodeResVal.CODE_401);
        }
        if(!log.getUserType().equals(userType)) {
			throw new BusinessException(CodeResVal.CODE_401);
		}
        //延长有效期
    	redisTemplate.expire(loginKey, activationHour,TimeUnit.HOURS);
        return log;
    }

    @Override
    public void logout(String accessId) {
        String loginKey = getLoginLogCacheKey(accessId);
        if (redisTemplate.hasKey(loginKey)) {
            redisTemplate.delete(loginKey);
        }
    }
    
    public static String getLoginLogCacheKey(String accessId) {
		return Constants.getKey("loginlogrediskey",accessId);
    }

}
