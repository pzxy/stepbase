package com.base.core.devtools.mapping;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.core.devtools.head.vo.SecretKeyVO;
import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author start 
 */
@RequestMapping("/devdoc")
public interface DevDbMapping {

	/**
	 * 密钥
	 * @return
	 */
	@Operation(summary="密钥")
	@GetMapping("/listkeys")
	ListVO<SecretKeyVO> listkeys();

	/**
	 * 查询语句
	 * @param sql
	 * @return
	 */
	@Operation(summary="查询语句")
	@PostMapping("/jdbc/query")
	ListVO<Map<String, Object>> queryForList(@RequestBody @NotEmpty String sql);

	/**
	 * 更新语句
	 * @param sql
	 * @return
	 */
	@Operation(summary="更新语句")
	@PostMapping("/jdbc/update")
	ObjectVO<Integer> update(@RequestBody @NotEmpty String sql);

	/**
	 * 表结构下载
	 */
	@Operation(summary="表结构下载", description = "上线前可下载表结构进行结构比对")
	@GetMapping("/jdbc/table")
	void table();
}
