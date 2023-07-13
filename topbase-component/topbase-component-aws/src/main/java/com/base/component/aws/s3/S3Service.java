package com.base.component.aws.s3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.gitee.magic.core.exception.ApplicationException;

/**
 * @author start 
 */
@Service
public class S3Service {

	@Value("${aws.s3.accessid}")
	private String accessId;
	@Value("${aws.s3.accesskey}")
	private String accessKey;
	
	public AmazonS3 getInstance(String regionName) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessId, accessKey);
        return AmazonS3ClientBuilder.standard()
        		.withCredentials(new AWSStaticCredentialsProvider(credentials))
        		.withRegion(Regions.fromName(regionName))
                .build();
	}
	
	/**
	 * 判断是否存在当前文件
	 * @param regionName
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public boolean doesObjectExist(String regionName,String bucketName,String key) {
		AmazonS3 s3 = getInstance(regionName);
		return s3.doesObjectExist(bucketName, key);
	}
	
	/**
	 * 上传文件
	 * @param regionName
	 * @param bucketName
	 * @param key
	 * @param file
	 */
	public void putObject(String regionName,String bucketName, String key,File file) {
		AmazonS3 s3 = getInstance(regionName);
		s3.putObject(new PutObjectRequest(bucketName, key, file));
	}
	
	public void putObject(String regionName,String bucketName,String key,InputStream inputStream) {
		AmazonS3 s3 = getInstance(regionName);
		ObjectMetadata metadata=new ObjectMetadata();
		try {
			metadata.setContentLength(inputStream.available());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		s3.putObject(bucketName, key, inputStream,metadata);
	}
	
	public void deleteObject(String regionName,String bucketName, String key) {
		AmazonS3 s3 = getInstance(regionName);
		s3.deleteObject(bucketName, key);
	}
	
	public void getObject(String regionName,String bucketName, String key,File file) {
		AmazonS3 s3 = getInstance(regionName);
		s3.getObject(new GetObjectRequest(bucketName,key), file);
	}

	/**
	 * 生成下载地址
	 * 
	 * @param key
	 * @return
	 */
	public String getUrl(String regionName,String bucketName,String key,Boolean isExpiration) {
		if(isExpiration) {
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, key);
			Date expiration = new Date(System.currentTimeMillis() + 1000 * 10);
			// 设置有效期
			urlRequest.setExpiration(expiration);
			URL url = getInstance(regionName).generatePresignedUrl(urlRequest);
			return url.toString();
		}else {
			URL url = getInstance(regionName).getUrl(bucketName, key);
			return url.toString();
		}
	}
	
	/**
	 * STS临时授权用BasicSessionCredentials封装授权
	 * @return
	 */
	public Credentials getStsCredentials(String regionName) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessId, accessKey);
		AWSSecurityTokenService client = AWSSecurityTokenServiceClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.fromName(regionName))
				.build();
		GetSessionTokenRequest sessionTokenRequest = new GetSessionTokenRequest();
		sessionTokenRequest.setDurationSeconds(900);
		GetSessionTokenResult sessionTokenResult =
				client.getSessionToken(sessionTokenRequest);
		Credentials sessionCreds = sessionTokenResult.getCredentials();
		return sessionCreds;
	}

}
