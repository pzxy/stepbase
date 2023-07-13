package com.base.service.base.logger.entity;

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
@Entity("loginLog")
@Table("log_login_log")
@TableDef(comment = "登陆日志")
public class LoginLogDO extends BaseV1Ext {

    private static final long serialVersionUID = 1L;

    public LoginLogDO() {
    }

    @ColumnDef(indexes = @Indexes(normal = @Normal), comment = "用户ID")
    private Long userId;

    @ColumnDef(comment = "用户类型")
    private String userType;

    @ColumnDef(indexes = @Indexes(unique = @Unique), length = 32, comment = "访问ID")
    private String accessId;

    @ColumnDef(length = 64, comment = "访问Key")
    private String accessKey;

    @ColumnDef(comment = "来源")
    private String source;

    @ColumnDef(comment = "请求IP")
    private String requestIp;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

}
