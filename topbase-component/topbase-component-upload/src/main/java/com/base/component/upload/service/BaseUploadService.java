package com.base.component.upload.service;

import java.io.File;
import java.io.InputStream;

import com.base.core.head.vo.UploadStsVO;

/**
 * @author start 
 */
public interface BaseUploadService {

	/**
	 * STS授权
	 * 
	 * @param name
	 * @return
	 */
	UploadStsVO sts(String name);

	/**
	 * 上传对象
	 * @param key
	 * @param file
	 */
	void putObject(String key, File file);
	
	/**
	 * Put输入流
	 * @param key
	 * @param inputStream
	 */
	void putObject(String key, InputStream inputStream);

	/**
	 * 删除对象
	 * @param key
	 */
	void deleteObject(String key);

	/**
	 * 获取对象
	 * @param key
	 * @param file
	 */
	void getObject(String key, File file);
	
	/**
	 * 判断文件是否存在
	 * 
	 * @param key
	 * @return
	 */
	boolean doesObjectExist(String key);
	
	/**
	 * 生成下载文件url
	 * 
	 * @param key
	 * @return
	 */
	String getUrl(String key);

	/**
	 * 生成下载文件url
	 * 
	 * @param key
	 * @param isExpiration 是否有过期时间
	 * @return
	 */
	String getUrl(String key, Boolean isExpiration);

	/**
	 * 生成缩略图
	 * @param key
	 * @param width
	 * @param height
	 * @return
	 */
	String getImageSnapshotUrl(String key, Integer width, Integer height);

	/**
	 * 生成视频缩略图
	 * @param key
	 * @param width
	 * @param height
	 * @return
	 */
	String getVideoSnapshotUrl(String key, Integer width, Integer height);

}
