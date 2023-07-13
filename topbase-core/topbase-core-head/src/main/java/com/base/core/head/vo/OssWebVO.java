package com.base.core.head.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class OssWebVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="OssID")
	public String accessKeyId;
	
	@Schema(title="policy")
	private String policy;
	
	@Schema(title="签名")
	private String signature;
	
	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
