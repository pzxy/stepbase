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
@Entity("userRoleRelation")
@Table("sys_user_role_relation")
@TableDef(comment = "用户角色关系", 
indexes = @IndexesUnion(unique = @UniqueUnion(fields = {"userId","roleId"})))
public class UserRoleRelationDO extends BaseV1Ext {

	private static final long serialVersionUID = 1L;
	
	public UserRoleRelationDO(){}

    @ColumnDef(indexes = @Indexes(normal = @Normal), comment = "用户ID")
	private Long userId;

	@ColumnDef(indexes = @Indexes(normal = @Normal), comment = "角色ID")
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
