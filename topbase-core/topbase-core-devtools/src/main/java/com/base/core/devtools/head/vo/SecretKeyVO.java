package com.base.core.devtools.head.vo;

import java.io.Serializable;
import java.util.Date;

import com.gitee.magic.core.converter.PropertyConverter;
import com.gitee.magic.framework.head.converter.DateTimeConverterEditor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * @author start 
 */
@ToString
public class SecretKeyVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="访问ID")
    private String accessId;

    @Schema(title="密钥")
    private String accessKey;

    @Schema(title="类型")
    private Integer type;

    @Schema(title="失效时间")
    @PropertyConverter(DateTimeConverterEditor.class)
    private Date invalidTime;

    @Schema(title="备注")
    private String remark;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
