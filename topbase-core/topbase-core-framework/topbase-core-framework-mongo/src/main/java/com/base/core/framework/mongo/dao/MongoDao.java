package com.base.core.framework.mongo.dao;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.base.core.framework.mongo.entity.MongoBase;

/**
 * @author start 
 */
public interface MongoDao<T extends MongoBase, PK extends Serializable> extends MongoRepository<T,PK> {

}
