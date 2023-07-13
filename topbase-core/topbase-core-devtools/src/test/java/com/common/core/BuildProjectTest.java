package com.common.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.base.core.devtools.utils.ProjectUpdate;

public class BuildProjectTest {

	@Test
	public void test_build() throws IOException {
		File file = new File("/Users/start/Desktop/toptemplate");
		Map<String, String> p = new HashMap<>();
		p.put("prefix", "top");
		p.put("companyname", "topnetwork");
		p.put("ProjectTemplate", "blockchain");
		p.put("projecttemplate", "blockchain".toLowerCase());
		ProjectUpdate project = new ProjectUpdate(file, p);
		project.build();
	}

	@Test
	public void test_upgrade() throws IOException {
		// swagger升级
		File file = new File("/Users/start/Documents/Developer/git/top-nfr");
		Map<String, String> params = new LinkedHashMap<>(3);
		// pom
		params.put("io.swagger<", "io.swagger.core.v3<");
		params.put("start.magic<", "com.gitee.magic<");
		params.put("topbase-core-framework<", "topbase-core-context<");
		params.put("com.topnetwork<", "org.topnetwork<");
		params.put("topbase-core-ref<", "topbase-core-head<");
		params.put("magic-framework-core<", "magic-framework-base<");
		params.put("com.topnetwork.foundation<", "org.topnetwork.foundation<");
		params.put("topbase-component-boot-swagger<", "topbase-component-openapi<");
		// api
		params.put("\\@Api\\([ ]*tags", "@Tag(name");
		params.put("\\@ApiOperation\\(", "@Operation(summary=");
		params.put("\\@ApiParam\\(", "@Parameter(description=");
		params.put("\\@ApiModel\\([ ]*\"", "@Schema(title=\"");
		params.put("\\@ApiModel\\([ ]*description", "@Schema(title");
		params.put("\\@ApiModelProperty\\([ ]*\"", "@Schema(title=\"");
		params.put("\\@ApiModelProperty\\([ ]*value", "@Schema(title");
		params.put("io.swagger.annotations.Api;", "io.swagger.v3.oas.annotations.tags.Tag;");
		params.put("io.swagger.annotations.ApiParam;", "io.swagger.v3.oas.annotations.Parameter;");
		params.put("io.swagger.annotations.ApiOperation;", "io.swagger.v3.oas.annotations.Operation;");
		params.put("io.swagger.annotations.ApiModel;", "io.swagger.v3.oas.annotations.media.Schema;");
		params.put("io.swagger.annotations.ApiModelProperty;", "io.swagger.v3.oas.annotations.media.Schema;");

		// magic
		params.put("start.framework.utils.ScriptDelegate;",
				"com.base.core.framework.sql.utils.ScriptDelegate;");
		params.put("start.magic.core.converter.PropertyConverterEditor;",
				"com.gitee.magic.context.PropertyConverterEditor;");
		params.put("start.magic.core.ApplicationException", "com.gitee.magic.core.exception.ApplicationException");
		params.put("start.framework.commons.utils.TimeUtils", "com.gitee.magic.framework.head.utils.TimeUtils");
		params.put("start.framework.commons.utils.CodecUtils", "com.gitee.magic.framework.head.utils.CodecUtils;");
		
		params.put("start.magic.core.valid.", "com.gitee.magic.core.valid.");
		params.put("start.magic.thirdparty.json.", "com.gitee.magic.core.json.");
		params.put("start.magic.thirdparty.codec.", "com.gitee.magic.core.utils.codec.");
		params.put("start.magic.core.converter.", "com.gitee.magic.core.converter.");
		params.put("start.magic.utils.", "com.gitee.magic.core.utils.");
		params.put("start.magic.context.", "com.gitee.magic.context.");
		params.put("start.framework.commons.rest.ConverterEditorUtils;", "com.gitee.magic.context.ConverterEditorUtils;");
		params.put("start.framework.commons.rest.RestConverterEditor;", "com.gitee.magic.context.RestConverterEditor;");
		params.put("start.framework.commons.rest.HttpRequest;", "com.gitee.magic.framework.base.rest.HttpRequest;");
		params.put("start.framework.commons.rest.HttpWrapper;", "com.gitee.magic.framework.base.rest.HttpWrapper;");
		params.put("start.framework.commons.rest.RequestMethodEnum;", "com.gitee.magic.framework.base.rest.RequestMethodEnum;");
		params.put("start.framework.commons.rest.RestBusinessException;", "com.gitee.magic.framework.base.rest.RestBusinessException;");
		params.put("start.framework.commons.rest.RestfulTemplate;", "com.gitee.magic.framework.base.rest.RestfulTemplate;");
		params.put("start.framework.commons.rest.ApiRestTemplate;", "com.gitee.magic.framework.base.rest.ApiRestTemplate;");

		params.put("start.framework.commons.beans.", "com.gitee.magic.framework.head.vo.");
		params.put("start.framework.commons.exception.", "com.gitee.magic.framework.head.exception.");
		params.put("start.framework.commons.converter.", "com.gitee.magic.framework.head.converter.");
		params.put("start.framework.commons.utils.Assert", "com.gitee.magic.framework.head.utils.Assert");
		params.put("start.framework.commons.utils.CodecUtils", "com.gitee.magic.framework.head.utils.CodecUtils");
		params.put("start.framework.commons.utils.FileUtils", "com.gitee.magic.framework.head.utils.FileUtils");
		params.put("start.framework.commons.utils.IoUtils", "com.gitee.magic.framework.head.utils.IoUtils");
		params.put("start.framework.commons.utils.TimeUtils", "com.gitee.magic.framework.head.utils.TimeUtils");
		
		params.put("start.framework.commons.constant.", "com.gitee.magic.framework.base.constant.");
		params.put("start.framework.commons.context.", "com.gitee.magic.framework.base.context.");
//		params.put("start.framework.commons.rest.", "com.gitee.magic.framework.base.rest.");
		params.put("start.framework.commons.result.", "com.gitee.magic.framework.base.result.");
		params.put("start.framework.commons.utils.IpUtils", "com.gitee.magic.framework.base.utils.IpUtils");
		params.put("start.framework.commons.utils.MapConvert", "com.gitee.magic.framework.base.utils.MapConvert");

		params.put("start.magic.persistence", "com.gitee.magic.jdbc.persistence");

		// topbase-core-head
		params.put("com.topnetwork.base.", "com.base.");
		params.put("com.topnetwork.ref.param.PageHeaderParam;", "com.base.core.head.ao.PageAO;");
		params.put("extends[ ]*PageHeaderParam", "extends PageAO");
		params.put("com.topnetwork.ref.param;", "com.base.core.head.ao.IDAO;");
		params.put("extends[ ]*IDParam", "extends IDAO");
		params.put("com.topnetwork.annotations", "com.base.core.head.annotations");
		params.put("com.topnetwork.ao", "com.base.core.head.ao");
		params.put("com.topnetwork.constants", "com.base.core.head.constants");
		params.put("com.topnetwork.converter", "com.base.core.head.converter");
		params.put("com.topnetwork.enums", "com.base.core.head.enums");
		params.put("com.topnetwork.excel", "com.base.core.head.excel");
		params.put("com.topnetwork.utils", "com.base.core.head.utils");
		params.put("com.topnetwork.valid", "com.base.core.head.valid");
		params.put("com.topnetwork.valid", "com.base.core.head.valid");
		params.put("com.topnetwork.vo", "com.base.core.head.vo");
		// topbase-core-context
		params.put("com.common.annotation.", "com.base.core.context.annotation.");
		params.put("com.common.utils.ProxyService;", " com.base.core.context.utils.ProxyService;");
		params.put("com.common.utils.SpringUtils;", "com.base.core.context.utils.SpringUtils;");
		params.put("com.common.mvc.Constants;", "com.base.core.context.mvc.Constants;");
		// topbase-core-mvc
		params.put("com.common.business.CommonBusiness", "com.base.core.mvc.business.CommonBusiness");
		params.put("com.common.mvc.BaseController;", "com.base.core.context.mvc.BaseController;");
		params.put("com.common.mvc.BaseV1Controller;", "com.base.core.context.mvc.BaseV1Controller;");
		params.put("com.common.mvc.BaseInterceptor;", "com.base.common.mvc.BaseInterceptor;");
		params.put("com.common.mvc.RequserAgainFilter;", "com.base.core.mvc.web.RequserAgainFilter;");
		params.put("com.common.mvc.DefaultWebMvcConfiguration;", "com.base.core.mvc.web.DefaultWebMvcConfiguration;");
		params.put("com.common.utils.LoggerUtils;", "com.base.core.context.utils.LoggerUtils;");

		// topbase-core-framework
		params.put("start.framework.service.", "com.base.core.framework.sql.service.");
		params.put("start.framework.dao.", "com.base.core.framework.sql.dao.");
		params.put("start.framework.entity.", "com.base.core.framework.sql.entity.");

		ProjectUpdate project = new ProjectUpdate(file, params);
		project.build();
	}

}
