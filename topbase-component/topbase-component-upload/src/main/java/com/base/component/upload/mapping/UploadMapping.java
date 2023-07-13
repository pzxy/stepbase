package com.base.component.upload.mapping;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.base.core.head.vo.UploadStsVO;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * @author start 
 */
@RequestMapping("/upload")
public interface UploadMapping {

	/**
	 * 申请文件Key
	 * @return
	 */
	@Operation(summary="申请文件Key")
	@GetMapping("/applykey")
	ObjectVO<String> apply();
	
	/**
	 * 获取STS授权
	 * @return
	 */
	@Operation(summary="获取STS授权")
	@GetMapping("/sts")
	ObjectVO<UploadStsVO> sts();

	/**
	 * 文件上传(同名文件覆盖)
	 * @param file
	 * @return
	 */
	@Operation(summary="文件上传(同名文件覆盖)")
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ObjectVO<String> upload(@Parameter(description = "文件") @RequestPart MultipartFile file);
	
}
