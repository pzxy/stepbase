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
public class FilHttpUtils {
	
	public static JsonObject result(Optional<String> response) {
        if (response.isPresent()) {
            String body = response.get();
            JsonObject result=new JsonObject(body);
            String error1="error";
            if(result.has(error1)) {
            	throw new ApplicationException(result.get("error").toString());
            }
            String result1="result";
            if(result.has(result1)) {
            	return result;
            }
        }
        return null;
    }

    public static Optional<String> get(String url) {
    	Map<String,String> header=new HashMap<>(1);
    	header.put("User-Agent", "Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)");
        return request(url,header,RequestMethodEnum.GET,null);
    }

    public static Optional<String> post(String rpcUrl,Map<String,String> header,List<Object> object, String method) {
    	header.put("Content-Type", "application/json");
    	JsonObject params = new JsonObject();
    	params.put("jsonrpc", "2.0");
        params.put("id", 1);
        params.put("method", method);
        params.put("params", object);
        return request(rpcUrl,header,RequestMethodEnum.POST,params.toString());
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
