package com.base.service.system.entity;

import com.base.core.framework.sql.entity.BaseV1Ext;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.IndexesUnion;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.UniqueUnion;

import lombok.ToString;

/**
 * @author start 
 */
@ToString
@Entity("roleAuthRelation")
@Table("sys_role_auth_relation")
@TableDef(comment = "角色授权关系", 
indexes = @IndexesUnion(unique = @UniqueUnion(fields = {"roleId","authId"})))
public class RoleAuthRelationDO extends BaseV1Ext {

	private static final long serialVersionUID = 1L;
	
	public RoleAuthRelationDO(){}

    @ColumnDef(indexes = @Indexes(normal = @Normal), comment = "角色ID")
	private Long roleId;

    @ColumnDef(indexes = @Indexes(normal = @Normal), comment = "授权ID")
	private Long authId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	
}
