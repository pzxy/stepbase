package com.base.core.framework.sql.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.Base;
import com.base.core.framework.sql.service.SqlBaseService;
import com.base.core.framework.sql.utils.SqlUtils;
import com.base.core.framework.sql.utils.SqlWrapperBuilderV2Ext;
import com.gitee.magic.core.annotations.SqlCondition.SqlConditionType;
import com.gitee.magic.framework.base.result.QueryResult;
import com.gitee.magic.framework.base.utils.MapConvert;
import com.gitee.magic.framework.base.utils.ParamValue;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.vo.QueryVO;
import com.gitee.magic.jdbc.persistence.EntityObject;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.AbstractGeneratedCriteria;
import com.gitee.magic.jdbc.persistence.source.jdbc.criteria.BaseExample;
import com.gitee.magic.jdbc.persistence.source.jdbc.mapper.SqlTemplate;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.DeleteWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.update.UpdateWrapper;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.toolkit.StringPool;

/**
 * @author start
 */
public class SqlBaseServiceImpl<T extends Base, PK extends Serializable> extends BaseServiceImpl<T,PK>
		implements SqlBaseService<T, PK> {
	
	private final SqlBaseDao<T,PK> baseDao;
	
	public SqlBaseServiceImpl(SqlBaseDao<T,PK> baseDao){
		super(baseDao);
		this.baseDao=baseDao;
	}
	
	public T load(String fieldName,Object value) {
		return get(queryForList(fieldName,value));
	}
	
	public List<T> queryForList(String fieldName,Object value){
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.eq(fieldName, value);
		return baseDao.queryForList(wrapper);
	}

	@Override
	public <R,P> R load(Class<R> rpro,P param) {
		return load(rpro,MapConvert.convertExt(param));
	}

	@Override
	public <R> R load(Class<R> rpro,Map<String, ParamValue> params) {
		QueryWrapper wrapper=buildWrapper(rpro,params);
		return get(queryForMap(rpro, wrapper));
	}
	
	public int set(PK id,String fieldName,Object value) {
		return set(Arrays.asList(id),fieldName,value);
	}
	
	public int set(Collection<PK> ids,String fieldName,Object value) {
		if(ids.isEmpty()) {
			return 0;
		}
		UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.in("id", ids).set(fieldName, value);
        return baseDao.executeUpdate(wrapper);
	}
	
	@Override
	public int remove(PK id) {
		return remove(Arrays.asList(id));
	}

	@Override
	public int remove(PK[] ids) {
		return remove(Arrays.asList(ids));
	}
	
	@Override
	public int remove(Collection<PK> ids) {
		if(ids.size()==0) {
			return 0;
		}
		DeleteWrapper wrapper=new DeleteWrapper();
		wrapper.in("id", ids);
		return baseDao.executeUpdate(wrapper);
	}
	
	public int remove(String fieldName,Object value) {
		DeleteWrapper wrapper=new DeleteWrapper();
		wrapper.eq(fieldName, value);
		return baseDao.executeUpdate(wrapper);
	}

	public <P> int executeUpdateMapper(String sqlId, P params) {
		return baseDao.executeUpdateMapper(sqlId,MapConvert.convert(params));
	}

	public <P> int queryForIntMapper(String sqlId, P params) {
		return baseDao.queryForIntMapper(sqlId,MapConvert.convert(params));
	}

	public <P> long queryForLongMapper(String sqlId, P params) {
		return baseDao.queryForLongMapper(sqlId,MapConvert.convert(params));
	}

	public <P> float queryForFloatMapper(String sqlId, P params) {
		return baseDao.queryForFloatMapper(sqlId,MapConvert.convert(params));
	}

	public <P> double queryForDoubleMapper(String sqlId, P params) {
		return baseDao.queryForDoubleMapper(sqlId,MapConvert.convert(params));
	}
	
	@Override
	public List<T> queryForByIds(PK[] ids){
		return queryForByIds(Arrays.asList(ids));
	}
	
	@Override
	public List<T> queryForByIds(Collection<PK> ids){
		return queryForIn("id",ids);
	}
	
	public <P>List<T> queryForIn(String fieldName,Collection<P> values) {
		if(CollectionUtils.isEmpty(values)) {
			return new ArrayList<>();
		}
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.in(fieldName, values);
		return baseDao.queryForList(wrapper);
	}

	@Override
	public <R> List<R> queryForByIds(Class<R> rpro, Collection<PK> ids) {
		return queryForIn(rpro,"id",ids);
	}

	public <R,P> List<R> queryForIn(Class<R> rpro, String fieldName,Collection<P> values) {
		if(CollectionUtils.isEmpty(values)) {
			return new ArrayList<>();
		}
		Map<String,ParamValue> params=new HashMap<>(0);
		QueryWrapper wrapper=buildWrapper(rpro,params);
		wrapper.in(fieldName, values);
		return queryForMap(rpro, wrapper);
	}
	
	@Override
	public List<T> queryForAll() {
		return baseDao.queryForList(new QueryWrapper());
	}

	@Override
	public <R> List<R> queryForAll(Class<R> rpro) {
		return result(rpro,queryForMapAll());
	}
	
	@Override
	public List<Map<String, Object>> queryForMapAll() {
		return baseDao.queryForMap(new QueryWrapper());
	}
	
	@Override
	public <R, P> List<R> queryForMap(Class<R> rpro,P param) {
		return queryForMap(rpro, MapConvert.convert(param));
	}

	@Override
	public <R> List<R> queryForMap(Class<R> rpro,Map<String, Object> params) {
		QueryWrapper wrapper=buildQueryWrapper(rpro,params);
		wrapper.orderByDesc("id");
		return queryForMap(rpro, wrapper);
	}
	
	@Override
	public <R> List<R> queryForMapParamValue(Class<R> rpro,Map<String, ParamValue> params) {
		QueryWrapper wrapper=buildWrapper(rpro,params);
		return queryForMap(rpro, wrapper);
	}

	public <R> List<R> queryForMap(Class<R> rpro, QueryWrapper wrapper) {
		return result(rpro,baseDao.queryForMap(wrapper));
	}

	public <R, P> List<R> queryForMapMapper(Class<R> rpro, String sqlId, P params) {
		return queryForMapMapper(rpro,sqlId,MapConvert.convert(params));
	}

	public <R> List<R> queryForMapMapper(Class<R> rpro, String sqlId, Map<String, Object> params) {
		return result(rpro,queryForMapMapper(sqlId, params));
	}

	public <P> List<Map<String, Object>> queryForMapMapper(String sqlId, P params) {
		return queryForMapMapper(sqlId, MapConvert.convert(params));
	}
	
	public List<Map<String,Object>> queryForMapMapper(String sqlId, Map<String, Object> params) {
		return baseDao.queryForMapMapper(sqlId, params);
	}
	
	public <R> QueryWrapper buildQueryWrapper(Class<R> rpro, Map<String, Object> params) {
		return buildQueryWrapper(true,rpro,params);
	}
	
	public <R> QueryWrapper buildQueryWrapper(Boolean idhandle,Class<R> rpro, Map<String, Object> params){
		EntityObject eObject=baseDao.getEntityObject();
		Map<String, ParamValue> paramsVal=new HashMap<>(10);
		for(String p:params.keySet()) {
			ParamValue pv=new ParamValue();
			pv.setCondition(new SqlConditionType[]{SqlConditionType.EQ});
			pv.setValue(params.get(p));
			paramsVal.put(p, pv);
		}
		return SqlWrapperBuilderV2Ext.buildQueryWrapper(eObject,idhandle,rpro,paramsVal);
	}
	
	public <R,P> QueryWrapper buildWrapper(Class<R> rpro, P param) {
		Map<String,ParamValue> params=MapConvert.convertExt(param);
		return buildWrapper(rpro,params);
	}
	
	public <R> QueryWrapper buildWrapper(Class<R> rpro, Map<String, ParamValue> params) {
		return SqlWrapperBuilderV2Ext.buildQueryWrapper(baseDao.getEntityObject(),true,rpro,params);
	}

	//////////////////////////QueryResult不建议使用
	
	public <R>QueryResult<List<R>> queryForPage(Class<R> rpro, String sqlId, Map<String, Object> params) {
		String mapperSql=baseDao.mapperSql(sqlId,params);
		List<Map<String, Object>> results =baseDao.queryForMap(
				SqlTemplate.getExecuteSql(mapperSql),
				SqlTemplate.getExecuteParameter(mapperSql, params));
		if (results.isEmpty()) {
        	return null;
		}
		//转化为统计查询Sql
		String mapperSqlCount=SqlUtils.convertCountSql(mapperSql);
		int totalCount = baseDao.queryForInt(
				SqlTemplate.getExecuteSql(mapperSqlCount), 
				SqlTemplate.getExecuteParameter(mapperSqlCount, params));
		return queryResult(rpro, results, totalCount);
	}

	public QueryResult<List<Map<String, Object>>> queryForPage(String sqlId, Map<String, Object> params) {
		String mapperSql=baseDao.mapperSql(sqlId,params);
		List<Map<String, Object>> results =baseDao.queryForMap(
				SqlTemplate.getExecuteSql(mapperSql),
				SqlTemplate.getExecuteParameter(mapperSql, params));
		if (results.isEmpty()) {
        	return null;
		}
		//转化为统计查询Sql
		String mapperSqlCount=SqlUtils.convertCountSql(mapperSql);
		int totalCount = baseDao.queryForInt(
				SqlTemplate.getExecuteSql(mapperSqlCount), 
				SqlTemplate.getExecuteParameter(mapperSqlCount, params));
		return queryResult(results, totalCount);
	}

	public <R> QueryResult<List<R>> queryForPage(Class<R> rpro, QueryWrapper wrapper) {
        List<Map<String, Object>> results = baseDao.queryForMap(wrapper);
        if (results.isEmpty()) {
        	return null;
        }
        //重设查询统计,last("limit 1,10");应清空
        wrapper.select("count(1)");
        int totalCount = baseDao.queryForInt(wrapper.last(StringPool.EMPTY));
        return queryResult(rpro, results, totalCount);
    }

	public <R, E extends AbstractGeneratedCriteria> QueryResult<List<R>> queryForPage(Class<R> rpro, BaseExample<E> example) {
		return queryForPage(rpro,Arrays.asList("*"),example);
	}

	public <R, E extends AbstractGeneratedCriteria> QueryResult<List<R>> queryForPage(Class<R> rpro, List<String> selects,
			BaseExample<E> example) {
		List<Map<String, Object>> results = baseDao.queryForMap(selects,example);
        if (results.isEmpty()) {
        	return null;
        }
        example.pageClear();
        int totalCount = baseDao.queryForCount(example);
        return queryResult(rpro, results, totalCount);
	}
	
	@Override
	public <R, P> QueryResult<List<R>> queryForPage(Class<R> rpro, P param) {
		Map<String,Object> params=MapConvert.convert(param);
		int pageIndex=Integer.parseInt(String.valueOf(params.get("pageIndex")));
		int pageSize=Integer.parseInt(String.valueOf(params.get("pageSize")));
		int index=(pageIndex - 1) * pageSize;
		return queryForPage(rpro,MapConvert.convertExt(param),index,pageSize);
	}

	@Override
	public <R, P> QueryResult<List<R>> queryForPage(Class<R> rpro, P param,int pageIndex,int pageSize) {
		return queryForPage(rpro,MapConvert.convertExt(param),pageIndex,pageSize);
	}
	
	@Override
	public <R> QueryResult<List<R>> queryForPage(Class<R> rpro, Map<String, ParamValue> params,int pageIndex,int pageSize) {
		QueryWrapper wrapper=buildWrapper(rpro,params);
		if(wrapper.isEmptyOfWhere()) {
			wrapper.eq("1","1");
		}
		wrapper.orderByDesc("id");
		wrapper.last("limit " + pageIndex+","+pageSize);
		return queryForPage(rpro, wrapper);
	}
	
	///////////////////////////QueryVO
	
	public <R>QueryVO<R> pageWrapper(Class<R> rpro, String sqlId, Map<String, Object> params) {
		QueryVO<Map<String, Object>> query=pageWrapper(sqlId, params);
		if(query==null) {
        	return null;
		}
		return queryVO(rpro,query.getData(),query.getTotal());
	}

	public QueryVO<Map<String, Object>> pageWrapper(String sqlId, Map<String, Object> params) {
		String mapperSql=baseDao.mapperSql(sqlId,params);
		List<Map<String, Object>> results =baseDao.queryForMap(
				SqlTemplate.getExecuteSql(mapperSql),
				SqlTemplate.getExecuteParameter(mapperSql, params));
		if (results.isEmpty()) {
        	return null;
		}
		//转化为统计查询Sql
		String mapperSqlCount=SqlUtils.convertCountSql(mapperSql);
		int total = baseDao.queryForInt(
				SqlTemplate.getExecuteSql(mapperSqlCount), 
				SqlTemplate.getExecuteParameter(mapperSqlCount, params));
		return queryVO(results, total);
	}

	public <R> QueryVO<R> pageWrapper(Class<R> rpro, QueryWrapper wrapper) {
        List<Map<String, Object>> results = baseDao.queryForMap(wrapper);
        if (results.isEmpty()) {
        	return null;
        }
        //重设查询统计,last("limit 1,10");应清空
        wrapper.select("count(1)");
        int totalCount = baseDao.queryForInt(wrapper.last(StringPool.EMPTY));
        return queryVO(rpro, results, totalCount);
    }

	public <R, E extends AbstractGeneratedCriteria> QueryVO<R> pageWrapper(Class<R> rpro, BaseExample<E> example) {
		return pageWrapper(rpro,Arrays.asList("*"),example);
	}

	public <R, E extends AbstractGeneratedCriteria> QueryVO<R> pageWrapper(Class<R> rpro, List<String> selects,
			BaseExample<E> example) {
		List<Map<String, Object>> results = baseDao.queryForMap(selects,example);
        if (results.isEmpty()) {
        	return null;
        }
        example.pageClear();
        int totalCount = baseDao.queryForCount(example);
        return queryVO(rpro, results, totalCount);
	}

	@Override
	public <R, P> QueryVO<R> pageWrapper(Class<R> rpro, P param) {
		Map<String,Object> params=MapConvert.convert(param);
		int pageIndex=Integer.parseInt(String.valueOf(params.get("pageIndex")));
		int pageSize=Integer.parseInt(String.valueOf(params.get("pageSize")));
		int index=(pageIndex - 1) * pageSize;
		return pageWrapper(rpro,MapConvert.convertExt(param),index,pageSize);
	}

	@Override
	public <R, P> QueryVO<R> pageWrapper(Class<R> rpro, P param, int pageIndex, int pageSize) {
		return pageWrapper(rpro,MapConvert.convertExt(param),pageIndex,pageSize);
	}

	@Override
	public <R> QueryVO<R> pageWrapper(Class<R> rpro, Map<String, ParamValue> params, int pageIndex, int pageSize) {
		QueryWrapper wrapper=buildWrapper(rpro,params);
		if(wrapper.isEmptyOfWhere()) {
			wrapper.eq("1","1");
		}
		wrapper.orderByDesc("id");
		wrapper.last("limit " + pageIndex+","+pageSize);
		return pageWrapper(rpro, wrapper);
	}
	
	@Override
	public <R>List<T> streamToList(List<R> result,Function<R,PK> resultMapper){
		List<PK> ids = result.stream().map(resultMapper).distinct().collect(Collectors.toList());
		return queryForByIds(ids);
	}
	
	@Override
	public <R,P>List<P> streamToList(List<R> result,Function<R,PK> resultMapper,Class<P> rpro){
		List<PK> ids = result.stream().map(resultMapper).distinct().collect(Collectors.toList());
		return queryForByIds(rpro,ids);
	}
	
	@Override
	public <R,P>Map<PK,P> streamToMap(List<R> result,Function<R,PK> resultMapper,Class<P> rpro,Function<P,PK> valMapper){
		List<P> list=streamToList(result,resultMapper,rpro);
		return list.stream().collect(Collectors.toMap(valMapper, Function.identity()));
	}

	@Override
	public <R, P,KV> Map<KV, List<P>> streamToMapGroupingBy(List<R> result, Function<R, PK> resultMapper, Class<P> rpro,
			Function<P, KV> valMapper) {
		List<P> list=streamToList(result,resultMapper,rpro);
		return list.stream().collect(Collectors.groupingBy(valMapper));
	}

	@Override
	public void checkUnique(PK id,String fieldName, Object value, String message) {
		Map<String,Object> params=new HashMap<>(1);
		params.put(fieldName, value);
		checkUnique(id,params,message);
	}
	
	@Override
	public void checkUnique(PK id,Map<String,Object> params, String message) {
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.select("IFNULL(Count(1),0)");
		wrapper.ne(id!=null,"id", id);
		wrapper.allEq(params);
		if(baseDao.globalQueryForObject(wrapper,Integer.class)>0) {
			throw new BusinessException(message);
		}
	}
	
}