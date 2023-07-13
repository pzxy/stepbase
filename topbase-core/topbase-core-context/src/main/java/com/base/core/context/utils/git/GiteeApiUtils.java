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
public class GiteeApiUtils {

	private static String GITEE_FILECONTENT_API = "#{REPO_IP}/api/v5/repos/#{OWNER}/#{REPO}/contents/#{FILE_PATH}?ref=#{BRANCH_NAME}&access_token=#{PRIVATE_TOKEN}";
	
	public static String getFileContentFromRepository(
			String repoIp,String owner,String repo,String branchName,String fileFullPath,String privateToken) {
		Map<String, String> params = new HashMap<String, String>(20);
		params.put("REPO_IP", repoIp);
		params.put("OWNER", owner);
		params.put("REPO", repo);
		params.put("FILE_PATH", CodecUtils.encode(fileFullPath,Config.getEncoding()));
		params.put("BRANCH_NAME", branchName);
		params.put("PRIVATE_TOKEN", privateToken);
		String reqFileCotnetUri = PlaceholderUtil.anotherReplace(GITEE_FILECONTENT_API, params);
		HttpRequest request=new HttpRequest(reqFileCotnetUri,RequestMethodEnum.GET);
		HttpWrapper wrapper=new HttpWrapper();
		String body=wrapper.start(request);
		JsonObject jsonBody = new JsonObject(body);
		String content = jsonBody.getString("content");
		return new String(Base64.decode(content));
	}
	
	public static String loadString(String repoIp,String owner,String repo,String branchName,String projectName,String active,String privateToken) {
		String fileFullPathName=projectName+"/application-"+active+".properties";
		return getFileContentFromRepository(repoIp,owner,repo,branchName,fileFullPathName,privateToken);
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
