package com.base.service.system.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.base.core.context.annotation.Cache;
import com.base.core.framework.sql.service.impl.SqlBaseServiceImplV1Ext;
import com.base.core.head.constants.CodeResVal;
import com.base.service.system.dao.AuthDao;
import com.base.service.system.entity.AuthDO;
import com.base.service.system.service.AuthService;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.Acl;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start 
 */
@Service("authService")
public class AuthServiceImpl extends SqlBaseServiceImplV1Ext<AuthDO, Long> implements AuthService {

	private AuthDao authDao;

	public AuthServiceImpl(@Qualifier("authDao") AuthDao authDao) {
		super(authDao);
		this.authDao = authDao;
	}
	
	@Cache(key = "#accessId+'_'+#action+'_'+#aclState",value = "authService_authorityCheck",expire = 600)
	@Override
	public void authorityCheck(String accessId,String action,int aclState) {
		AuthDO auth=getAuth(action);
		Acl acl=new Acl();
		acl.setAuthorizeTotalValue(aclState);
		if(!acl.isAuthorize(auth.getValue())) {
			throw new BusinessException(CodeResVal.CODE_403);
		}
	}

	@Override
	public void update(String action,int value) {
		UpdateWrapper wrapper=new UpdateWrapper();
		wrapper.eq("action", action).set("value", value);
		authDao.executeUpdate(wrapper);
	}

	@Override
	public int remove(Collection<Long> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			return 0;
		}
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.select("IFNULL(COUNT(1),0)");
		wrapper.in("parentId", ids);
		if(authDao.queryForInt(wrapper)>0) {
			throw new BusinessException(CodeResVal.CODE_10012);
		}
		return super.remove(ids);
	}

	@Override
	public AuthDO getAuth(String action) {
		AuthDO auth=load("action",action);
		if(auth==null) {
			throw new BusinessException(CodeResVal.CODE_10010);
		}
		return auth;
	}

	@Override
	public <R> List<R> queryChild(Class<R> rpro, Long parentId) {
		Map<String,Object> params=new HashMap<>(1);
		params.put("parentId", parentId);
		QueryWrapper wrapper=buildQueryWrapper(rpro, params);
		wrapper.orderByAsc("sort");
		return queryForMap(rpro,wrapper);
	}

}
