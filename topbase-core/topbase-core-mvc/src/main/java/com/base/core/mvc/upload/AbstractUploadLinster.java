package com.base.core.mvc.upload;

/**
 * @author start
 */
public abstract class AbstractUploadLinster {
	
	/**
	 * 1、根据文件指纹判断当前文件是否已经存在
	 */
	public UploadModel isExist(String signature){
		return null;
	}
	
	/**
	 * 2、把文件从服务端保存至第三方存平台
	 * @param fileName 文件名
	 * @param filepath	文件存储路径
	 */
	public void localSaveToThirdparty(String fileName,String filepath){
		
	}

	/**
	 * 保存文件存储信息
	 * @param mode
	 * @return
	 */
	public abstract String save(UploadModel mode);
	
}
