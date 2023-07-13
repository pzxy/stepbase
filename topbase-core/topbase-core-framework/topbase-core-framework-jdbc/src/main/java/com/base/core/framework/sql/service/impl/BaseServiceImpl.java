package com.base.core.framework.sql.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.base.core.framework.sql.dao.BaseDao;
import com.base.core.framework.sql.entity.Base;
import com.base.core.framework.sql.service.BaseService;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.framework.base.result.QueryResult;
import com.gitee.magic.framework.base.result.PageResponse.PageInfo;
import com.gitee.magic.framework.base.utils.MapConvert;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public class BaseServiceImpl<T extends Base, PK extends Serializable> 
		implements BaseService<T, PK> {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private final BaseDao<T,PK> baseDao;
	
	public BaseServiceImpl(BaseDao<T,PK> baseDao){
		this.baseDao=baseDao;
	}

	@Override
	public long save(T entity) {
		if(entity.getId()==null){
			entity.setId(baseDao.getSnowflakeIdWorkerNextId());
			return baseDao.persist(entity);
		}else{
			return baseDao.merge(entity);
		}
	}

	@Override
	public <P>T to(PK pk,P param){
		T target=ReflectUtils.newInstance(baseDao.getPrototype());
		BeanUtils.copyProperties(param, target);
		if(pk!=null) {
			target.setId((Long)pk);
		}
		return target;
	}
	
	@Override
	public <P>Long saveForUpdate(PK pk,P param){
		T target=to(pk,param);
		save(target);
		return target.getId();
	}

	@Override
	public void saveBatch(List<T> entitys) {
		List<T> iEntitys=new ArrayList<>();
		List<T> uEntitys=new ArrayList<>();
		for(T entity:entitys) {
			if(entity.getId()==null){
				entity.setId(baseDao.getSnowflakeIdWorkerNextId());
				iEntitys.add(entity);
			} else {
				uEntitys.add(entity);
			}
		}
		if(!iEntitys.isEmpty()){
			baseDao.persistBatch(iEntitys);
		}
		if(!uEntitys.isEmpty()){
			baseDao.mergeBatch(uEntitys);
		}
	}

	@Override
	public long remove(T entity) {
		return baseDao.remove(entity);
	}

	@Override
	public T load(PK pk) {
		T t=baseDao.load(pk);
		if(t==null) {
			throw new BusinessException(BaseCode.CODE_1017,pk,baseDao.getPrototype().getName());
		}
		return t;
	}
	
	@Override
	public <R>R get(List<R> collections) {
		return get(collections, true);
	}

	@Override
	public <R>R get(List<R> collections, Boolean flag) {
		if (collections == null) {
			return null;
		}
		if (collections.isEmpty()) {
			return null;
		}
		if (collections.size() > 1 && flag) {
			throw new ApplicationException("Data repetition");
		}
		return collections.get(0);
	}

	@Override
	public <R>R result(Class<R> prototype,Map<String,Object> data){
		return MapConvert.convert(prototype, data);
	}
	
	@Override
	public <R>List<R> result(Class<R> prototype,List<Map<String,Object>> datas){
		List<R> results=new ArrayList<>();
		for(Map<String,Object> data:datas) {
			results.add(result(prototype, data));
		}
		return results;
	}
	
	@Override
	public <R>QueryResult<List<R>> queryResult(Class<R> prototype,List<Map<String,Object>> datas,int totalCount){
		List<R> results=new ArrayList<>();
		for(Map<String,Object> data:datas) {
			results.add(result(prototype, data));
		}
		return queryResult(results,totalCount);
	}
	
	@Override
	public <R>QueryResult<List<R>> queryResult(List<R> results,int totalCount){
		QueryResult<List<R>> query=new QueryResult<>();
		PageInfo pageInfo=new PageInfo();
		pageInfo.setTotalCount(totalCount);
		query.setResult(results);
		query.setPageInfo(pageInfo);
		return query;
	}

	@Override
	public <R> QueryVO<R> queryVO(Class<R> prototype, List<Map<String, Object>> datas, int totalCount) {
		List<R> results=new ArrayList<>();
		for(Map<String,Object> data:datas) {
			results.add(result(prototype, data));
		}
		return queryVO(results,totalCount);
	}

	@Override
	public <R> QueryVO<R> queryVO(List<R> results, int totalCount) {
		return new QueryVO<>(results,totalCount);
	}
	
}