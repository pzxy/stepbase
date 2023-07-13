package com.common.core;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.gitee.magic.framework.base.rest.ApiRestTemplate;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;
import com.gitee.magic.framework.head.utils.CodecUtils;
import com.gitee.magic.framework.head.utils.IoUtils;

public class DevDocTest {

	private final String HOST="https://uat.xshs.cn:8080/oa";
//	private final String HOST="https://uat.xshs.cn:8080/education";
	private final String ACCESSID="8412f654f8662d033111fc453edc5b63";
	private final String ACCESSKEY="c4jWPrC8mdRC+Nt3ftdwDDi564O3L0+FqMjRRHwHigBwmmoSZB7Pug==";
	
	public ApiRestTemplate template() {
		return new ApiRestTemplate(HOST, ACCESSID, ACCESSKEY);
	}
	
	@Test
	public void test_sql_query() throws IOException {
		ClassPathResource cpr=new ClassPathResource("test_sql_query.txt");
		String sql=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(),-1));

		template().requestForString("/devdoc/jdbc/query",RequestMethodEnum.POST,sql);
	}
	
	@Test
	public void test_sql_execute() throws IOException {
		ClassPathResource cpr=new ClassPathResource("test_sql_execute.txt");
		String sql=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(),-1));

		template().requestForString("/devdoc/jdbc/execute",RequestMethodEnum.POST,sql);
	}
	
	@Test
	public void test_sql_update() throws IOException {
		ClassPathResource cpr=new ClassPathResource("test_sql_update.txt");
		String sql=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(),-1));
		
		template().requestForString("/devdoc/jdbc/update",RequestMethodEnum.POST,sql);
	}
	
	@Test
	public void test_config() {
		template().requestForString("/devdoc/config",RequestMethodEnum.GET,null);
	}
	
	@Test
	public void test_file_list() {
		String path="/applications/app_data/xxl-job/jobhandler";
		ApiRestTemplate template=new ApiRestTemplate(HOST);
		template.requestForString("/devdoc/file/list?path="+
				CodecUtils.encode(path,"UTF-8"),RequestMethodEnum.GET,null);
	}

}
