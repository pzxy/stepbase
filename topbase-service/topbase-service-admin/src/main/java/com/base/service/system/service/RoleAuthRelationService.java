package com.base.service.system.service;

import java.util.List;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.system.entity.RoleAuthRelationDO;

/**
 * @author start 
 */
public interface RoleAuthRelationService extends SqlBaseService<RoleAuthRelationDO,Long> {

	/**
	 * 授权检测
	 * @param action
	 * @param roleIds
	 */
    void authorityCheck(String action,List<Long> roleIds);

	/**
	 * 绑定授权关系
	 * @param roleId
	 * @param authIds
	 */
	void binding(Long roleId,List<Long> authIds);
	
	/**
	 * 获取角色对应的授权列表
	 * @param <R>
	 * @param rpro
	 * @param roleIds
	 * @return
	 */
	<R>List<R> queryAuth(Class<R> rpro,List<Long> roleIds);
	
	/**
	 * 移除已绑定关系
	 * @param roleId
	 */
	void removeByRoleId(Long roleId);
	
	/**
	 * 移除已绑定关系
	 * @param authId
	 */
	void removeByAuthId(Long authId);
	
}
