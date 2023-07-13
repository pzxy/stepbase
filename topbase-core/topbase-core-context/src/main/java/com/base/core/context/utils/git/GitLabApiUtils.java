package com.base.core.context.utils.git;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.base.core.context.utils.PlaceholderUtil;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.codec.Base64;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.rest.HttpRequest;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;
import com.gitee.magic.framework.head.utils.CodecUtils;

/**
 * @author  start
 */
public class GitLabApiUtils {
	
	private static String GITLAB_FILECONTENT_API = "#{REPO_IP}/api/v4/projects/#{PROJECT_ID}/repository/files/#{FILE_PATH}?ref=#{BRANCH_NAME}";
	private static String GITLAB_SINGLE_PROJECT_API = "#{REPO_IP}/api/v4/projects/#{PROJECT_PATH}";

	/**
	 * 使用gitLab api获取指定项目的projectId，为Get请求
	 * @param repoIp       项目仓库的地址
	 * @param owner  项目的path，如：http://192.168.59.185/weizhenyao/magic.git，则owner为：weizhenyao
	 * @param repo  项目的path，如：http://192.168.59.185/weizhenyao/magic.git，则repo为：magic
	 * @param privateToken 进入gitlab后台User Settings->Access Tokens创建
	 * @return 返回目的projectId
	 */
	public static int getProjectId(String repoIp,String owner,String repo, String privateToken) {
		String projectPath=owner+"/"+repo;
		Map<String, String> params = new HashMap<String, String>(4);
		params.put("REPO_IP", repoIp);
		// gitlab api要求项目的path需要安装uri编码格式进行编码，比如"/"编码为"%2F"
		params.put("PROJECT_PATH", CodecUtils.encode(projectPath,Config.getEncoding()));
		String getSingleProjectUrl = PlaceholderUtil.anotherReplace(GITLAB_SINGLE_PROJECT_API, params);
		HttpRequest request=new HttpRequest(getSingleProjectUrl,RequestMethodEnum.GET);
		request.setHeader("PRIVATE-TOKEN", privateToken);
		HttpWrapper wrapper=new HttpWrapper();
		String content=wrapper.start(request);
		JsonObject responBody = new JsonObject(content);
		int projectId = responBody.getInt("id");
		return projectId;
	}

	public static String getFileContentFromRepository(
			String repoIp,
			int projectId,String branchName, 
			String path,String privateToken) {
		Map<String, String> params = new HashMap<String, String>(20);
		params.put("REPO_IP", repoIp);
		params.put("PROJECT_ID", projectId + "");
		params.put("BRANCH_NAME", branchName);
		params.put("FILE_PATH", CodecUtils.encode(path,Config.getEncoding()));
		String reqFileCotnetUri = PlaceholderUtil.anotherReplace(GITLAB_FILECONTENT_API, params);
		HttpRequest request=new HttpRequest(reqFileCotnetUri,RequestMethodEnum.GET);
		request.setHeader("PRIVATE-TOKEN", privateToken);
		HttpWrapper wrapper=new HttpWrapper();
		String body=wrapper.start(request);
		JsonObject jsonBody = new JsonObject(body);
		String content = jsonBody.getString("content");
		return new String(Base64.decode(content));
	}
	
	public static String loadString(String repoIp,String owner,String repo,String branchName,String projectName,String active,String privateToken) {
		String path=projectName+"/application-"+active+".properties";
		int projectId = getProjectId(repoIp, owner,repo, privateToken);
		return getFileContentFromRepository(repoIp,projectId,branchName,path,privateToken);
	}
	
	public static Properties load(String repoIp,String owner,String repo,String branchName,String projectName,String active,String privateToken) {
		String content=loadString(repoIp,owner,repo,branchName,projectName,active,privateToken);
		Properties prop=new Properties();
		try {
			prop.load(new ByteArrayInputStream(content.getBytes()));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		return prop;
	}

}
