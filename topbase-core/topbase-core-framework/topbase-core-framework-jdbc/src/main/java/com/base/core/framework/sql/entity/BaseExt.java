package com.base.core.framework.sql.entity;

import java.util.Date;

import com.gitee.magic.core.annotations.Column;
import com.gitee.magic.jdbc.persistence.annotation.MappedSuperclass;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDateTime;

/**
 * @author start
 */
@MappedSuperclass
public class BaseExt extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ColumnDef(defaultValue = "CURRENT_TIMESTAMP",comment = "创建时间")
	@Column("created_date")
	@ScriptConverter(FieldDateTime.class)
	private Date createdDate;

	@ColumnDef(defaultValue = "CURRENT_TIMESTAMP",more = "ON UPDATE CURRENT_TIMESTAMP",comment = "最后修改时间")
	@Column("modified_date")
	@ScriptConverter(FieldDateTime.class)
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
