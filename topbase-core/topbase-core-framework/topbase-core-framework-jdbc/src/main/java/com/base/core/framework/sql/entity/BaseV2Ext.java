package com.base.core.framework.sql.entity;

import com.gitee.magic.jdbc.persistence.annotation.MappedSuperclass;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;

/**
 * 继承逻辑删除的类字段不允许设置单独的唯一约束,需要建立联合索引
 * @TableDef(comment = "表名", indexes = @IndexesUnion(unique = @UniqueUnion(fields = {"字段1","deleted"})))
 * @author start
 *
 */
@MappedSuperclass
public class BaseV2Ext extends BaseV1Ext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ColumnDef(comment = "删除标记(0:未删除,非零:删除)")
	private Long deleted;

	public Long getDeleted() {
		return deleted;
	}

	public void setDeleted(Long deleted) {
		this.deleted = deleted;
	}
	
}
