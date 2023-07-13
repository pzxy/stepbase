package com.base.core.devtools.head.ao;

import java.io.Serializable;

import com.gitee.magic.core.valid.annotation.Length;
import com.gitee.magic.core.valid.annotation.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class SignAO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="签名密钥")
	@NotNull
	@Length
	private String accessKey;

	@Schema(title="数据内容")
	private String content;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
