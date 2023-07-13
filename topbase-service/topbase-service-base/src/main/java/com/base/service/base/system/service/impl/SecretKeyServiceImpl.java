package com.base.service.base.system.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.base.core.context.annotation.Cache;
import com.base.core.framework.sql.service.impl.SqlBaseServiceImpl;
import com.base.core.head.constants.CodeResVal;
import com.base.service.base.system.dao.SecretKeyDao;
import com.base.service.base.system.entity.SecretKeyDO;
import com.base.service.base.system.entity.SecretKeyExample;
import com.base.service.base.system.entity.SecretKeyExample.Criteria;
import com.base.service.base.system.service.SecretKeyService;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
@Service("secretKeyService")
public class SecretKeyServiceImpl extends SqlBaseServiceImpl<SecretKeyDO, Long>
        implements SecretKeyService {

    private SecretKeyDao secretKeyDao;

    public SecretKeyServiceImpl(@Qualifier("secretKeyDao") SecretKeyDao secretKeyDao) {
        super(secretKeyDao);
        this.secretKeyDao = secretKeyDao;
    }

    @Override
    @Cache(key = "#accessId+'_'+#type",value = "'SecretKeyServiceImpl_getSecretKeyByAccessIdWithType_accessId_type1'",expire = 60)
    public SecretKeyDO getSecretKeyByAccessIdWithType(String accessId,Integer type) {
    	SecretKeyExample example=new SecretKeyExample();
    	Criteria criteria=example.createCriteria();
    	criteria.andAccessIdEqualTo(accessId).andTypeEqualTo(type);
        SecretKeyDO secretKey = get(secretKeyDao.queryForList(example));
        if (secretKey == null) {
            throw new BusinessException(CodeResVal.CODE_401);
        }
        if(secretKey.getInvalidTime().getTime()<System.currentTimeMillis()) {
            throw new BusinessException(CodeResVal.CODE_401);
        }
        return secretKey;
    }

}