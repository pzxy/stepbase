package com.base.core.context.utils.jwt;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  @author start
 */
@Getter@Setter@ToString
public class PayloadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title="主题")
	private String sub;

    @Schema(title="签发时间")
    private Long iat;

    @Schema(title="过期时间")
    private Long exp;

    @Schema(title="JWT ID")
    private String jti;

    @Schema(title="内容")
    private String payload;
    
}
