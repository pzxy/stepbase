package com.base.core.devtools.head.vo;

import java.io.Serializable;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class ConfigVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="全名")
	private String fullName;
	
    @Schema(title="应用名称(spring.application.name)")
	private String systemName;

    @Schema(title="版本号(magic.system.version)")
	private String systemVersion;

    @Schema(title="数据中心索引(magic.system.balanced_datacenter_id)")
	private Integer balancedDataCenterId;

    @Schema(title="工作组索引(magic.system.balanced_worker_id)")
	private Integer balancedWorkerId;
    
    @Schema(title="请求Body大小(magic.http.max_body_text_size)")
	private Long maxBodyTextSize;

    @Schema(title="本机IP")
	private String localIpByNetcard;
    
    @Schema(title="请求IP")
    private String requestIp;

    @Schema(title="活跃用户数")
	private Long activeUsers;

    @Schema(title="配置参数")
    private Map<String, String> props;
    
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public Integer getBalancedDataCenterId() {
		return balancedDataCenterId;
	}

	public void setBalancedDataCenterId(Integer balancedDataCenterId) {
		this.balancedDataCenterId = balancedDataCenterId;
	}

	public Integer getBalancedWorkerId() {
		return balancedWorkerId;
	}

	public void setBalancedWorkerId(Integer balancedWorkerId) {
		this.balancedWorkerId = balancedWorkerId;
	}

	public String getLocalIpByNetcard() {
		return localIpByNetcard;
	}

	public void setLocalIpByNetcard(String localIpByNetcard) {
		this.localIpByNetcard = localIpByNetcard;
	}
	
	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public Long getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(Long activeUsers) {
		this.activeUsers = activeUsers;
	}

	public Long getMaxBodyTextSize() {
		return maxBodyTextSize;
	}

	public void setMaxBodyTextSize(Long maxBodyTextSize) {
		this.maxBodyTextSize = maxBodyTextSize;
	}

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}
	
}
