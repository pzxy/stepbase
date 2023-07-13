package com.base.common.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import com.base.core.context.annotation.SecretKeyAuthorityCheck;
import com.base.core.mvc.web.BaseMvcInterceptor;
import com.base.service.base.system.entity.SecretKeyDO;
import com.base.service.base.system.service.SecretKeyService;
import com.gitee.magic.framework.base.context.Http;

/**
 * 基础拦截器
 * @author start
 *
 */
public class BaseInterceptor extends BaseMvcInterceptor {
	
    @Autowired
    protected SecretKeyService secretKeyService;

    /**
     * 安全验证
     * @param http
     * @param handlerMethod
     */
    @Override
    public void secretKeyAuthorityCheck(Http http,HandlerMethod handlerMethod) {
    	SecretKeyAuthorityCheck check = handlerMethod.getMethodAnnotation(SecretKeyAuthorityCheck.class);
        http.requestCheck();
        SecretKeyDO secretKey=secretKeyService.getSecretKeyByAccessIdWithType(http.getAccessId(),check.value());
        http.signaturesHmacSha1Check(secretKey.getAccessKey());
    }

}
