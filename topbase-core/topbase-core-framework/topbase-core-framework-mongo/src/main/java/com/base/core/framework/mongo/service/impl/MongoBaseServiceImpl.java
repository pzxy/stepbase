package com.base.core.framework.mongo.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.base.core.framework.mongo.dao.MongoDao;
import com.base.core.framework.mongo.entity.MongoBase;
import com.base.core.framework.mongo.service.MongoBaseService;
import com.gitee.magic.core.utils.SnowflakeIdWorker;
import com.gitee.magic.framework.head.vo.QueryVO;

/**
 * @author start 
 */
public class MongoBaseServiceImpl<T extends MongoBase, PK extends Serializable> implements MongoBaseService<T, PK> {

	private final MongoDao<T,PK> mongoDao;
	
	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	
	public MongoBaseServiceImpl(MongoDao<T,PK> mongoDao){
		this.mongoDao=mongoDao;
	}

	@Override
	public void save(T entity) {
		if(entity.getId()==null) {
			entity.setId(snowflakeIdWorker.nextId());
		}else {
			
		}
		mongoDao.save(entity);
	}
	
	@Override
	public void remove(PK pk) {
		mongoDao.deleteById(pk);
	}
	
	public <R>QueryVO<R> queryForPage(List<R> data,int total){
		QueryVO<R> qr=new QueryVO<>();
		qr.setData(data);
		qr.setTotal(total);
		return qr;
	}
	
}
