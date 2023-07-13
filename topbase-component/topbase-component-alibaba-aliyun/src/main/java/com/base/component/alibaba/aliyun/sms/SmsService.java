package com.base.component.alibaba.aliyun.sms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;

/**
 * @author start 
 */
@Component
public class SmsService {

	private static final String OK="OK";
	private static final String CODE="Code";

	@Value("${aliyun.ram.accessKeyId}")
	public String accessKeyId;
	@Value("${aliyun.ram.accessKeySecret}")
	public String secretAccessKey;
	@Value("${aliyun.regionId:cn-hangzhou}")
	public String regionId;

	public void sendSms(String template, List<String> mobiles, JsonObject p, String signName) {
		Map<String,String> params=new HashMap<>(4);
		params.put("TemplateCode", template);
		params.put("PhoneNumbers", StringUtils.listToString(mobiles));
		params.put("TemplateParam", p.toString());
		params.put("SignName", signName);
		sendSms(params);
	}
	
	public void sendSms(Map<String,String> params) {
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secretAccessKey);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", regionId);
		for(String key:params.keySet()) {
			request.putQueryParameter(key, params.get(key));
		}
		CommonResponse response;
		try {
			response = client.getCommonResponse(request);
		} catch (ClientException e) {
			throw new ApplicationException(e);
		}
		JsonObject json = new JsonObject(response.getData());
		if (!OK.equals(json.getString(CODE))) {
			throw new ApplicationException(response.getData());
		}
	}

}
