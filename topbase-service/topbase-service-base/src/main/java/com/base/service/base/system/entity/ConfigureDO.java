package com.base.service.base.system.entity;

import com.base.core.framework.sql.entity.Base;
import com.base.core.head.enums.ValueTypeEnum;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldText;

import lombok.ToString;

/**
 * @author start 
 */
@ToString
@Entity("configure")
@Table("sys_configure")
@TableDef(comment = "配置")
public class ConfigureDO extends Base {

    private static final long serialVersionUID = 1L;

    public ConfigureDO() {
    }

    @ColumnDef(indexes = @Indexes(normal = @Normal), comment = "组ID")
    private Integer groupId;

    @ColumnDef(indexes = @Indexes(unique = @Unique), length = 50, comment = "编号")
    private String code;

    @ScriptConverter(FieldText.class)
    @ColumnDef(comment = "值")
    private String value;

    @ColumnDef(comment = "值类型")
    private ValueTypeEnum type;

    @ColumnDef(comment = "备注")
    private String remark;

    @ColumnDef(comment = "排序")
    private Integer sorted;

    @ColumnDef(comment = "是否禁用")
    private Boolean disable;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ValueTypeEnum getType() {
		return type;
	}

	public void setType(ValueTypeEnum type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSorted() {
		return sorted;
	}

	public void setSorted(Integer sorted) {
		this.sorted = sorted;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

}
