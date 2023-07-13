package com.base.service.system.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.base.core.context.annotation.AuthenticationCheck;
import com.base.core.mvc.business.CommonBusiness;
import com.base.service.system.dao.AuthDao;
import com.base.service.system.entity.AuthDO;
import com.base.service.system.service.AuthService;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.base.rest.RequestMethodEnum;
import com.gitee.magic.jdbc.persistence.source.jdbc.sqlplus.conditions.query.QueryWrapper;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author start 
 */
@Component("authUtils")
public class AuthUtils {

	@Autowired
	private AuthDao authDao;
	@Autowired
	private AuthService authService;
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	/**
	 * 生成映射仅供开发使用
	 * @param parentId
	 * @param isInvalidDeleteAction
	 */
	@Transactional(rollbackFor = Exception.class)
	public void generateMapping(Long parentId,Boolean isInvalidDeleteAction) {
		List<AuthDO> listAuths=new ArrayList<>();
		Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
		Set<RequestMappingInfo> set = map.keySet();
		for (RequestMappingInfo info : set) {
			HandlerMethod handlerMethod = map.get(info);
			AuthenticationCheck check=handlerMethod.getMethodAnnotation(AuthenticationCheck.class);
			if (check!=null) {
				if(check.value()) {
					//可根据Action做更新
					String action=CommonBusiness.getActionCode(handlerMethod);
					QueryWrapper wrapper=new QueryWrapper();
					wrapper.eq("action", action);
					AuthDO auth=authService.get(authDao.queryForList(wrapper));
					if(auth==null) {
						auth = new AuthDO();
						auth.setAction(action);
						auth.setValue(0);
						auth.setSort(999);
						auth.setParentId(parentId);
					}
					String uri=info.getPathPatternsCondition().getPatterns().iterator().next().getPatternString();
					auth.setUri(uri);
					auth.setMethod(getRequestMethod(handlerMethod));
					Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
					if (operation!=null) {
						auth.setName(operation.summary());
					}else {
						throw new ApplicationException(action+"未注解 @ApiOperation");
					}
					listAuths.add(auth);
				}
			}
		}
		authService.saveBatch(listAuths);
		//移除不存在的Action码
		Map<Long,AuthDO> authMap=listAuths.stream().collect(Collectors.toMap(AuthDO::getId, Function.identity()));
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.eq("parentId", parentId);
		List<AuthDO> allAuths=authDao.queryForList(wrapper);
		for(AuthDO a:allAuths) {
			if(!authMap.containsKey(a.getId())) {
				if(isInvalidDeleteAction) {
					authService.remove(a);
					System.out.println("已自动删除无效操作码:"+a.getParentId()+"\t"+a.getName()+"\t"+a.getAction()+"\t"+a.getUri()+"_"+a.getMethod());
				}else{
					System.out.println("请手动删除无效操作码:"+a.getParentId()+"\t"+a.getName()+"\t"+a.getAction()+"\t"+a.getUri()+"_"+a.getMethod());
				}
			}
		}
	}
	
	public static String getRequestMethod(HandlerMethod method) {
        if (method.hasMethodAnnotation(GetMapping.class)) {
            return RequestMethodEnum.GET.name();
        }else if (method.hasMethodAnnotation(PostMapping.class)) {
            return RequestMethodEnum.POST.name();
        }else if (method.hasMethodAnnotation(PutMapping.class)) {
            return RequestMethodEnum.PUT.name();
        }else if (method.hasMethodAnnotation(DeleteMapping.class)) {
            return RequestMethodEnum.DELETE.name();
        }else if (method.hasMethodAnnotation(RequestMapping.class)) {
			return "ALL";
        }
        return null;
    }
	
}
