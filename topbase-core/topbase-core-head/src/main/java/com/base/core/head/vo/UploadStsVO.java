package com.base.core.head.vo;
import java.io.Serializable;
import java.util.Date;

import com.gitee.magic.core.converter.PropertyConverter;
import com.gitee.magic.framework.head.converter.TimeStampConverterEditor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class UploadStsVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="所属区域")
	private String endPoint;
	
	@Schema(title="存储桶名称")
	private String bucketName;

	@Schema(title="访问ID")
	private String accessKeyId;

	@Schema(title="访问密钥")
	private String accessKeySecret;

	@Schema(title="安全Token")
	private String securityToken;

	@Schema(title="有效时间")
	@PropertyConverter(TimeStampConverterEditor.class)
	private Date expiration;
	
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
}