package com.base.core.devtools.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.annotation.SecretKeyAuthorityCheck;
import com.base.core.context.mvc.BaseV1Controller;
import com.base.core.context.utils.ResponseDownloadUtils;
import com.base.core.devtools.head.vo.SecretKeyVO;
import com.base.core.devtools.mapping.DevDbMapping;
import com.base.core.framework.sql.utils.ScriptDelegate;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.utils.MapConvert;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.TimeUtils;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.ObjectVO;
import com.gitee.magic.jdbc.persistence.EntityDefinitionException;
import com.gitee.magic.jdbc.persistence.annotation.Entity;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author start 
 */
@RestController
@Tag(name = "内部接口")
public class DevDbController extends BaseV1Controller implements DevDbMapping {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	@RestfulCheck
	@Override
	public ListVO<SecretKeyVO> listkeys() {
		if (Config.isSystemFlag()) {
			throw new BusinessException("线上环境请联系管理员获取");
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> datas = jdbcTemplate.queryForList("select * from sys_secret_key");
		List<SecretKeyVO> results = new ArrayList<>();
		for (Map<String, Object> data : datas) {
			results.add(MapConvert.convert(SecretKeyVO.class, data));
		}
		return response(results);
	}

	@SecretKeyAuthorityCheck(-1)
	@Override
	public ListVO<Map<String, Object>> queryForList(String sql) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return response(jdbcTemplate.queryForList(sql));
	}

	@SecretKeyAuthorityCheck(-1)
	@Override
	public ObjectVO<Integer> update(String sql) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return response(jdbcTemplate.update(sql));
	}

	@RestfulCheck
	@Override
	public void table() {
		StringBuilder sbSql = new StringBuilder();
		ScriptDelegate delegate = new ScriptDelegate();
		String[] names = beanFactory.getBeanNamesForAnnotation(Entity.class);
		for (String name : names) {
			Class<?> prototype = beanFactory.getType(name);
			try {
				sbSql.append(delegate.buildTableScript(prototype));
				sbSql.append("\n");
			} catch (EntityDefinitionException e) {
				throw new ApplicationException(e);
			}
		}
		ResponseDownloadUtils.download(response, Config.getFullName() + "_table_" + TimeUtils.getSysTimeLong() + ".sql",
				sbSql.toString());
	}

}
