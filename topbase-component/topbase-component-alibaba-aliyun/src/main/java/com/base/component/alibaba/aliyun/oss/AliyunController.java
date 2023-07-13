package com.base.component.alibaba.aliyun.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.mvc.BaseV1Controller;
import com.base.core.head.vo.OssWebVO;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author start 
 */
@RestController
@RequestMapping("/aliyun")
@Tag(name = "云存储")
public class AliyunController extends BaseV1Controller {
	
	@Autowired
	private OssService oss;

	@Operation(summary="获取WebOSS直传信息")
	@RestfulCheck
    @GetMapping("/web")
    public ObjectVO<OssWebVO> web(){
    	return response(oss.web());
    }
	
}
