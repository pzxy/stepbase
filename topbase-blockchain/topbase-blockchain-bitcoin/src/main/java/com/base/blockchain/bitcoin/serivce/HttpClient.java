package com.base.blockchain.bitcoin.serivce;

import com.gitee.magic.framework.base.rest.HttpRequest;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;

/**
 * @author start 
 */
public class HttpClient {

	public static String requestGet(String url) {
		HttpRequest request=new HttpRequest(url,RequestMethodEnum.GET);
		HttpWrapper wrapper=new HttpWrapper();
		return wrapper.start(request);
	}
	
}
