package com.base.component.openapi.customizer;

import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Operation;

/**
 *  @author start
 */
@Component
public class OperationCustomizerImpl implements GlobalOperationCustomizer {

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if(handlerMethod==null) {
			return operation;
		}
		String operationId=handlerMethod.getMethod().getName()+
				"_"+handlerMethod.getBeanType().getSimpleName();
		operation.setOperationId(operationId);
		return operation;
	}

}
