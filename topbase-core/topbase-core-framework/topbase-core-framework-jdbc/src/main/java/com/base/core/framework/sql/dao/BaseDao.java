package com.base.core.framework.sql.dao;

import java.io.Serializable;
import java.util.List;

import com.base.core.framework.sql.entity.Base;

/**
 * @author start
 */
public interface BaseDao<T extends Base,PK extends Serializable> {
	
	/**
	 * 保存对象
	 * @param entity
	 * @return
	 */
	int persist(T entity);
	
	/**
	 * 根据主键更新对象
	 * @param entity
	 * @return
	 */
	long merge(T entity);
	
	/**
	 * 根据主键删除对象
	 * @param entity
	 * @return
	 */
	long remove(T entity);

	/**
	 * 批量保存对象
	 * @param entitys
	 * @return
	 */
	int[] persistBatch(List<T> entitys);

	/**
	 * 批量更新对象
	 * @param entitys
	 * @return
	 */
	int[] mergeBatch(List<T> entitys);

	/**
	 * 根据主键加载对象
	 * @param id
	 * @return
	 */
	T load(PK id);
	
	/**
	 * 获取全局唯一ID
	 * @return
	 */
	long getSnowflakeIdWorkerNextId();
	
	/**
	 * 获取当前实例类
	 * @return
	 */
	Class<T> getPrototype();
	
}
