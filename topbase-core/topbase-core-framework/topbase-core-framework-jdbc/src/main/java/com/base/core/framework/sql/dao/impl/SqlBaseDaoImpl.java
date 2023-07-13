package com.base.core.framework.sql.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.Base;
import com.base.core.framework.sql.utils.ScriptDelegate;
import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.jdbc.persistence.EntityObject;
import com.gitee.magic.jdbc.persistence.EntityProperty;
import com.gitee.magic.jdbc.persistence.RepositoryException;
import com.gitee.magic.jdbc.persistence.source.jdbc.EntityJdbcManager;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.GenerateExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.mapper.SqlTemplate;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.SqlWrapperBuilder;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.DeleteWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;

/**
 * @author start
 */
public class SqlBaseDaoImpl<T extends Base, PK extends Serializable>
		extends BaseDaoImpl<T,PK> implements SqlBaseDao<T, PK>,InitializingBean {

	private EntityJdbcManager entityJdbcManager;
	
	@Value("${start.framework.print.sqltablescript:false}") 
	private boolean printSqlTableScript;
	
	public SqlBaseDaoImpl(Class<T> prototype){
		super(prototype);
	}
	
	@Override
	public int persist(T entity) {
		return getEntityJdbcManager().persist(entity);
	}

	@Override
	public long merge(T entity) {
		return getEntityJdbcManager().merge(entity);
	}
	
	@Override
	public long remove(T entity) {
		return getEntityJdbcManager().remove(entity);
	}
	
	@Override
	public int[] persistBatch(List<T> entitys) {
		return getEntityJdbcManager().persistBatch(getPrototype(), entitys);
	}

	@Override
	public int[] mergeBatch(List<T> entitys) {
		return getEntityJdbcManager().mergeBatch(getPrototype(), entitys);
	}
	
	@Override
	public T load(PK id) {
		return getEntityJdbcManager().load(getPrototype(),id);
	}
	
	/////////////////////////////////////SQL Start
	
	@Override
	public int[] executeBatch(String sql,List<Object[]> params){
		return getEntityJdbcManager().executeBatch(sql,params);
	}
	
	@Override
	public int executeUpdate(String sql, Object... params){
		return getEntityJdbcManager().executeUpdate(sql,params);
	}
	
	@Override
	public List<T> queryForList(String sql,Object... params){
		return getEntityJdbcManager().getListEntityBySql(getPrototype(), sql, params);
	}
	
	@Override
	public List<Map<String,Object>> queryForMap(String sql,Object... params){
		return getEntityJdbcManager().getListMapBySql(sql, params);
	}
	
	@Override
	public Integer queryForInt(String sql,Object... params){
		return queryForObject(Integer.class,sql,params);
	}

	@Override
	public Long queryForLong(String sql,Object... params) {
		return queryForObject(Long.class,sql,params);
	}
	
	@Override
	public Float queryForFloat(String sql,Object... params) {
		return queryForObject(Float.class,sql,params);
	}
	
	@Override
	public Double queryForDouble(String sql,Object... params) {
		return queryForObject(Double.class,sql,params);
	}

	@Override
	public <R>R queryForObject(Class<R> requiredType,String sql,Object... params){
		return getJdbcTemplate().queryForObject(sql,requiredType, params);
	}
	
	/////////////////////////////Wrapper Start
	
	@Override
	public int executeUpdate(UpdateWrapper wrapper) {
		return executeUpdate(SqlWrapperBuilder.getSql(getTableName(), wrapper),
				SqlWrapperBuilder.getParams(wrapper));
	}

	@Override
	public int executeUpdate(DeleteWrapper wrapper) {
		return executeUpdate(SqlWrapperBuilder.getSql(getTableName(), wrapper),
				SqlWrapperBuilder.getParams(wrapper));
	}

	@Override
	public List<T> queryForList(QueryWrapper wrapper) {
		return queryForList(SqlWrapperBuilder.getSql(getTableName(), wrapper),
				SqlWrapperBuilder.getParams(wrapper));
	}

	@Override
	public List<Map<String, Object>> queryForMap(QueryWrapper wrapper) {
		return queryForMap(SqlWrapperBuilder.getSql(getTableName(), wrapper),
				SqlWrapperBuilder.getParams(wrapper));
	}

	@Override
	public Integer queryForInt(QueryWrapper wrapper) {
		return queryForObject(Integer.class,wrapper);
	}
	
	@Override
	public Long queryForLong(QueryWrapper wrapper) {
		return queryForObject(Long.class,wrapper);
	}
	
	@Override
	public Float queryForFloat(QueryWrapper wrapper) {
		return queryForObject(Float.class,wrapper);
	}
	
	@Override
	public Double queryForDouble(QueryWrapper wrapper) {
		return queryForObject(Double.class,wrapper);
	}

	@Override
	public <R> R queryForObject(Class<R> requiredType, QueryWrapper wrapper) {
		return queryForObject(requiredType,SqlWrapperBuilder.getSql(getTableName(), wrapper,false),
				SqlWrapperBuilder.getParams(wrapper));
	}

	/////////////////////////////SQLMapper Start
	
	@Override
	public String mapperSql(String sqlId) {
		return mapperSql(sqlId,new HashMap<>(0));
	}

	@Override
	public String mapperSql(String sqlId, Map<String, Object> params) {
		return SqlTemplate.getMapperSqlTemplate(sqlId,params);
	}
	
	@Override
	public int executeUpdateMapper(String sqlId) {
		return executeUpdateMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public int executeUpdateMapper(String sqlId, Map<String, Object> params) {
		String sql=mapperSql(sqlId,params);
		return executeUpdate(
				SqlTemplate.getExecuteSql(sql),
				SqlTemplate.getExecuteParameter(sql, params));
	}

	@Override
	public List<T> queryForListMapper(String sqlId) {
		return queryForListMapper(sqlId,new HashMap<>(0));
	}
	
	@Override
	public List<T> queryForListMapper(String sqlId, Map<String, Object> params) {
		String sql=mapperSql(sqlId,params);
		return queryForList(
				SqlTemplate.getExecuteSql(sql),
				SqlTemplate.getExecuteParameter(sql, params));
	}

	@Override
	public List<Map<String, Object>> queryForMapMapper(String sqlId) {
		return queryForMapMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public List<Map<String, Object>> queryForMapMapper(String sqlId, Map<String, Object> params) {
		String sql=mapperSql(sqlId,params);
		return queryForMap(
				SqlTemplate.getExecuteSql(sql),
				SqlTemplate.getExecuteParameter(sql, params));
	}

	@Override
	public Integer queryForIntMapper(String sqlId) {
		return queryForIntMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public Integer queryForIntMapper(String sqlId, Map<String, Object> params) {
		return queryForObjectMapper(Integer.class,sqlId,params);
	}

	@Override
	public Long queryForLongMapper(String sqlId) {
		return queryForLongMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public Long queryForLongMapper(String sqlId, Map<String, Object> params) {
		return queryForObjectMapper(Long.class,sqlId,params);
	}

	@Override
	public Float queryForFloatMapper(String sqlId) {
		return queryForFloatMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public Float queryForFloatMapper(String sqlId, Map<String, Object> params) {
		return queryForObjectMapper(Float.class,sqlId,params);
	}

	@Override
	public Double queryForDoubleMapper(String sqlId) {
		return queryForDoubleMapper(sqlId,new HashMap<>(0));
	}

	@Override
	public Double queryForDoubleMapper(String sqlId, Map<String, Object> params) {
		return queryForObjectMapper(Double.class,sqlId,params);
	}

	@Override
	public <R> R queryForObjectMapper(Class<R> requiredType, String sqlId) {
		return queryForObjectMapper(requiredType,sqlId,new HashMap<>(0));
	}
	
	@Override
	public <R> R queryForObjectMapper(Class<R> requiredType, String sqlId, Map<String, Object> params) {
		String sql=mapperSql(sqlId,params);
		return queryForObject(requiredType,
				SqlTemplate.getExecuteSql(sql),
				SqlTemplate.getExecuteParameter(sql, params));
	}
	
	//////////////////////////////////---------------------
	
	/**
	 * Spring JDBC操作对象
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate(){
		return getEntityJdbcManager().getJdbcTemplate();
	}
	
	/**
	 * 获取JDBC操作对象
	 * @return
	 */
	public EntityJdbcManager getEntityJdbcManager() {
		return entityJdbcManager;
	}
	
	@Override
	public EntityObject getEntityObject() {
		 return getEntityJdbcManager().getEntityDefinition(getPrototype());
	}

	/**
	 * 设置JDBC操作对象手动设置只能在构造函数中设置只能设置一次
	 * @param entityJdbcManager
	 */
	@Autowired
	public void setEntityJdbcManager(EntityJdbcManager entityJdbcManager) {
		if(this.entityJdbcManager==null){
			this.entityJdbcManager = entityJdbcManager;
		}
	}
	
	/**'
	 * 获取当前实体所映射的表名
	 * @return
	 */
	public String getTableName(){
		return getEntityObject().getTableName();
	}
	
	/**
	 * 打开SQLTable语句
	 * @return
	 */
	public boolean isPrintSqlTableScript() {
		return printSqlTableScript;
	}

	public void printSqlScript() {
		if(!isPrintSqlTableScript()){
			return;
		}
		ScriptDelegate delelte=new ScriptDelegate();
		try(Connection connection= getJdbcTemplate().getDataSource().getConnection()){
			delelte.tableAnalysis(connection,getPrototype());
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.printSqlScript();
	}

	@Override
	public <E extends AbstractGeneratedCriteria>List<T> queryForList(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExample.builderSelectSql(Arrays.asList("*"), getTableName(), example, params);
		return queryForList(sql, params.toArray());
	}

	@Override
	public <E extends AbstractGeneratedCriteria>List<Map<String, Object>> queryForMap(BaseExample<E> example){
		return queryForMap(Arrays.asList("*"),example);
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>List<Map<String, Object>> queryForMap(List<String> selects,BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExample.builderSelectSql(selects, getTableName(), example, params);
		return queryForMap(sql, params.toArray());
	}

	@Override
	public <E extends AbstractGeneratedCriteria>int queryForCount(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		String sql=GenerateExample.builderSelectSql(Arrays.asList("IFNULL(count(1),0)"), getTableName(), example, params);
		return queryForInt(sql.toString(), params.toArray());
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>int executeUpdate(T entity,BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		Class<?> prototype=entity.getClass();
		EntityObject eObject=entityJdbcManager.getEntityDefinition(prototype);
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
		sql.append(GenerateExample.generateWhereClause(example,params));
		return executeUpdate(sql.toString(),params.toArray());
	}
	
	@Override
	public <E extends AbstractGeneratedCriteria>int executeDelete(BaseExample<E> example){
		List<Object> params=new ArrayList<>();
		StringBuilder sql=new StringBuilder();
		sql.append("Delete From "+getTableName());
		sql.append(GenerateExample.generateWhereClause(example,params));
		return executeUpdate(sql.toString(), params.toArray());
	}
	
	@Override
	public <R,V>R globalQueryForObject(QueryWrapper wrapper,Class<R> requiredType) {
		String sql=SqlWrapperBuilder.getSql(getTableName(), wrapper);
		Object[] args=SqlWrapperBuilder.getParams(wrapper);
		return queryForObject(requiredType,sql,args);
	}	
}