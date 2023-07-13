package com.base.component.alibaba.aliyun.oss;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.base.component.upload.service.BaseUploadService;
import com.base.core.head.vo.UploadStsVO;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.head.utils.TimeUtils;

/**
 * @author start 
 */
@Service
public class UploadOssServiceImpl implements BaseUploadService {
	
	@Value("${aliyun.oss.endpoint}")
	public String endpoint;
	@Value("${aliyun.oss.bucketName}")
	public String bucketName;

	@Autowired
	private Sts sts;
	@Autowired
	private OssService oss;

	@Override
	public UploadStsVO sts(String roleSessionName) {
		Credentials cred=sts.doAuth(roleSessionName);
		UploadStsVO result = new UploadStsVO();
		result.setEndPoint(endpoint);
		result.setBucketName(bucketName);
		result.setAccessKeyId(cred.getAccessKeyId());
		result.setAccessKeySecret(cred.getAccessKeySecret());
		result.setSecurityToken(cred.getSecurityToken());
		SimpleDateFormat df = new SimpleDateFormat(TimeUtils.YYYYMMDDTHHMMSS_F_Z);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			result.setExpiration(df.parse(cred.getExpiration()));
		} catch (ParseException e) {
			throw new ApplicationException(e);
		}
		return result;
	}

	@Override
	public boolean doesObjectExist(String key) {
		return oss.doesObjectExist(endpoint, bucketName, key);
	}

	@Override
	public void putObject(String key, InputStream inputStream) {
		oss.putObject(endpoint, bucketName, key, inputStream);
	}
	
	@Override
	public void putObject(String key, File file) {
		oss.putObject(endpoint, bucketName, key, file);
	}

	@Override
	public void deleteObject(String key) {
		oss.deleteObject(endpoint, bucketName, key);
	}

	@Override
	public void getObject(String key, File file) {
		oss.getObject(endpoint, bucketName, key,file);
	}
	
	@Override
	public String getUrl(String key) {
		return oss.getUrl(endpoint, bucketName, key);
	}

	@Override
	public String getUrl(String key, Boolean isExpiration) {
		return oss.getUrl(endpoint, bucketName, key);
	}

	@Override
	public String getImageSnapshotUrl(String key, Integer width, Integer height) {
		return oss.getImageSnapshotUrl(endpoint, bucketName, key,width,height);
	}

	@Override
	public String getVideoSnapshotUrl(String key, Integer width, Integer height) {
		return oss.getVideoSnapshotUrl(endpoint, bucketName, key,width,height);
	}
	
}
