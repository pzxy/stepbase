package com.base.service.logger.entity;

import java.util.Date;

import com.base.core.framework.sql.entity.Base;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.Indexes;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldMediumText;

import lombok.ToString;

/**
 * @author start 
 */
@ToString
@Entity("operatorLog")
@Table("log_operator_log")
@TableDef(comment = "操作日志")
public class OperatorLogDO extends Base {

    private static final long serialVersionUID = 1L;

    public OperatorLogDO() {
    }

    @ColumnDef(indexes = @Indexes(normal = @Normal), length = 32, comment = "访问ID")
    private String accessId;

    @ColumnDef(comment = "应用名称")
    private String name;
    
    @ColumnDef(comment = "方法")
    private String method;

    @ColumnDef(comment = "URI")
    private String uri;

    @ColumnDef(comment = "请求参数",isNull = true)
    private String queryString;
    
    @ColumnDef(comment = "请求头")
    private JsonObject requestHeader;

    @ScriptConverter(FieldMediumText.class)
    @ColumnDef(comment = "请求体")
    private String requestContent;

    @ScriptConverter(FieldMediumText.class)
    @ColumnDef(comment = "响应日志内容")
    private String responseContent;

    @ColumnDef(indexes = @Indexes(unique = @Unique), length = 32, comment = "请求Id")
    private String requestId;

    @ColumnDef(comment = "请求IP地址")
    private String requestIp;
    
    @ColumnDef(length = 15, comment = "本机IP地址")
    private String localIp;

    @ColumnDef(comment = "服务器时间")
    private Date serverTime;

    @ColumnDef(comment = "所属工作机器索引")
    private Integer workerId;

    @ColumnDef(comment = "所在数据中心索引")
    private Integer dataCenterId;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public JsonObject getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(JsonObject requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public Integer getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(Integer dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}