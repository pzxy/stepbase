package com.base.component.xxljob.service;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.rest.HttpRequest;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;

/**
 * 配置源代码JobInfoController,接口方法添加@PermissionLimit(limit = false)注解则可无须登录调用
 * @author start
 *
 */
public class JobInfoService {

	private static final String CODE="code";
	private static final Integer OKCODE=200;
	
	private String addresses;
	private String userName;
	private String passWord;
	
	public JobInfoService(String address,String userName,String password) {
		this.addresses=address;
		this.userName=userName;
		this.passWord=password;
	}

	/**
	 * jobCron:表达式
	 * jobDesc:任务描述
	 * jobGroup:任务组ID
	 * executorHandler:执行器
	 * author:负责人
	 * executorParam:这是参数
	 * glueType:BEAN
	 * executorRouteStrategy:FIRST
	 * executorBlockStrategy:SERIAL_EXECUTION
	 * @param params
	 * @return 返回任务ID
	 */
	public String add(Map<String, Object> params) {
		JsonObject json=request(addresses+"/jobinfo/add",params);
		return json.getString("content");
	}
	
	/**
	 * jobCron:表达式
	 * jobDesc:任务描述
	 * id:任务ID
	 * jobGroup:任务组ID
	 * executorHandler:执行器
	 * author:负责人
	 * executorParam:这是参数
	 * glueType:BEAN
	 * executorRouteStrategy:FIRST
	 * executorBlockStrategy:SERIAL_EXECUTION
	 * @param params
	 */
	public void update(Map<String, Object> params) {
		request(addresses+"/jobinfo/update",params);
	}
	
	public void remove(int jobInfoId) {
		Map<String, Object> body = new HashMap<>(1);
		body.put("id", jobInfoId);
		request(addresses+"/jobinfo/remove",body);
	}
	
	public void start(int jobInfoId) {
		Map<String, Object> body = new HashMap<>(1);
		body.put("id",jobInfoId);
		request(addresses+"/jobinfo/start",body);
	}
	
	public void stop(int jobInfoId) {
		Map<String, Object> body = new HashMap<>(1);
		body.put("id", jobInfoId);
		request(addresses+"/jobinfo/stop",body);
	}
	
	private JsonObject request(String url,Map<String, Object> params) {
		HttpRequest request=new HttpRequest(url,RequestMethodEnum.POST);
		if(!StringUtils.isEmpty(userName)&&!StringUtils.isEmpty(passWord)) {
			request.setHeader("Cookie", login(userName,passWord));
		}
		request.setUriBodyContent(params);
		HttpWrapper wrapper=new HttpWrapper();
		String content=wrapper.start(request);
		JsonObject json=new JsonObject(content);
		if(json.getInt(CODE)!=OKCODE) {
			throw new ApplicationException(json.getString("msg"));
		}
		return json;
	}
	
	public String login(String userName,String password) {
		Map<String, Object> params=new HashMap<>(2);
		params.put("userName", userName);
		params.put("password", password);
		HttpRequest request=new HttpRequest(addresses+"/login",RequestMethodEnum.POST);
		request.setUriBodyContent(params);
		HttpWrapper wrapper=new HttpWrapper();
		HttpURLConnection connection=wrapper.requestConnect(request);
		String content=wrapper.start(request,connection);
		JsonObject json=new JsonObject(content);
		if(json.getInt(CODE)!=OKCODE) {
			throw new ApplicationException(json.getString("msg"));
		}
		return connection.getHeaderField("Set-Cookie");
	}
	
	public static void main(String[] args) {
		String addresses="http://192.168.50.194:8081/xxl-job-admin";
		String userName="admin";
		String passWord="123456";
		JobInfoService js=new JobInfoService(addresses,userName,passWord);
		js.stop(60);
	}
	
}
