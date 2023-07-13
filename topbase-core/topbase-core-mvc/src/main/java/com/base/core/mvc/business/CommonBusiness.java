package com.base.core.mvc.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.method.HandlerMethod;

import com.base.core.head.ao.PageAO;
import com.base.core.head.constants.CodeResVal;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.Md5;
import com.gitee.magic.framework.base.context.Http;
import com.gitee.magic.framework.base.rest.HttpWrapper;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;
import com.gitee.magic.framework.base.utils.MapConvert;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author  start
 */
public class CommonBusiness {

	/**
	 * 获取http分页参数
	 */
	public static <T extends PageAO> Map<String, Object> httpPageParam(T ao) {
		Map<String, Object> params = MapConvert.convert(ao);
		params.put("pageIndex", ao.index());
		return params;
	}
	
	/**
	 * 检测ContentType
	 */
	public static void checkRequestContentType(Http http) {
		if(http.getMethod().equals(RequestMethodEnum.GET.name())) {
			return;
		}
		int length = http.getRequest().getContentLength();
		String contentType = http.getRequest().getContentType();
		if (isDefaultContentType(contentType)) {
			//请求主体不能为空
			if (length <= 0) {
				throw new BusinessException(CodeResVal.CODE_1014);
			}
		}
	}

	public static boolean isDefaultContentType(String contentType) {
		return isContentType(contentType,HttpWrapper.CONTENTTYPE_JSON)||
				isContentType(contentType,HttpWrapper.CONTENTTYPE_GZIP);
	}
	
	public static boolean isFileContentType(String contentType) {
		return isContentType(contentType,MediaType.MULTIPART_FORM_DATA_VALUE);
	}
	
	public static boolean isContentType(String contentType,String mediaType) {
		if (StringUtils.isEmpty(contentType)) {
			return false;
		}
		try {
			MimeType mType = MimeType.valueOf(contentType);
			return MimeType.valueOf(mediaType).isCompatibleWith(mType);
		} catch (Exception e) {
			return false;
		}
	}
    
    /**
	 * 生成行为码
	 * @param handlerMethod
	 * @param method
	 * @return
	 */
	public static String getActionCode(HandlerMethod handlerMethod) {
		List<String> items=new ArrayList<>();
		Method method=handlerMethod.getMethod();
		items.add(handlerMethod.getBeanType().getName());
		items.add(method.getReturnType().getName());
		items.add(method.getName());
		for(Class<?> c:method.getParameterTypes()) {
			items.add(c.getName());
        }
		String name=StringUtils.listToString(items, "|");
		return Md5.md5(name);
	}

	public static void main(String[] args) {
		System.out.println(isFileContentType("multipart/form-data"));
	}

}
