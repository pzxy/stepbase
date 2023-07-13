package com.base.core.framework.sql.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.base.core.framework.sql.dao.BaseDao;
import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.core.utils.SnowflakeIdWorker;

/**
 * @author start
 */
public abstract class BaseDaoImpl<T extends Base, PK extends Serializable>
		implements BaseDao<T, PK> {

	/**
	 * Prototype
	 */
	private final Class<T> prototype;
	
	/**
	 * twitter_Snowflake 
	 */
	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	
	public BaseDaoImpl(Class<T> prototype){
		this.prototype=prototype;
	}
	
	@Override
	public Class<T> getPrototype() {
		return prototype;
	}
	
	@Override
	public long getSnowflakeIdWorkerNextId(){
		return snowflakeIdWorker.nextId();
	}
	
}