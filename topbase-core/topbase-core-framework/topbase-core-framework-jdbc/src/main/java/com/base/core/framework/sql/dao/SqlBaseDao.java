package com.base.core.framework.sql.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.jdbc.persistence.EntityObject;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.DeleteWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start
 */
public interface SqlBaseDao<T extends Base,PK extends Serializable> extends BaseDao<T, PK> {

	/**
	 * 执行批量sql
	 * @param sql
	 * @param params
	 * @return
	 */
	int[] executeBatch(String sql,List<Object[]> params);

	/**
	 * 执行sql
	 * @param sql
	 * @param params
	 * @return
	 */
	int executeUpdate(String sql, Object... params);

	/**
	 * 查询sql
	 * @param sql
	 * @param params
	 * @return
	 */
	List<T> queryForList(String sql,Object... params);

	/**
	 * 查询sql
	 * @param sql
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryForMap(String sql,Object... params);

	/**
	 * ignore
	 * @param sql
	 * @param params
	 * @return
	 */
	Integer queryForInt(String sql,Object... params);

	/**
	 * ignore
	 * @param sql
	 * @param params
	 * @return
	 */
	Long queryForLong(String sql,Object... params);

	/**
	 * ignore
	 * @param sql
	 * @param params
	 * @return
	 */
	Float queryForFloat(String sql,Object... params);

	/**
	 * ignore
	 * @param sql
	 * @param params
	 * @return
	 */
	Double queryForDouble(String sql,Object... params);

	/**
	 * ignore
	 * @param requiredType
	 * @param sql
	 * @param params
	 * @param <R>
	 * @return
	 */
	<R>R queryForObject(Class<R> requiredType,String sql,Object... params);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	int executeUpdate(UpdateWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	int executeUpdate(DeleteWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	List<T> queryForList(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	List<Map<String, Object>> queryForMap(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	Integer queryForInt(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	Long queryForLong(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	Float queryForFloat(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param wrapper
	 * @return
	 */
	Double queryForDouble(QueryWrapper wrapper);

	/**
	 * ignore
	 * @param requiredType
	 * @param wrapper
	 * @param <R>
	 * @return
	 */
	<R>R queryForObject(Class<R> requiredType,QueryWrapper wrapper);

	/**
	 * 获取SQL映射语句
	 * @param sqlId
	 * @return
	 */
	String mapperSql(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	String mapperSql(String sqlId,Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	int executeUpdateMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	int executeUpdateMapper(String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	List<T> queryForListMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	List<T> queryForListMapper(String sqlId,Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	List<Map<String,Object>> queryForMapMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryForMapMapper(String sqlId,Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	Integer queryForIntMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	Integer queryForIntMapper(String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	Long queryForLongMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	Long queryForLongMapper(String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	Float queryForFloatMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	Float queryForFloatMapper(String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param sqlId
	 * @return
	 */
	Double queryForDoubleMapper(String sqlId);

	/**
	 * ignore
	 * @param sqlId
	 * @param params
	 * @return
	 */
	Double queryForDoubleMapper(String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param requiredType
	 * @param sqlId
	 * @param <R>
	 * @return
	 */
	<R>R queryForObjectMapper(Class<R> requiredType,String sqlId);

	/**
	 * ignore
	 * @param requiredType
	 * @param sqlId
	 * @param params
	 * @param <R>
	 * @return
	 */
	<R>R queryForObjectMapper(Class<R> requiredType,String sqlId, Map<String,Object> params);

	/**
	 * ignore
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>List<T> queryForList(BaseExample<E> example);

	/**
	 * ignore
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>List<Map<String, Object>> queryForMap(BaseExample<E> example);

	/**
	 * ignore
	 * @param selects
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>List<Map<String, Object>> queryForMap(List<String> selects,BaseExample<E> example);

	/**
	 * ignore
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>int queryForCount(BaseExample<E> example);

	/**
	 * Update
	 * @param entity
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>int executeUpdate(T entity,BaseExample<E> example);

	/**
	 * 删除Example
	 * @param example
	 * @param <E>
	 * @return
	 */
	<E extends AbstractGeneratedCriteria>int executeDelete(BaseExample<E> example);

	/**
	 * 实体对象
	 * @return
	 */
	EntityObject getEntityObject();
	
	/**
	 * 全局统计包括逻辑删
	 * @param <R>
	 * @param <V>
	 * @param wrapper
	 * @param requiredType
	 * @return
	 */
	<R,V>R globalQueryForObject(QueryWrapper wrapper,Class<R> requiredType);
}
