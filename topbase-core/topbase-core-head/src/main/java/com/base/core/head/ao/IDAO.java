package com.base.core.head.ao;

import java.io.Serializable;

import com.gitee.magic.core.valid.annotation.NotNull;
import com.gitee.magic.core.valid.annotation.number.LongValid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class IDAO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Schema(title="主键ID")
	@NotNull
	@LongValid
    private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}
