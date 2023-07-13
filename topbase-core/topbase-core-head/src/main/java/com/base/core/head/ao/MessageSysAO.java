package com.base.core.head.ao;

import com.gitee.magic.core.valid.annotation.Length;
import com.gitee.magic.core.valid.annotation.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class MessageSysAO extends MessageBaseAO {
	
	@Schema(title="标题")
	@NotNull
	@Length
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
