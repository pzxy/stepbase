package com.base.service.system.service;

import java.util.List;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.system.entity.AuthDO;

/**
 * @author start 
 */
public interface AuthService extends SqlBaseService<AuthDO,Long> {
    
    /**
     * 授权检测
     * @param accessId
     * @param action
     * @param aclState	当前用户的授权状态值
     */
    void authorityCheck(String accessId,String action,int aclState);

    /**
     * 更新授权信息
     * @param action
     * @param value
     */
    void update(String action,int value);

    /**
     * 获取权限信息
     * @param action
     * @return
     */
    AuthDO getAuth(String action);
    
    /**
     * 获取子列表
     * @param <R>
     * @param rpro
     * @param parentId
     * @return
     */
    <R>List<R> queryChild(Class<R> rpro,Long parentId);
	
}
