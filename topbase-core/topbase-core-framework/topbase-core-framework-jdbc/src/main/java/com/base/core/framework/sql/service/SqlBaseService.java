package com.base.core.framework.sql.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.framework.base.result.QueryResult;
import com.gitee.magic.framework.base.utils.ParamValue;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public interface SqlBaseService<T extends Base,PK extends Serializable> extends BaseService<T, PK> {

	/**
	 * 加载对象
	 * @param rpro
	 * @param param
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R,P>R load(Class<R> rpro,P param);

	/**
	 * 加载对象
	 * @param rpro
	 * @param params
	 * @param <R>
	 * @return
	 */
	<R>R load(Class<R> rpro,Map<String,ParamValue> params);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int remove(PK id);

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	int remove(PK[] ids);

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	int remove(Collection<PK> ids);

	/**
	 * 查询对象
	 * @param ids
	 * @return
	 */
	List<T> queryForByIds(PK[] ids);

	/**
	 * 查询对象
	 * @param ids
	 * @return
	 */
	List<T> queryForByIds(Collection<PK> ids);

	/**
	 * 查询对象
	 * @param <R>
	 * @param rpro
	 * @param ids
	 * @return
	 */
	<R>List<R> queryForByIds(Class<R> rpro, Collection<PK> ids);

	/**
	 * 获取所有对象
	 * @return
	 */
	List<T> queryForAll();

	/**
	 * 获取所有对象
	 * @param rpro
	 * @param <R>
	 * @return
	 */
	<R> List<R> queryForAll(Class<R> rpro);

	/**
	 * 获取所有Map
	 * @return
	 */
	List<Map<String, Object>> queryForMapAll();

	/**
	 * 获取对象
	 * @param rpro
	 * @param param
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R, P> List<R> queryForMap(Class<R> rpro, P param);

	/**
	 * 获取对象
	 * @param rpro
	 * @param params
	 * @param <R>
	 * @return
	 */
	<R> List<R> queryForMap(Class<R> rpro, Map<String, Object> params);
	
	/**
	 * 获取对象
	 * @param <R>
	 * @param rpro
	 * @param params
	 * @return
	 */
	<R> List<R> queryForMapParamValue(Class<R> rpro,Map<String, ParamValue> params);

	/**
	 * 分页
	 * @param rpro
	 * @param param
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R, P> QueryResult<List<R>> queryForPage(Class<R> rpro, P param);

	/**
	 * 分页
	 * @param rpro
	 * @param param
	 * @param pageIndex
	 * @param pageSize
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R, P> QueryResult<List<R>> queryForPage(Class<R> rpro, P param,int pageIndex,int pageSize);

	/**
	 * 分页
	 * @param rpro
	 * @param params
	 * @param pageIndex
	 * @param pageSize
	 * @param <R>
	 * @return
	 */
	<R> QueryResult<List<R>> queryForPage(Class<R> rpro, Map<String, ParamValue> params,int pageIndex,int pageSize);

	/**
	 * 分页
	 * @param rpro
	 * @param param
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R, P> QueryVO<R> pageWrapper(Class<R> rpro, P param);

	/**
	 * 分页
	 * @param rpro
	 * @param param
	 * @param pageIndex
	 * @param pageSize
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	<R, P> QueryVO<R> pageWrapper(Class<R> rpro, P param,int pageIndex,int pageSize);

	/**
	 * 分页
	 * @param rpro
	 * @param params
	 * @param pageIndex
	 * @param pageSize
	 * @param <R>
	 * @return
	 */
	<R> QueryVO<R> pageWrapper(Class<R> rpro, Map<String, ParamValue> params,int pageIndex,int pageSize);
	
	/**
	 * List转化为List
	 * @param <R>
	 * @param result
	 * @param resultMapper
	 * @return
	 */
	<R>List<T> streamToList(List<R> result,Function<R,PK> resultMapper);
	
	/**
	 * List转化为List
	 * @param <R>
	 * @param <P>
	 * @param result
	 * @param resultMapper
	 * @param rpro
	 * @return
	 */
	<R,P>List<P> streamToList(List<R> result,Function<R,PK> resultMapper,Class<P> rpro);
	
	/**
	 * List转化为Map
	 * @param <R>
	 * @param <P>
	 * @param result
	 * @param resultMapper
	 * @param rpro
	 * @param valMapper
	 * @return
	 */
	<R,P>Map<PK,P> streamToMap(List<R> result,Function<R,PK> resultMapper,Class<P> rpro,Function<P,PK> valMapper);
	
	/**
	 * List转化为MapGroupby
	 * @param <R>
	 * @param <P>
	 * @param <KV>
	 * @param result
	 * @param resultMapper
	 * @param rpro
	 * @param valMapper
	 * @return
	 */
	<R,P,KV>Map<KV, List<P>> streamToMapGroupingBy(List<R> result,Function<R,PK> resultMapper,Class<P> rpro,Function<P,KV> valMapper);

	/**
	 * 全局检测是否唯一
	 * @param id
	 * @param fieldName
	 * @param value
	 * @param message
	 */
	void checkUnique(PK id,String fieldName,Object value,String message);
	
	/**
	 * 全局检测是否唯一
	 * @param id
	 * @param params
	 * @param message
	 */
	void checkUnique(PK id,Map<String,Object> params, String message);
	
}
