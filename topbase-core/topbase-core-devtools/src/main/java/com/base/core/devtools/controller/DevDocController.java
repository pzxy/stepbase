package com.base.core.devtools.controller;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.bind.annotation.RestController;

import com.base.core.context.annotation.RestfulCheck;
import com.base.core.context.mvc.BaseV1Controller;
import com.base.core.context.utils.LocalizationMessage;
import com.base.core.context.utils.ResponseDownloadUtils;
import com.base.core.devtools.head.ao.SignAO;
import com.base.core.devtools.head.vo.ConfigVO;
import com.base.core.devtools.head.vo.SignVO;
import com.base.core.devtools.mapping.DevDocMapping;
import com.base.core.head.constants.CodeResVal;
import com.base.core.head.utils.DataDictionaryUtils;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.Base64;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.constant.Message;
import com.gitee.magic.framework.base.context.CacheContext;
import com.gitee.magic.framework.base.utils.IpUtils;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.CodecUtils;
import com.gitee.magic.framework.head.vo.ListVO;
import com.gitee.magic.framework.head.vo.ObjectVO;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author start 
 */
@RestController
@Tag(name = "内部接口")
public class DevDocController extends BaseV1Controller implements DevDocMapping {

	@Value("${common.coderes.classpath:}")
	private String codeResClassPath;
	@Autowired
    private ConfigurableEnvironment environment;

	@RestfulCheck
	@Override
    public ObjectVO<ConfigVO> config() {
		ConfigVO result=new ConfigVO();
		result.setFullName(Config.getFullName());
		result.setSystemName(Config.getSystemName());
		result.setSystemVersion(Config.getSystemVersion());
		result.setBalancedDataCenterId(Config.getBalancedDataCenterId());
		result.setBalancedWorkerId(Config.getBalancedWorkerId());
		result.setLocalIpByNetcard(IpUtils.getLocalIpByNetcard());
		result.setRequestIp(IpUtils.getRequestIp(request));
		result.setActiveUsers(CacheContext.getActiveUsers());
    	result.setMaxBodyTextSize(Config.getMaxBodyTextSize());
    	
    	MutablePropertySources propSrcs = environment.getPropertySources();        
    	Map<String, String> props = StreamSupport.stream(propSrcs.spliterator(), false)
    			.filter(ps -> ps instanceof EnumerablePropertySource)
    			.map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
    			.flatMap(Arrays::stream).distinct().collect(Collectors.toMap(Function.identity(), environment::getProperty));
    	result.setProps(props);
        return response(result);
    }

	@RestfulCheck
	@Override
    public ObjectVO<Map<String,String>> resp(String language) {
        Map<String, String> results = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(Integer.parseInt(o1)>Integer.parseInt(o2)) {
					return 1;
				}else if(Integer.parseInt(o1)<Integer.parseInt(o2)) {
					return -1;
				}else {
					return 0;
				}
			}
			
		});
        List<Class<?>> protList=new ArrayList<>();
        if(!StringUtils.isEmpty(codeResClassPath)) {
        	Class<?> prototype;
    		try {
    			prototype = Class.forName(codeResClassPath);
    		} catch (ClassNotFoundException e) {
    			throw new ApplicationException(e);
    		}
    		protList.add(prototype);
        }
        protList.add(CodeResVal.class);
        for(Class<?> pro:protList) {
        	Field[] fields = pro.getFields();
            for (Field field : fields) {
                String value;
    			try {
    				value = String.valueOf(field.get(null));
    			} catch (IllegalArgumentException | IllegalAccessException e) {
    				throw new ApplicationException(e);
    			}
                Message message = LocalizationMessage.getMessage(Message.parse(value), language);
                results.put(String.valueOf(message.getCode()), message.getMessage());
            }
        }
        return response(results);
    }

	@RestfulCheck
	@Override
	public ObjectVO<Map<String, JsonObject>> dictionary(Boolean source) {
		Map<String, JsonObject> results = new TreeMap<>();
		for(String key:DataDictionaryUtils.getDataDictionaryMap().keySet()) {
			if(source) {
				results.put(key, new JsonObject(DataDictionaryUtils.getDataDictionaryMap().get(key)));
			}else {
				results.put(key, new JsonObject(DataDictionaryUtils.getDictoinary(key)));
			}
		}
        return response(results);
	}

	@RestfulCheck
	@Override
    public ObjectVO<SignVO> sign(SignAO param) {
        SignVO result=new SignVO();
        String signatureBase64=CodecUtils.signatureHmacSha1(param.getAccessKey(), param.getContent());
        result.setSignatureBase64(signatureBase64);
        result.setSignatureBase64Url(CodecUtils.encode(signatureBase64,Config.getEncoding()));
    	String content=new String(Base64.decode(param.getContent()));
        String signature=CodecUtils.signatureHmacSha1(param.getAccessKey(), content);
        result.setSignature(signature);
        result.setSignatureUrl(CodecUtils.encode(signature,Config.getEncoding()));
        return response(result);
    }

	@RestfulCheck
	@Override
    public ListVO<String> listFile(String path) {
		List<String> listFiles=new ArrayList<>();
		File file=new File(path);
		if(file.isDirectory()) {
			File[] files=file.listFiles();
			for(File f:files) {
				if(!f.isDirectory()) {
					listFiles.add(f.getAbsolutePath());
				}
			}
		}
        return response(listFiles);
    }

	@RestfulCheck
	@Override
    public void download(String path) {
		File file=new File(path);
		if(!file.exists()) {
			throw new BusinessException(CodeResVal.CODE_404);
		}
		if(!file.isFile()) {
			throw new BusinessException(CodeResVal.CODE_404);
		}
		ResponseDownloadUtils.download(response, file.getName(),file);
    }

}
