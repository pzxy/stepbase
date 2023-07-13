package com.base.core.framework.mongo.entity;

import org.springframework.data.annotation.Id;

/**
 * @author start 
 */
public class MongoBase {

	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
