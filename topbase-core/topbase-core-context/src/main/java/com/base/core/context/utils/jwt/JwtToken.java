package com.base.core.context.utils.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.codec.Md5;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.TimeUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

/**
 * @author start
 * Created by zsh on 2022/3/9
 */
public class JwtToken {

	private String secret;

	public JwtToken(String secret) {
		this.secret = Md5.md5(secret);
	}
	
	public String sign(String data, Date expiration) {
		PayloadDTO dto = new PayloadDTO();
		dto.setSub("zsh");
		dto.setIat(System.currentTimeMillis());
		dto.setExp(expiration.getTime());
		dto.setJti(UUID.randomUUID().toString());
		dto.setPayload(data);
		try {
			JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
			Payload payload = new Payload(ConverterEditorUtils.converter(dto, JsonObject.class).toString());
			JWSObject jwsObject = new JWSObject(jwsHeader, payload);
			JWSSigner jwsSigner = new MACSigner(secret);
			jwsObject.sign(jwsSigner);
			return jwsObject.serialize();
		} catch (KeyLengthException e) {
			throw new ApplicationException(e);
		} catch (JOSEException e) {
			throw new ApplicationException(e);
		}
	}
	
	public Boolean isVerify(String token) {
		JWSObject jwsObject;
		try {
			jwsObject = JWSObject.parse(token);
		} catch (ParseException e) {
			return false;
		}
		try {
			JWSVerifier jwsVerifier = new MACVerifier(secret);
			return jwsObject.verify(jwsVerifier);
		} catch (JOSEException e) {
			return false;
		}
	}
	
	public PayloadDTO payload(String token) {
		JWSObject jwsObject;
		try {
			jwsObject = JWSObject.parse(token);
		} catch (ParseException e) {
			throw new BusinessException(e);
		}
		String payload = jwsObject.getPayload().toString();
		return ConverterEditorUtils.restoreObject(PayloadDTO.class, payload);
	}
	
	public Boolean isExpire(String token) {
		PayloadDTO payloadDto = payload(token);
		return payloadDto.getExp() < System.currentTimeMillis();
	}

	public static void main(String[] args) {
		JwtToken jwtTokenService = new JwtToken("test");
		String token=jwtTokenService.sign("2342314", TimeUtils.getDate(3));
		System.out.println("是否有效:"+jwtTokenService.isVerify(token+"34234"));
		System.out.println("是否过期:"+jwtTokenService.isExpire(token));
		
		PayloadDTO dto=jwtTokenService.payload(token);
		System.out.println(dto);

	}

}
