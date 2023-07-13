package com.base.core.framework.mongo.service;

import java.io.Serializable;

import com.base.core.framework.mongo.entity.MongoBase;

/**
 * @author start 
 */
public abstract interface MongoBaseService<T extends MongoBase,PK extends Serializable> {

	/**
	 * 保存或更新
	 * @param entity
	 */
	void save(T entity);

	/**
	 * 删除
	 * @param pk
	 */
	void remove(PK pk);
	
}
