package com.base.core.framework.sql.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.base.core.framework.sql.dao.SqlBaseDao;
import com.base.core.framework.sql.entity.BaseV1Ext;
import com.base.core.framework.sql.service.SqlBaseServiceStreamV1Ext;
import com.base.core.head.ao.PageAO;
import com.base.core.head.vo.EntityVO;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public class SqlBaseServiceStreamImplV1Ext<T extends BaseV1Ext,VO extends EntityVO> 
extends SqlBaseServiceImplV1Ext<T,Long> implements SqlBaseServiceStreamV1Ext<T,VO> {
	
	@SuppressWarnings("unused")
	private final SqlBaseDao<T,Long> baseDao;
	private final Class<VO> prototype;
	
	public SqlBaseServiceStreamImplV1Ext(SqlBaseDao<T,Long> baseDao,Class<VO> prototype){
		super(baseDao);
		this.baseDao=baseDao;
		this.prototype=prototype;
	}
	
	@Override
	public <R>Map<Long, VO> streamToMap(List<R> result, Function<R, Long> resultMapper) {
		List<VO> list=streamToList(result,resultMapper,this.prototype);
		fill(list);
		return list.stream().collect(Collectors.toMap(VO::getId, Function.identity()));
	}

	@Override
	public <R,KV> Map<KV, List<VO>> streamToMapGroupingBy(List<R> result, Function<R, Long> resultMapper,
			Function<VO, KV> valMapper) {
		List<VO> list=streamToList(result,resultMapper,this.prototype);
		fill(list);
		return list.stream().collect(Collectors.groupingBy(valMapper));
	}
	
	@Override
	public void fill(List<VO> list) {
	}

	@Override
	public <P extends PageAO> QueryVO<VO> page(P ao) {
		QueryVO<VO> result=pageWrapper(this.prototype, ao);
		if(result==null) {
			return null;
		}
		fill(result.getData());
		return result;
	}
	
}