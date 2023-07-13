package com.base.blockchain.filecoin.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.framework.base.rest.HttpRequest;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;

/**
 * @author start 
 */
public class HttpUtils {
	
	public static JsonObject result(String body) {
		JsonObject result=new JsonObject(body);
		String er1="error";
        if(result.has(er1)) {
        	throw new ApplicationException(result.get("error").toString());
        }
        String re1="result";
        if(result.has(re1)) {
        	return result;
        }
        return null;
    }

    public static String post(String url,String authorization,List<Object> params, String method) {
    	Map<String,String> header=new HashMap<>(3);
    	header.put("Authorization", authorization);
    	header.put("Content-Type", "application/json");
    	JsonObject body = new JsonObject();
    	body.put("jsonrpc", "2.0");
        body.put("id", 1);
        body.put("method", method);
        body.put("params", params);
        Optional<String> response=request(url,header,RequestMethodEnum.POST,body.toString());
        return response.get();
    }
    
    public static Optional<String> request(String url,Map<String,String> header,RequestMethodEnum method,String bodyContent){
    	HttpRequest request=new HttpRequest(url,method);
        request.setHeaders(header);
        request.setBodyContent(bodyContent);
        HttpWrapper wrapper=new HttpWrapper();
        String content=wrapper.start(request);
        return Optional.of(content);
    }
    
}
