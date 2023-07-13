package com.base.core.framework.mybatis.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author start
 */
public class BaseV1Ext extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableField("created_date")
	private Date createdDate;

	@TableField("modified_date")
	private Date modifiedDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
