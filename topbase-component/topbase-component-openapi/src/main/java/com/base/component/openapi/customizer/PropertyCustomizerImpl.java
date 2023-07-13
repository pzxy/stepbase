package com.base.component.openapi.customizer;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.base.component.openapi.utils.ApiUtils;
import com.fasterxml.jackson.databind.type.TypeBase;
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
import com.gitee.magic.framework.head.utils.TimeUtils;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 *  @author start
 */
@Component
public class PropertyCustomizerImpl implements PropertyCustomizer {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Schema customize(Schema property, AnnotatedType type) {
		if(property==null) {
			return property;
		}
		if(type.getCtxAnnotations()==null) {
			return property;
		}
		TypeBase t=(TypeBase)type.getType();
		Class<?> rawClass=t.getRawClass();
		Boolean s=true;
		for(Annotation ann:type.getCtxAnnotations()) {
			if(ann.annotationType().equals(io.swagger.v3.oas.annotations.media.Schema.class)) {
				s=false;
				break;
			}
		}
		if(s) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("未添加@Schema注解 Generic:{} Field:{}",type.getParent().getName(),type.getPropertyName());
			}
		}
		String int64Format="int64";
		String dateTimeFormat="date-time";
		if(int64Format.equals(property.getFormat())
				||dateTimeFormat.equals(property.getFormat())) {
			StringSchema cloneProperty=new StringSchema();
			BeanUtils.copyProperties(property, cloneProperty);
			cloneProperty.setType("string");
			cloneProperty.setFormat(null);
			if(int64Format.equals(property.getFormat())) {
				cloneProperty.setDefault(Long.MAX_VALUE);
			}
			property=cloneProperty;
		}
		List<String> descriptions = new ArrayList<>();
		if(!StringUtils.isEmpty(property.getDescription())) {
			descriptions.add(property.getDescription());
		}
		for(Annotation ann:type.getCtxAnnotations()) {
			if(ann.annotationType().equals(Column.class)) {
				
			}
			if(ann.annotationType().equals(NotNull.class)) {
				
			}
			if(ann.annotationType().equals(NotEmpty.class)) {
				descriptions.add(ApiUtils.getDescribe((NotEmpty)ann));
			}
			if(ann.annotationType().equals(Length.class)) {
				Length sv=(Length)ann;
				property.setMinLength(sv.min());
				property.setMaxLength(sv.max());
			}
			if(ann.annotationType().equals(ShortValid.class)) {
				ShortValid sv=(ShortValid)ann;
				property.setMinimum(new BigDecimal(String.valueOf(sv.min())));
				property.setMaximum(new BigDecimal(String.valueOf(sv.max())));
			}
			if(ann.annotationType().equals(IntegerValid.class)) {
				IntegerValid sv=(IntegerValid)ann;
				property.setMinimum(new BigDecimal(String.valueOf(sv.min())));
				property.setMaximum(new BigDecimal(String.valueOf(sv.max())));
			}
			if(ann.annotationType().equals(LongValid.class)) {
				LongValid sv=(LongValid)ann;
				property.setMinimum(new BigDecimal(String.valueOf(sv.min())));
				property.setMaximum(new BigDecimal(String.valueOf(sv.max())));
			}
			if(ann.annotationType().equals(FloatValid.class)) {
				FloatValid sv=(FloatValid)ann;
				property.setMinimum(new BigDecimal(String.valueOf(sv.min())));
				property.setMaximum(new BigDecimal(String.valueOf(sv.max())));
			}
			if(ann.annotationType().equals(DoubleValid.class)) {
				DoubleValid sv=(DoubleValid)ann;
				property.setMinimum(new BigDecimal(String.valueOf(sv.min())));
				property.setMaximum(new BigDecimal(String.valueOf(sv.max())));
			}
			if(ann.annotationType().equals(Equals.class)) {
				Equals sv=(Equals)ann;
				property.required(Arrays.asList(sv.value()));
			}
			if(ann.annotationType().equals(NotEquals.class)) {
				descriptions.add(ApiUtils.getDescribe((NotEquals)ann));
			}
			if(ann.annotationType().equals(Regex.class)) {
				Regex sv=(Regex)ann;
				property.pattern(sv.regex());
			}
			if(ann.annotationType().equals(Format.class)) {
				descriptions.add(ApiUtils.getDescribe((Format)ann));
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
								//时间戳转int64
								IntegerSchema cloneProperty=new IntegerSchema();
								BeanUtils.copyProperties(property, cloneProperty);
								cloneProperty.setType("integer");
								cloneProperty.setFormat(int64Format);
								property=cloneProperty;
								property.setDefault(System.currentTimeMillis()+"");
								property.setPattern(doc.value());
							} else {
								property.setDefault(TimeUtils.format(new Date(), doc.value()));
								property.setPattern(doc.value());
							}
						} else {
							descriptions.add(doc.value());
						}
					}else {
						descriptions.add("PropertyConverter("+editor.getSimpleName()+")");
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
		property.setDescription(StringUtils.listToString(descriptions));
		return property;
	}

}
