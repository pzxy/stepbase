package com.base.component.alibaba.aliyun.oss;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * @author start 
 */
@Component
public class Sts {

	private static final String STS_API_VERSION = "2015-04-01";
	private static final String REGION_CN_HANGZHOU = "cn-hangzhou";
	
	@Value("${aliyun.sts.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.sts.accessKeySecret}")
	private String accessKeySecret;
	
	@Value("${aliyun.sts.roleArn}")
	private String roleArn;
	@Value("${aliyun.sts.tokenExpireTime:3600}")
	private Long tokenExpireTime;
	@Value("${aliyun.sts.policyFile:data/osspolicy/all_policy.txt}")
	private String policyFile;
	
	public Credentials doAuth(String roleSessionName) {
		ClassPathResource resource=new ClassPathResource(policyFile);
		String policy;
		try {
			policy = new String(IoUtils.inputStreamConvertBytes(resource.getInputStream(), resource.getInputStream().available()));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		ProtocolType protocolType = ProtocolType.HTTPS;
		AssumeRoleResponse stsResponse;
		try {
			stsResponse = assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName,
					policy, protocolType, tokenExpireTime);
		} catch (ClientException e) {
			throw new ApplicationException(e);
		}
		return stsResponse.getCredentials();
	}
	
	private AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
			String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds)
			throws ClientException {

		// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
		IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);

		// 创建一个 AssumeRoleRequest 并设置请求参数
		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion(STS_API_VERSION);
		request.setSysMethod(MethodType.POST);
		request.setSysProtocol(protocolType);

		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(policy);
		request.setDurationSeconds(durationSeconds);

		// 发起请求，并得到response
		return client.getAcsResponse(request);
	}

}
