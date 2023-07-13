package com.base.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.base.core.framework.sql.service.impl.SqlBaseServiceImplV1Ext;
import com.base.core.head.constants.CodeResVal;
import com.base.service.system.dao.RoleAuthRelationDao;
import com.base.service.system.entity.AuthDO;
import com.base.service.system.entity.RoleAuthRelationDO;
import com.base.service.system.service.AuthService;
import com.base.service.system.service.RoleAuthRelationService;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;

/**
 * @author start 
 */
@Service("roleAuthRelationService")
public class RoleAuthRelationServiceImpl extends SqlBaseServiceImplV1Ext<RoleAuthRelationDO,Long> 
implements RoleAuthRelationService {

	private RoleAuthRelationDao roleAuthRelationDao;
	@Autowired
	private AuthService authService;
	
	public RoleAuthRelationServiceImpl(@Qualifier("roleAuthRelationDao")RoleAuthRelationDao roleAuthRelationDao) {
		super(roleAuthRelationDao);
		this.roleAuthRelationDao=roleAuthRelationDao;
	}

	@Override
	public void authorityCheck(String action, List<Long> roleIds) {
		AuthDO auth=authService.getAuth(action);
		if(auth==null) {
			return;
		}
		if(CollectionUtils.isEmpty(roleIds)) {
			throw new BusinessException(CodeResVal.CODE_403);
		}
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.in("roleId", roleIds).eq("authId", auth.getId());
		if(roleAuthRelationDao.queryForList(wrapper).isEmpty()) {
			throw new BusinessException(CodeResVal.CODE_403);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void binding(Long roleId,List<Long> authIds) {
		//先移除已绑定关系
		removeByRoleId(roleId);
		//重新建立绑定关系
		List<RoleAuthRelationDO> list=new ArrayList<>();
		for(Long authId:authIds) {
			RoleAuthRelationDO relation=new RoleAuthRelationDO();
			relation.setRoleId(roleId);
			relation.setAuthId(authId);
			list.add(relation);
		}
		saveBatch(list);
	}

	@Override
	public <R>List<R> queryAuth(Class<R> rpro,List<Long> roleIds){
		List<RoleAuthRelationDO> relations=queryForIn("roleId", roleIds);
		return authService.streamToList(relations, RoleAuthRelationDO::getAuthId, rpro);
	}
	
	@Override
	public void removeByRoleId(Long roleId) {
		remove("roleId",roleId);
	}

	@Override
	public void removeByAuthId(Long authId) {
		remove("authId",authId);
	}

}
