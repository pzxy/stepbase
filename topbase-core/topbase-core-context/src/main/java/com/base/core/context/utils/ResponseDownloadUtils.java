package com.base.core.context.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.head.utils.CodecUtils;

/**
 * @author start 
 */
public class ResponseDownloadUtils {
	
	public static void setFile(HttpServletResponse response, String fileName) {
		setResponse(response, "application/octet-stream", fileName);
	}

	public static void setResponse(HttpServletResponse response, String contentType, String fileName) {
		response.setContentType(contentType);
		response.setCharacterEncoding(Config.getEncoding());
		response.setHeader("Content-disposition", "attachment;filename=" + CodecUtils.encode(fileName, Config.getEncoding()));
		response.setHeader("access-control-expose-headers", "Content-disposition");
	}
	
	public static void download(HttpServletResponse response, String fileName,File file) {
		ResponseDownloadUtils.setFile(response, fileName);
		InputStream in;
		try {
			in = new FileInputStream(file);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		out(response,in);
	}
	
	public static void download(HttpServletResponse response, String fileName,String content) {
		ResponseDownloadUtils.setFile(response, fileName);
		out(response,new ByteArrayInputStream(content.getBytes()));
	}
	
	public static void out(HttpServletResponse response,InputStream in) {
		try {
			int len = -1;
            byte[] buffer = new byte[1024];
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer,0,len);
            }
            in.close();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

}
