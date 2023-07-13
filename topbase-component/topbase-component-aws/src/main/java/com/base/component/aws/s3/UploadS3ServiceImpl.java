package com.base.component.aws.s3;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.securitytoken.model.Credentials;
import com.base.component.upload.service.BaseUploadService;
import com.base.core.head.vo.UploadStsVO;

/**
 * @author start 
 */
@Service
public class UploadS3ServiceImpl implements BaseUploadService {
	
	@Value("${aws.s3.bucketName}")
	private String bucketName;
	@Value("${aws.s3.regionName:us-east-1}")
	private String regionName;
	
	@Autowired
	private S3Service s3Service;

	@Override
	public UploadStsVO sts(String roleSessionName) {
		Credentials cred=s3Service.getStsCredentials(regionName);
		UploadStsVO result=new UploadStsVO();
		result.setEndPoint(regionName);
		result.setBucketName(bucketName);
		result.setAccessKeyId(cred.getAccessKeyId());
		result.setAccessKeySecret(cred.getSecretAccessKey());
		result.setSecurityToken(cred.getSessionToken());
		result.setExpiration(cred.getExpiration());
		return result;
	}

	@Override
	public boolean doesObjectExist(String key) {
		return s3Service.doesObjectExist(regionName,bucketName,key);
	}

	@Override
	public void putObject(String key, InputStream inputStream) {
		s3Service.putObject(regionName,bucketName, key, inputStream);
	}

	@Override
	public void putObject(String key, File file) {
		s3Service.putObject(regionName,bucketName,key, file);
	}

	@Override
	public void deleteObject(String key) {
		s3Service.deleteObject(regionName,bucketName,key);
	}

	@Override
	public void getObject(String key, File file) {
		s3Service.getObject(regionName, bucketName, key, file);
	}
	
	@Override
	public String getUrl(String key) {
		return s3Service.getUrl(regionName,bucketName,key, true);
	}

	@Override
	public String getUrl(String key, Boolean isExpiration) {
		return s3Service.getUrl(regionName,bucketName,key, isExpiration);
	}

	@Override
	public String getImageSnapshotUrl(String key, Integer width, Integer height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVideoSnapshotUrl(String key, Integer width, Integer height) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
