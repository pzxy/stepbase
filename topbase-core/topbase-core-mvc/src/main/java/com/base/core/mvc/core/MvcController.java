package com.base.core.mvc.core;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.core.context.mvc.BaseV1Controller;
import com.gitee.magic.framework.head.vo.BaseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author start 
 * @date 2022-04-11 15:00:27
 */
@RestController
@RequestMapping
@Tag(name = "Mvc")
public class MvcController extends BaseV1Controller {
	
	@Operation(summary="首页")
	@GetMapping("/")
	public void home() {
	}
	
	@Operation(summary="心跳")
	@GetMapping("/health")
	public BaseVO health() {
		return response();
	}
	
}
