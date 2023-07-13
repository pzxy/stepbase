package com.base.core.mvc.upload;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessAdviceException;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * @author start
 */
@Component
public class UploadHelper {
	
	/**
	 * 对上传的文件是否进行加密
	 */
	@Value("${uploadfile_encrypt:false}")
	private Boolean uploadfileEncrypt;
	
	/**
	 * 文件上传临时目录路径
	 */
	@Value("${uploadfile_temp_path}")
	private String uploadfileTempPath;
	
	/**
	 * 文件上传保存目录路径
	 */
	@Value("${uploadfile_save_path}")
	private String uploadfileSavePath;
	
	/**
	 * 文件下载保存目录路径
	 */
	@Value("${uploadfile_download_path}")
	private String uploadfileDownloadPath;
	
	@Value("${desKeyClsPath}")
	private String desKeyClsPath;
	
	/**
	 * 文件上传
	 * @param request	 请求对象
	 * @param signature 上传文件的签名值
	 * @param linster 监听对象
	 * @return
	 */
	public UploadModel upload(HttpServletRequest request,String signature,AbstractUploadLinster linster){
		return upload(request, signature,true, linster);
	}
	
	/**
	 * 文件上传
	 * @param request	 请求对象
	 * @param signature 上传文件的签名值
	 * @param isDeleteLocalFile 是否删除本地保存的文件
	 * @param linster 监听对象
	 * @return
	 */
	public UploadModel upload(HttpServletRequest request,String signature,boolean isDeleteLocalFile,AbstractUploadLinster linster){
		String name=StringUtils.random();
		// 创建临时目录
		IoUtils.mkdirs(uploadfileTempPath);
		String tmpPath = uploadfileTempPath + name;
		// 创建存储目录
		IoUtils.mkdirs(uploadfileSavePath);
		String savePath = uploadfileSavePath + name;
		try {
			// 保存文件到本地
			InputStream inputStream=null;
			try {
				inputStream=request.getInputStream();
			} catch (IOException e) {
				throw new BusinessAdviceException(e, BaseCode.CODE_1015);
			}
			//1、保存到临时目录
			String[] info = IoUtils.generateToLocal(inputStream,tmpPath);
			// 文件签名校验
			if(!info[0].equalsIgnoreCase(signature)){
				throw new BusinessException(BaseCode.CODE_1016);
			}
			// 判断是否已有相同的文件已存在则直接保存
			UploadModel tmp = linster.isExist(signature);
			if (tmp != null) {
				return tmp;
			}
			//2、判断是否加密并存储文件至本地
			if (uploadfileEncrypt) {
				IoUtils.encryption(tmpPath, savePath,desKeyClsPath);
			} else {
				if (!IoUtils.renameTo(tmpPath, savePath)) {
					throw new BusinessException(BaseCode.CODE_1015);
				}
			}
			//3、存储文件至云端
			linster.localSaveToThirdparty(name, savePath);
			//4、存储信息保存
			UploadModel mode=new UploadModel();
			mode.setName(name);
			mode.setSignature(signature);
			mode.setLength(request.getContentLength());
			mode.setEncrypt(uploadfileEncrypt);
			mode.setKey(linster.save(mode));
			return mode;
		} finally {
			// 5、删除本地文件
			IoUtils.deleteFile(tmpPath);
			if(isDeleteLocalFile){
				IoUtils.deleteFile(savePath);
			}
		}
	}
	
}
