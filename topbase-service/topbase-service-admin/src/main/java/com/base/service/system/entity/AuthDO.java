package com.base.service.system.entity;

import com.base.core.framework.sql.entity.BaseV1Ext;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;

import lombok.ToString;

/**
 * @author start 
 */
@ToString
@Entity("auth")
@Table("sys_auth")
@TableDef(comment = "授权")
public class AuthDO extends BaseV1Ext {

	private static final long serialVersionUID = 1L;

	public AuthDO() {
	}

	@ColumnDef(comment = "接口名称")
	private String name;
	
	@ColumnDef(indexes = @Indexes(normal = @Normal), comment = "父ID")
	private Long parentId;

	@ColumnDef(indexes = @Indexes(unique = @Unique),length = 100, comment = "操作码")
	private String action;

	@ColumnDef(comment = "接口地址",isNull = true)
	private String uri;

	@ColumnDef(comment = "请求方法")
	private String method;

	@ColumnDef(indexes = @Indexes(normal = @Normal),comment = "授权值")
	private Integer value;
	
	@ColumnDef(comment = "排序值")
	private Integer sort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
