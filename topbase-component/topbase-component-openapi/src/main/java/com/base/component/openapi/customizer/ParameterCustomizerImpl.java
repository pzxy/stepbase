package com.base.component.openapi.customizer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import com.base.component.openapi.utils.ApiUtils;
import com.gitee.magic.core.annotations.Column;
import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.converter.PropertyConverter;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.valid.annotation.Custom;
import com.gitee.magic.core.valid.annotation.Equals;
import com.gitee.magic.core.valid.annotation.Format;
import com.gitee.magic.core.valid.annotation.Length;
import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.core.valid.annotation.NotEquals;
import com.gitee.magic.core.valid.annotation.NotNull;
import com.gitee.magic.core.valid.annotation.Regex;
import com.gitee.magic.core.valid.annotation.TimeFormat;
import com.gitee.magic.core.valid.annotation.number.DoubleValid;
import com.gitee.magic.core.valid.annotation.number.FloatValid;
import com.gitee.magic.core.valid.annotation.number.IntegerValid;
import com.gitee.magic.core.valid.annotation.number.LongValid;
import com.gitee.magic.core.valid.annotation.number.ShortValid;
import com.gitee.magic.framework.head.converter.TimeStampConverterEditor;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 *  @author start
 */
@Component
public class ParameterCustomizerImpl implements ParameterCustomizer {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
		if(parameterModel==null) {
			return parameterModel;
		}
		String int64Format="int64";
		String dateTimeFormat="date-time";
		if(int64Format.equals(parameterModel.getSchema().getFormat())
				||dateTimeFormat.equals(parameterModel.getSchema().getFormat())) {
			StringSchema cloneProperty=new StringSchema();
			BeanUtils.copyProperties(parameterModel.getSchema(), cloneProperty);
			cloneProperty.setType("string");
			cloneProperty.setFormat(null);
			parameterModel.setSchema(cloneProperty);
		}
		Class<?> rawClass=methodParameter.getParameter().getType();
		List<String> descriptions = new ArrayList<>();
		if(StringUtils.isEmpty(parameterModel.getDescription())) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("未添加@Parameter注解 Generic:{} Field:{}",
						methodParameter.getExecutable().toGenericString(),
						parameterModel.getName());
			}
		}else {
			descriptions.add(parameterModel.getDescription());
		}
		parameterModel.setRequired(false);
		for(Annotation ann:methodParameter.getParameterAnnotations()) {
			if(ann.annotationType().equals(Column.class)) {
				
			}
			if(ann.annotationType().equals(NotNull.class)) {
				parameterModel.setRequired(true);
			}
			if(ann.annotationType().equals(NotEmpty.class)) {
				descriptions.add(ApiUtils.getDescribe((NotEmpty)ann));
			}
			if(ann.annotationType().equals(ShortValid.class)) {
				descriptions.add(ApiUtils.getDescribe((ShortValid)ann));
			}
			if(ann.annotationType().equals(IntegerValid.class)) {
				descriptions.add(ApiUtils.getDescribe((IntegerValid)ann));
			}
			if(ann.annotationType().equals(LongValid.class)) {
				descriptions.add(ApiUtils.getDescribe((LongValid)ann));
			}
			if(ann.annotationType().equals(FloatValid.class)) {
				descriptions.add(ApiUtils.getDescribe((FloatValid)ann));
			}
			if(ann.annotationType().equals(DoubleValid.class)) {
				descriptions.add(ApiUtils.getDescribe((DoubleValid)ann));
			}
			if(ann.annotationType().equals(Equals.class)) {
				descriptions.add(ApiUtils.getDescribe((Equals)ann));
			}
			if(ann.annotationType().equals(NotEquals.class)) {
				descriptions.add(ApiUtils.getDescribe((NotEquals)ann));
			}
			if(ann.annotationType().equals(Format.class)) {
				descriptions.add(ApiUtils.getDescribe((Format)ann));
			}
			if(ann.annotationType().equals(Length.class)) {
				descriptions.add(ApiUtils.getDescribe((Length)ann));
			}
			if(ann.annotationType().equals(Regex.class)) {
				descriptions.add(ApiUtils.getDescribe((Regex)ann));
			}
			if(ann.annotationType().equals(TimeFormat.class)) {
				descriptions.add(ApiUtils.getDescribe((TimeFormat)ann));
			}
			if(ann.annotationType().equals(Custom.class)) {
				descriptions.add(ApiUtils.getDescribe((Custom)ann));
			}
			if(ann.annotationType().equals(PropertyConverter.class)) {
				PropertyConverter sv=(PropertyConverter)ann;
				Class<?> editor = ApiUtils.getDescribeConverter(rawClass, sv);
				if (editor != null) {
					DocDescription doc = editor.getAnnotation(DocDescription.class);
					if (doc != null) {
						if (rawClass.equals(java.sql.Date.class) 
								|| rawClass.equals(java.sql.Time.class)
								|| rawClass.equals(java.sql.Timestamp.class)
								|| rawClass.equals(java.util.Date.class)) {
							if (editor.equals(TimeStampConverterEditor.class)) {
								descriptions.add(doc.value());
							} else {
								descriptions.add(doc.value());
							}
						} else {
							descriptions.add(doc.value());
						}
					}
				}
			}
		}
		if(rawClass.isEnum()) {
			String enumDesc=ApiUtils.getDescribeEnum(rawClass);
    		if(enumDesc!=null) {
				descriptions.add(enumDesc);
    		}
		}
		parameterModel.setDescription(StringUtils.listToString(descriptions));
		return parameterModel;
	}

}
