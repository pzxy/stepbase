package com.base.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.core.framework.sql.service.impl.SqlBaseServiceImplV1Ext;
import com.base.service.system.dao.UserRoleRelationDao;
import com.base.service.system.entity.UserRoleRelationDO;
import com.base.service.system.service.UserRoleRelationService;

/**
 * @author start 
 */
@Service("userRoleRelationService")
public class UserRoleRelationServiceImpl extends SqlBaseServiceImplV1Ext<UserRoleRelationDO, Long>
		implements UserRoleRelationService {

	@SuppressWarnings("unused")
	private UserRoleRelationDao userRoleRelationDao;

	public UserRoleRelationServiceImpl(@Qualifier("userRoleRelationDao") UserRoleRelationDao userRoleRelationDao) {
		super(userRoleRelationDao);
		this.userRoleRelationDao = userRoleRelationDao;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void binding(Long userId, List<Long> roleIds) {
		// 先移除已绑定关系
		removeByUserId(userId);
		// 重新建立绑定关系
		List<UserRoleRelationDO> list = new ArrayList<>();
		for (Long roleId : roleIds) {
			UserRoleRelationDO relation = new UserRoleRelationDO();
			relation.setUserId(userId);
			relation.setRoleId(roleId);
			list.add(relation);
		}
		saveBatch(list);
	}

	@Override
	public List<Long> queryRoleId(Long userId) {
		List<UserRoleRelationDO> list=queryForList("userId", userId);
		return list.stream().map(UserRoleRelationDO::getRoleId).distinct().collect(Collectors.toList());
	}

	@Override
	public void removeByUserId(Long userId) {
		remove("userId",userId);
	}

	@Override
	public void removeByRoleId(Long roleId) {
		remove("roleId",roleId);
	}
	
	@Override
	public <R> List<UserRoleRelationDO> streamToListUserId(List<R> result, Function<R, Long> resultMapper) {
		List<Long> userIds = result.stream().map(resultMapper).distinct().collect(Collectors.toList());
		return queryForIn("userId", userIds);
	}

	@Override
	public <R> Map<Long, List<UserRoleRelationDO>> streamToMapGroupingByUserId(List<R> result,Function<R, Long> resultMapper) {
		List<UserRoleRelationDO> userRoleRelations = streamToListUserId(result,resultMapper);
		return userRoleRelations.stream().collect(Collectors.groupingBy(UserRoleRelationDO::getUserId));
	}

}
