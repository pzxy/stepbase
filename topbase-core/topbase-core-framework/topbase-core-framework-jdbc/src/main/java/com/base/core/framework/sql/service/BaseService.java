package com.base.core.framework.sql.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.framework.base.result.QueryResult;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public interface BaseService<T extends Base,PK extends Serializable>{

	/**
	 * 判断ID为null 进行persist,merge
	 * @param entity
	 * @return
	 */
	long save(T entity);
	
	/**
	 * P 转化 T
	 * @param <P>
	 * @param pk
	 * @param param
	 * @return
	 */
	<P>T to(PK pk,P param);

	/**
	 * 保存更新通用方法
	 * @param pk
	 * @param param
	 * @param <P>
	 * @return
	 */
	<P>Long saveForUpdate(PK pk,P param);

	/**
	 * 批量新增
	 * @param entitys
	 */
	void saveBatch(List<T> entitys);

	/**
	 * 根据ID删除对象
	 * @param entity
	 * @return
	 */
	long remove(T entity);

	/**
	 * 根据主键加载
	 * @param id
	 * @return
	 */
	T load(PK id);

	/**
	 * 获取列表数据的第一条记录，只能为一条数据
	 * @param collections
	 * @param <R>
	 * @return
	 */
	<R>R get(List<R> collections);

	/**
	 * 获取列表数据的第一条记录
	 * @param collections
	 * @param flag	true:表示只能为一条数据
	 * @param <R>
	 * @return
	 */
	<R>R get(List<R> collections,Boolean flag);
	
	/**
	 * Map转对象
	 * @param prototype
	 * @param data
	 * @return
	 */
	<R>R result(Class<R> prototype,Map<String,Object> data);
	
	/**
	 * List<Map>转对象列表
	 * @param prototype
	 * @param datas
	 * @return
	 */
	<R>List<R> result(Class<R> prototype,List<Map<String,Object>> datas);

	/**
	 * 分页List<Map>转对象列表
	 * @param prototype
	 * @param datas
	 * @param totalCount
	 * @param <R>
	 * @return
	 */
	<R>QueryResult<List<R>> queryResult(Class<R> prototype,List<Map<String,Object>> datas,int totalCount);

	/**
	 * 分页List<Map>转对象列表
	 * @param results
	 * @param totalCount
	 * @param <R>
	 * @return
	 */
	<R>QueryResult<List<R>> queryResult(List<R> results,int totalCount);


	/**
	 * 分页List<Map>转对象列表
	 * @param prototype
	 * @param datas
	 * @param totalCount
	 * @param <R>
	 * @return
	 */
	<R>QueryVO<R> queryVO(Class<R> prototype,List<Map<String,Object>> datas,int totalCount);

	/**
	 * 分页List<Map>转对象列表
	 * @param results
	 * @param totalCount
	 * @param <R>
	 * @return
	 */
	<R>QueryVO<R> queryVO(List<R> results,int totalCount);
	
}
