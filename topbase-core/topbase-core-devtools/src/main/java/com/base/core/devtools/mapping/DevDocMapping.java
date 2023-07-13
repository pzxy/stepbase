package com.base.core.devtools.mapping;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.core.devtools.head.ao.SignAO;
import com.base.core.devtools.head.vo.ConfigVO;
import com.base.core.devtools.head.vo.SignVO;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.valid.annotation.BooleanValid;
import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.core.valid.annotation.NotNull;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * @author start 
 */
@RequestMapping("/devdoc")
public interface DevDocMapping {

	/**
	 * 配置参数
	 * @return
	 */
	@Operation(summary="配置参数")
	@GetMapping("/config")
	ObjectVO<ConfigVO> config();

	/**
	 * 响应码
	 * @param language
	 * @return
	 */
	@Operation(summary="响应码")
	@GetMapping("/resp/{language}")
	ObjectVO<Map<String, String>> resp(@PathVariable @Parameter(description = "语言") @NotNull String language);

	/**
	 * 数据字典
	 * @param source
	 * @return
	 */
	@Operation(summary="数据字典")
	@GetMapping("/data/dictionary/{source}")
	ObjectVO<Map<String, JsonObject>> dictionary(@PathVariable @Parameter(description = "是否显示源") @NotNull @BooleanValid Boolean source);

	/**
	 * 生成签名
	 * @param param
	 * @return
	 */
	@Operation(summary="生成签名")
	@PostMapping("/sign")
	ObjectVO<SignVO> sign(@RequestBody SignAO param);

	/**
	 * 文件列表
	 * @param path
	 * @return
	 */
	@Operation(summary="文件列表")
	@GetMapping("/file/list")
	ListVO<String> listFile(@RequestParam @Parameter(description = "路径") @NotNull @NotEmpty String path);

	/**
	 * 文件下载
	 * @param path
	 */
	@Operation(summary="文件下载")
	@GetMapping("/file/download")
	void download(@RequestParam @Parameter(description = "路径") @NotNull @NotEmpty String path);

}
