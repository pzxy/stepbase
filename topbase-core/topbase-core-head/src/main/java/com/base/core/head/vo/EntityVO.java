package com.base.core.head.vo;

import java.io.Serializable;
import java.util.Date;

import com.gitee.magic.core.converter.PropertyConverter;
import com.gitee.magic.framework.head.converter.TimeStampConverterEditor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author start 
 */
@Getter@Setter@ToString
public class EntityVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="主键Id")
	private Long id;

	@Schema(title="创建时间")
	@PropertyConverter(TimeStampConverterEditor.class)
	private Date createdDate;

	@Schema(title="修改时间")
	@PropertyConverter(TimeStampConverterEditor.class)
	private Date modifiedDate;

}
