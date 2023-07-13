package com.base.service.blockchain.synchash.entity;

import com.base.core.framework.sql.entity.BaseV1Ext;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author start
 */
@Getter@Setter
@ToString
@Entity("syncHash")
@Table("has_sync_hash")
@TableDef(comment = "HASH同步信息")
public class SyncHashDO extends BaseV1Ext {

	private static final long serialVersionUID = 1L;
	
	public SyncHashDO(){}

    @ColumnDef(indexes = @Indexes(unique = @Unique), comment = "hash")
	private String hash;

    @ColumnDef(indexes = @Indexes(normal = @Normal),comment = "Bean")
	private String bean;

    @ColumnDef( comment = "状态")
	private SyncStatusEnum syncStatus;

}
