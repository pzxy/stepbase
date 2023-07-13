package com.base.core.framework.sql.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.base.core.framework.sql.entity.BaseV1Ext;

/**
 * @author start
 */
public class SqlBaseDaoImplV1Ext<T extends BaseV1Ext, PK extends Serializable>
		extends SqlBaseDaoImpl<T,PK> {
	
	public SqlBaseDaoImplV1Ext(Class<T> prototype){
		super(prototype);
	}

	@Override
	public int persist(T entity) {
		entity.setCreatedDate(new Date());
		entity.setModifiedDate(new Date());
		return super.persist(entity);
	}

	@Override
	public long merge(T entity) {
		entity.setModifiedDate(new Date());
		return super.merge(entity);
	}

	@Override
	public int[] persistBatch(List<T> entitys) {
		for(T entity:entitys) {
			entity.setCreatedDate(new Date());
			entity.setModifiedDate(new Date());
		}
		return super.persistBatch(entitys);
	}

	@Override
	public int[] mergeBatch(List<T> entitys) {
		for(T entity:entitys) {
			entity.setModifiedDate(new Date());
		}
		return super.mergeBatch(entitys);
	}
	

}