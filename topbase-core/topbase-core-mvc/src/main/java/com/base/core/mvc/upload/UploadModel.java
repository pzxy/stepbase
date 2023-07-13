package com.base.core.mvc.upload;

/**
 * @author start
 */
public class UploadModel {
	
	/**
	 * 主键 
	 */
	private String key;
	
	/**
	 * 存储的名称
	 */
	private String name;
	
	/**
	 * 文件签名值
	 */
	private String signature;
	
	/**
	 * 文件大小
	 */
	private Integer length;
	
	/**
	 * 文件是否加密存储
	 */
	private Boolean encrypt;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public Integer getLength() {
		return length;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public Boolean getEncrypt() {
		return encrypt;
	}
	
	public void setEncrypt(Boolean encrypt) {
		this.encrypt = encrypt;
	}
	
}
