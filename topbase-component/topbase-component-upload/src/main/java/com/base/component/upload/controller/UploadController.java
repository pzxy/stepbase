package com.base.component.upload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.component.upload.mapping.UploadMapping;
import com.base.component.upload.service.BaseUploadService;
import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.mvc.BaseV1Controller;
import com.base.core.head.vo.UploadStsVO;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author start 
 */
@RestController
@Tag(name = "云存储")
public class UploadController extends BaseV1Controller implements UploadMapping {
	
	@Autowired
	private BaseUploadService uploadService;

	@RestfulCheck
	@Override
	public ObjectVO<String> apply() {
		return response(StringUtils.random());
	}
	
	@RestfulCheck
	@Override
	public ObjectVO<UploadStsVO> sts() {
		return response(uploadService.sts("abc"));
	}
	
	@Override
    public ObjectVO<String> upload(MultipartFile file){
		String fileName=file.getOriginalFilename();
		try {
			uploadService.putObject(fileName, file.getInputStream());
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return response(uploadService.getUrl(fileName, false));
    }
	
}
