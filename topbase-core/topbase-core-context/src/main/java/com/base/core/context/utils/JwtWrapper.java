package com.base.core.context.utils;

import com.base.core.context.utils.jwt.JwtToken;
import com.base.core.context.utils.jwt.PayloadDTO;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.TimeUtils;

/**
 * @author start 
 */
public class JwtWrapper {
	
	private JwtToken jwt;
	private Integer second;
	
	public JwtWrapper(String secret) {
		this.jwt=new JwtToken(secret);
		this.second=60*60*24*7;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public String getToken(Object jwtObject) {
		String playload=ConverterEditorUtils.converter(jwtObject, JsonObject.class).toString();
		return this.jwt.sign(playload, TimeUtils.getSecond(second));
	}
	
	public <T>T verifyToken(String authorization,Class<T> prototype) {
		return verifyToken(authorization,prototype,false);
	}
	
	public <T>T verifyToken(String authorization,Class<T> prototype,Boolean isExpired) {
		if(StringUtils.isEmpty(authorization)) {
			throw new BusinessException(BaseCode.CODE_401);
		}
		if(!this.jwt.isVerify(authorization)) {
			throw new BusinessException(BaseCode.CODE_401);
		}
		if(isExpired) {
			if(this.jwt.isExpire(authorization)) {
				throw new BusinessException(BaseCode.CODE_401);
			}
		}
		PayloadDTO dto=jwt.payload(authorization);
		JsonObject json=new JsonObject(dto.getPayload());
		return ConverterEditorUtils.restoreObject(prototype, json);
	}
	
}
