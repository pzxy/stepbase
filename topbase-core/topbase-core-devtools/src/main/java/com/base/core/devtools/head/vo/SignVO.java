package com.base.core.devtools.head.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class SignVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="请求内容签名值")
	private String signature;

    @Schema(title="请求内容签名值再URL编码")
	private String signatureUrl;

    @Schema(title="请求内容Base64签名值编码")
	private String signatureBase64;

    @Schema(title="请求内容Base64签名值再URL编码")
	private String signatureBase64Url;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureUrl() {
		return signatureUrl;
	}

	public void setSignatureUrl(String signatureUrl) {
		this.signatureUrl = signatureUrl;
	}

	public String getSignatureBase64() {
		return signatureBase64;
	}

	public void setSignatureBase64(String signatureBase64) {
		this.signatureBase64 = signatureBase64;
	}

	public String getSignatureBase64Url() {
		return signatureBase64Url;
	}

	public void setSignatureBase64Url(String signatureBase64Url) {
		this.signatureBase64Url = signatureBase64Url;
	}

}
