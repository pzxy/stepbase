package com.base.core.head.ao;

import java.io.Serializable;
import java.util.List;

import com.gitee.magic.core.valid.annotation.Format;
import com.gitee.magic.core.valid.annotation.NotNull;
import com.gitee.magic.core.valid.annotation.Format.FormatType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class IDListAO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Schema(title="主键ID列表")
	@NotNull
    @Format(type=FormatType.JSONArrayLong)
    private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

}
