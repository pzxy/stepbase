package com.base.component.openapi.utils;

import java.util.Arrays;
import java.util.List;

import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.converter.PropertyConverter;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.valid.annotation.Custom;
import com.gitee.magic.core.valid.annotation.Equals;
import com.gitee.magic.core.valid.annotation.Format;
import com.gitee.magic.core.valid.annotation.Length;
import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.core.valid.annotation.NotEquals;
import com.gitee.magic.core.valid.annotation.Regex;
import com.gitee.magic.core.valid.annotation.TimeFormat;
import com.gitee.magic.core.valid.annotation.number.DoubleValid;
import com.gitee.magic.core.valid.annotation.number.FloatValid;
import com.gitee.magic.core.valid.annotation.number.IntegerValid;
import com.gitee.magic.core.valid.annotation.number.LongValid;
import com.gitee.magic.core.valid.annotation.number.ShortValid;
import com.gitee.magic.framework.base.utils.SchemaEnumUtils;

/**
 * @author start 
 */
public class ApiUtils {
	
	public static String getDescribe(NotEmpty v) {
		return "不能为''字符串";
	}
	
	public static String getDescribe(ShortValid v) {
		return "Short 范围("+v.min()+"~"+v.max()+")";
	}
	
	public static String getDescribe(IntegerValid v) {
		return "Integer 范围("+v.min()+"~"+v.max()+")";
	}
	
	public static String getDescribe(LongValid longValid) {
		return "Long 范围("+longValid.min()+"~"+longValid.max()+")";
	}
	
	public static String getDescribe(FloatValid floatValid) {
		return "Float 范围("+floatValid.min()+"~"+floatValid.max()+")";
	}
	
	public static String getDescribe(DoubleValid doubleValid) {
		return "Double 范围("+doubleValid.min()+"~"+doubleValid.max()+")";
	}
	
	public static String getDescribe(Equals equals) {
		String value=StringUtils.listToString(Arrays.asList(equals.value()));
		return "只能等于("+value+")中的任意值区分大小写";
	}
	
	public static String getDescribe(NotEquals notEquals) {
		String value=StringUtils.listToString(Arrays.asList(notEquals.value()));
		return "不能等于("+value+")中的任意值区分大小写";
	}
	
	public static String getDescribe(Format format) {
		return "格式("+format.type()+")";
	}
	
	public static String getDescribe(Length length) {
		return "长度("+length.min()+"~"+length.max()+")";
	}
	
	public static String getDescribe(Regex regex) {
		return "正则校验("+regex.regex()+")";
	}
	
	public static String getDescribe(TimeFormat timeFormat) {
		return "日期时间格式("+timeFormat.format()+")";
	}
	
	public static String getDescribe(Custom custom) {
		DocDescription doc=custom.value().getAnnotation(DocDescription.class);
		if(doc!=null) {
			return "自定义校验("+doc.value()+")";
		}else {
			return "自定义校验("+custom.value().getSimpleName()+")";
		}
	}
	
	public static String getDescribeType(Class<?> type,PropertyConverter converter) {
		Class<?> editor = getDescribeConverter(type,converter);
		if(editor!=null) {
			DocDescription doc=editor.getAnnotation(DocDescription.class);
			if(doc!=null) {
				return doc.value();
			}
		}
		return null;
	}
	
	public static Class<?> getDescribeConverter(Class<?> type,PropertyConverter converter) {
		if(converter!=null) {
			return converter.value();
		}else {
			return PropertyConverterEditor.getConverterClasses(type);
		}
	}
	
	public static String getDescribeEnum(Class<?> type) {
		if(type.isEnum()) {
			List<String> des=SchemaEnumUtils.get(type);
//			for(Field f:type.getFields()) {
//				Schema property=f.getAnnotation(Schema.class);
//				if(property!=null) {
//					if(f.getName().equals(property.title())) {
//						des.add(f.getName());
//					}else {
//						des.add(f.getName()+":"+property.title());
//					}
//				}else {
//					if(LOGGER.isWarnEnabled()) {
//						LOGGER.warn("枚举:{},字段:{}未添加@Schema注解",type,f.getName());
//					}
//				}
//			}
			if(!des.isEmpty()) {
				return "Enum ("+StringUtils.listToString(des)+")";
			}
		}
		return null;
	}
	
}
