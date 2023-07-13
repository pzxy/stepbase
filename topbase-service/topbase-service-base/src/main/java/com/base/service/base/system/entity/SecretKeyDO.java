package com.base.service.base.system.entity;

import java.util.Date;

import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;

import lombok.ToString;

/**
 * @author start 
 */
@ToString
@Entity("secretKey")
@Table("sys_secret_key")
@TableDef(comment = "密钥")
public class SecretKeyDO extends Base {

    private static final long serialVersionUID = 1L;

    public SecretKeyDO() {
    }

    @ColumnDef(indexes = @Indexes(unique = @Unique), length = 32, comment = "访问ID")
    private String accessId;

    @ColumnDef(length = 64, comment = "密钥")
    private String accessKey;

    @ColumnDef(comment = "类型")
    private Integer type;

    @ColumnDef(comment = "失效时间")
    private Date invalidTime;

    @ColumnDef(comment = "备注")
    private String remark;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
