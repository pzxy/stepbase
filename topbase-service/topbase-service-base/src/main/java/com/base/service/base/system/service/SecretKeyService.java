package com.base.service.base.system.service;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.base.system.entity.SecretKeyDO;

/**
 * @author start 
 */
public interface SecretKeyService extends SqlBaseService<SecretKeyDO, Long> {
    
    /**
     * 获取密钥
     * @param accessId
     * @param type
     * @return
     */
    SecretKeyDO getSecretKeyByAccessIdWithType(String accessId,Integer type);

}
