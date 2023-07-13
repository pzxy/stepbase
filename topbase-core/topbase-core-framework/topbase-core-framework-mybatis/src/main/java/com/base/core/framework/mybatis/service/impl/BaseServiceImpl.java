package com.base.core.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.core.framework.mybatis.entity.BaseV1Ext;
import com.base.core.framework.mybatis.service.BaseService;

/**
 * @author start
 * @param <T>
 * @param <M>
 */
public class BaseServiceImpl<T extends BaseV1Ext,M extends BaseMapper<T>> 
	extends ServiceImpl<M,T> implements BaseService<T> {

}
