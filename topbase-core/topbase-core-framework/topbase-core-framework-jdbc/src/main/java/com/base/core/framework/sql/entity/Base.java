package com.base.core.framework.sql.entity;

import java.io.Serializable;

import com.gitee.magic.jdbc.persistence.annotation.Id;
import com.gitee.magic.jdbc.persistence.annotation.MappedSuperclass;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldLong;

/**
 * @author start
 */
@MappedSuperclass
public abstract class Base implements Serializable{

	private static final long serialVersionUID = 43948582629964283L;

	@ColumnDef(comment="主键")
	@Id
	@ScriptConverter(FieldLong.class)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Base other = (Base) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
}