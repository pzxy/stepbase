package com.base.core.head.ao;

import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.core.valid.annotation.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class MessageBaseAO {

	@Schema(title="内容")
	@NotNull
	@NotEmpty
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
