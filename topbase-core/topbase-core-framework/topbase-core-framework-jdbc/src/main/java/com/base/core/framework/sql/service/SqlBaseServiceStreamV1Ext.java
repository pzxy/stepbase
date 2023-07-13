package com.base.core.framework.sql.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.base.core.framework.sql.entity.BaseV1Ext;
import com.base.core.head.ao.PageAO;
import com.base.core.head.vo.EntityVO;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start
 */
public interface SqlBaseServiceStreamV1Ext<T extends BaseV1Ext, VO extends EntityVO> extends SqlBaseService<T, Long> {

	/**
	 * collectorsToMap
	 * @param <R>
	 * @param result
	 * @param resultMapper
	 * @return
	 */
	<R> Map<Long, VO> streamToMap(List<R> result, Function<R, Long> resultMapper);
	
	/**
	 * 分组
	 * @param <R>
	 * @param <KV>
	 * @param result
	 * @param resultMapper
	 * @param valMapper
	 * @return
	 */
	<R,KV> Map<KV, List<VO>> streamToMapGroupingBy(List<R> result, Function<R, Long> resultMapper,Function<VO,KV> valMapper);
	
	/**
	 * 填充
	 * @param list
	 */
	void fill(List<VO> list);

	/**
	 * 分页
	 * @param <P>
	 * @param ao
	 * @return
	 */
	<P extends PageAO> QueryVO<VO> page(P ao);

}
