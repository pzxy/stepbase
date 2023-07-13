package com.base.service.system.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.base.core.framework.sql.service.SqlBaseService;
import com.base.service.system.entity.UserRoleRelationDO;

/**
 * @author start 
 */
public interface UserRoleRelationService extends SqlBaseService<UserRoleRelationDO,Long> {
	
	/**
	 * 绑定角色关系
	 * @param userId
	 * @param roleIds
	 */
	void binding(Long userId,List<Long> roleIds);

	/**
	 * 根据ID获取角色ID列表
	 * @param userId
	 * @return
	 */
	List<Long> queryRoleId(Long userId);
	
	/**
	 * 移除已绑定关系
	 * @param userId
	 */
	void removeByUserId(Long userId);

	/**
	 * 删除角色ID
	 * @param roleId
	 */
	void removeByRoleId(Long roleId);
	
	/**
	 * ignore
	 * @param <R>
	 * @param result
	 * @param resultMapper
	 * @return
	 */
	<R> List<UserRoleRelationDO> streamToListUserId(List<R> result, Function<R, Long> resultMapper);
	
	/**
	 * ignore
	 * @param <R>
	 * @param result
	 * @param resultMapper
	 * @return
	 */
	<R>Map<Long, List<UserRoleRelationDO>> streamToMapGroupingByUserId(List<R> result, Function<R, Long> resultMapper);
	
}
