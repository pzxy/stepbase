package com.base.core.framework.sql.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.base.core.framework.sql.entity.BaseV2Ext;
import com.base.core.framework.sql.utils.GenerateExampleV2Ext;
import com.base.core.framework.sql.utils.SqlWrapperBuilderV2Ext;
import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.jdbc.persistence.EntityObject;
import com.gitee.magic.jdbc.persistence.EntityProperty;
import com.gitee.magic.jdbc.persistence.RepositoryException;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.GenerateExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.DeleteWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start
 */
public class SqlBaseDaoImplV2Ext<T extends BaseV2Ext, PK extends Serializable>
		extends SqlBaseDaoImplV1Ext<T,PK> {
	
	public SqlBaseDaoImplV2Ext(Class<T> prototype){
		super(prototype);
	}
	
	@Override
	public int persist(T entity) {
		entity.setDeleted(0L);
		return super.persist(entity);
	}

	@Override
	public int[] persistBatch(List<T> entitys) {
		for(T entity:entitys) {
			entity.setDeleted(0L);
		}
		return super.persistBatch(entitys);
	}
	
	@Override
	public T load(PK id) {
		T t=super.load(id);
		if(t==null) {
			return null;
		}
		if(t.getDeleted()!=0L) {
			return null;
		}
		return t;
	}
	
	@Override
	public long remove(T entity) {
		UpdateWrapper wrapper=new UpdateWrapper();
		wrapper.eq("id", entity.getId())
		.set(SqlWrapperBuilderV2Ext.DELETEDFIELD, getSnowflakeIdWorkerNextId());
		return executeUpdate(wrapper);
	}
	
	@Override
	public int executeUpdate(DeleteWrapper wrapper) {
		Long deleted=getSnowflakeIdWorkerNextId();
		return executeUpdate(SqlWrapperBuilderV2Ext.getSql(getTableName(), wrapper),
				SqlWrapperBuilderV2Ext.getParams(deleted,wrapper));
//		throw new ApplicationException("逻辑删,DeleteWrapper请做特殊处理");
	}
	
	@Override
	public int executeUpdate(UpdateWrapper wrapper) {
		return executeUpdate(SqlWrapperBuilderV2Ext.getSql(getTableName(), wrapper),
				SqlWrapperBuilderV2Ext.getParams(wrapper));
	}

	@Override
	public List<T> queryForList(QueryWrapper wrapper) {
		return queryForList(SqlWrapperBuilderV2Ext.getSql(getTableName(), wrapper),
				SqlWrapperBuilderV2Ext.getParams(wrapper));
	}

	@Override
	public List<Map<String, Object>> queryForMap(QueryWrapper wrapper) {
		return queryForMap(SqlWrapperBuilderV2Ext.getSql(getTableName(), wrapper),
				SqlWrapperBuilderV2Ext.getParams(wrapper));
	}

	@Override
	public <R> R queryForObject(Class<R> requiredType, QueryWrapper wrapper) {
		return queryForObject(requiredType,SqlWrapperBuilderV2Ext.getSql(getTableName(), wrapper),
				SqlWrapperBuilderV2Ext.getParams(wrapper));
	}

	//////////////////////////////Example
	
	@Override
	public <E extends AbstractGeneratedCriteria>List<T> queryForList(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExampleV2Ext.builderSelectSql(Arrays.asList("*"), getTableName(), example, params);
		return queryForList(sql, params.toArray());
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>List<Map<String, Object>> queryForMap(List<String> selects,BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExampleV2Ext.builderSelectSql(selects, getTableName(), example, params);
		return queryForMap(sql, params.toArray());
	}

	@Override
	public <E extends AbstractGeneratedCriteria>int queryForCount(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExampleV2Ext.builderSelectSql(Arrays.asList("IFNULL(count(1),0)"), getTableName(), example, params);
		return queryForInt(sql.toString(), params.toArray());
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>int executeUpdate(T entity,BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		Class<?> prototype=entity.getClass();
		EntityObject eObject=getEntityJdbcManager().getEntityDefinition(prototype);
		List<String> updateSet=new ArrayList<>();
		for (EntityProperty member : eObject.getMembers()) {
			Object value=null;
			try {
				value = member.getDescriptor().getReadMethod().invoke(entity);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RepositoryException(e);
			}
			// 为空则不插入
			if (value == null) {
				continue;
			}
			updateSet.add(member.getTableFieldName()+"=?");
			params.add(PropertyConverterEditor.out(prototype,member.getFieldName(),member.getDataType(),value));
		}
		StringBuilder sql=new StringBuilder();
		sql.append("Update "+getTableName()+" Set ");
		sql.append(StringUtils.listToString(updateSet));
		sql.append(GenerateExampleV2Ext.generateWhereClause(example,params));
		return executeUpdate(sql.toString(),params.toArray());
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>int executeDelete(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		params.add(getSnowflakeIdWorkerNextId());
		StringBuilder sql=new StringBuilder();
		sql.append("Update "+getTableName()+" Set ");
		sql.append(SqlWrapperBuilderV2Ext.DELETEDFIELD+" = ?");
		sql.append(GenerateExample.generateWhereClause(example,params));
		return executeUpdate(sql.toString(),params.toArray());
	}
	
}