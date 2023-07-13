package com.base.component.alibaba.aliyun.oss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.base.core.head.vo.OssWebVO;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * @author start 
 */
@Component
public class OssService {
	
	@Value("${aliyun.ram.accessKeyId}")
	public String accessKeyId;
	@Value("${aliyun.ram.accessKeySecret}")
	public String accessKeySecret;
	
	@Value("${aliyun.oss.policyFile:data/osspolicy/web_post_policy.txt}")
	private String policyFile;

	/**
	 * 判断OSS是否存在当前文件
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public boolean doesObjectExist(String endpoint,String bucketName,String key){
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			return oss.doesObjectExist(bucketName, key);
		}finally{
			oss.shutdown();
		}
	}
	
	public ObjectMetadata getObjectMetadata(String endpoint,String bucketName,String key){
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			return oss.getObjectMetadata(bucketName, key);
		}finally{
			oss.shutdown();
		}
	}
	
	public void putObject(String endpoint,String bucketName,String key, File file) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
			oss.putObject(putObjectRequest);
		}finally{
			oss.shutdown();
		}
	}

	/**
	 * 上传流
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @param is
	 */
	public void putObject(String endpoint,String bucketName,String key, InputStream is) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			oss.putObject(bucketName, key, is);
		}finally{
			oss.shutdown();
		}
	}

	/**
	 * 获取文件下载到本地
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @param file
	 */
	public void getObject(String endpoint,String bucketName,String key, File file) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			oss.getObject(new GetObjectRequest(bucketName, key), file);
		}finally{
			oss.shutdown();
		}
	}
	
	public void deleteObject(String endpoint,String bucketName,String key) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			oss.deleteObject(bucketName, key);
		}finally{
			oss.shutdown();
		}
	}

	/**
	 * 获取下载的流
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public InputStream download(String endpoint,String bucketName,String key) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			OSSObject ossObject = oss.getObject(bucketName, key);
			return ossObject.getObjectContent();
		}finally{
			oss.shutdown();
		}
	}
	
	/**
	 * 生成下载地址
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public String getUrl(String endpoint,String bucketName,String key){
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
			 // 过期时间
		    Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10*6);
		    URL signedUrl = oss.generatePresignedUrl(bucketName, key,expiration);
		    return signedUrl.toString();
		}finally{
			oss.shutdown();
		}
	}
	
	/**
	 * 剪切图片
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @param width
	 * @param height
	 * @return
	 */
	public String getImageSnapshotUrl(String endpoint,String bucketName,String key, Integer width, Integer height){
		String style = String.format("image/resize,m_fill,w_%s,h_%s", width, height);
		return getSnapshotUrl(endpoint,bucketName,key,style);
	}
	
	/**
	 * 剪切视频帧
	 * @param endpoint
	 * @param bucketName
	 * @param key
	 * @param width
	 * @param height
	 * @return
	 */
	public String getVideoSnapshotUrl(String endpoint,String bucketName,String key, Integer width, Integer height){
	    String style = String.format("video/snapshot,t_1000,f_jpg,w_%s,h_%s,m_fast", width, height);
		return getSnapshotUrl(endpoint,bucketName,key,style);
	}
	
	public String getSnapshotUrl(String endpoint,String bucketName,String key,String style) {
		OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		try{
		    Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10*6);
		    GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
		    req.setExpiration(expiration);
		    req.setProcess(style);
		    URL signedUrl = oss.generatePresignedUrl(req);
		    return signedUrl.toString();
		}finally{
			oss.shutdown();
		}
	}
	
	/**
	 * Web直传
	 */
	public OssWebVO web() {
		ClassPathResource resource=new ClassPathResource(policyFile);
		String policy;
		try {
			policy = new String(IoUtils.inputStreamConvertBytes(resource.getInputStream(), resource.getInputStream().available()));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
        String encodePolicy = Base64.getEncoder().encodeToString(policy.getBytes());
        String signature = ServiceSignature.create().computeSignature(accessKeySecret, encodePolicy);
        OssWebVO result=new OssWebVO();
        result.setAccessKeyId(accessKeyId);
        result.setPolicy(encodePolicy);
        result.setSignature(signature);
        return result;
	}

}
