//package com.base.core.framework.mybatis.utils;
//
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.gitee.magic.core.json.JsonArray;
//import com.gitee.magic.core.json.JsonObject;
//import com.gitee.magic.core.utils.ReflectUtils;
//import com.gitee.magic.core.utils.StringUtils;
//import com.gitee.magic.framework.base.utils.SchemaEnumUtils;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldBoolean;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldChar;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDate;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDateTime;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDouble;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldEnum;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldFloat;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldInteger;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldJson;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldLong;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldSmallInt;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldTime;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldTimestamp;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldVarChar;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.BaseColumnFieldScriptDef;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.FieldDefInfo;
//
//public class BaseScriptDelete {
//	
//	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
//
//	protected static final Map<Class<?>, Class<? extends BaseColumnFieldScriptDef>> TYPEMAP = new HashMap<>();
//
//	static {
//		TYPEMAP.put(byte.class, FieldVarChar.class);
//		TYPEMAP.put(char.class, FieldChar.class);
//		TYPEMAP.put(boolean.class, FieldBoolean.class);
//		TYPEMAP.put(short.class, FieldSmallInt.class);
//		TYPEMAP.put(int.class, FieldInteger.class);
//		TYPEMAP.put(long.class, FieldLong.class);
//		TYPEMAP.put(float.class,FieldFloat.class);
//		TYPEMAP.put(double.class, FieldDouble.class);
//		TYPEMAP.put(Byte.class, FieldVarChar.class);
//		TYPEMAP.put(Character.class, FieldChar.class);
//		TYPEMAP.put(Boolean.class, FieldBoolean.class);
//		TYPEMAP.put(Short.class, FieldSmallInt.class);
//		TYPEMAP.put(Integer.class, FieldInteger.class);
//		TYPEMAP.put(Long.class, FieldLong.class);
//		TYPEMAP.put(Float.class, FieldFloat.class);
//		TYPEMAP.put(Double.class, FieldDouble.class);
//		TYPEMAP.put(BigInteger.class, FieldVarChar.class);
//		//TODO:等稳定后调整为FieldDecimal
////		types.put(BigDecimal.class, FieldDecimal.class);
//		TYPEMAP.put(BigDecimal.class, FieldVarChar.class);
//		TYPEMAP.put(String.class,FieldVarChar.class);
//		TYPEMAP.put(StringBuffer.class, FieldVarChar.class);
//		TYPEMAP.put(StringBuilder.class, FieldVarChar.class);
//		TYPEMAP.put(byte[].class, FieldVarChar.class);
//		TYPEMAP.put(char[].class, FieldVarChar.class);
//		TYPEMAP.put(boolean[].class, FieldVarChar.class);
//		TYPEMAP.put(short[].class, FieldVarChar.class);
//		TYPEMAP.put(int[].class, FieldVarChar.class);
//		TYPEMAP.put(long[].class, FieldVarChar.class);
//		TYPEMAP.put(float[].class,FieldVarChar.class);
//		TYPEMAP.put(double[].class, FieldVarChar.class);
//		TYPEMAP.put(Byte[].class, FieldVarChar.class);
//		TYPEMAP.put(Character[].class, FieldVarChar.class);
//		TYPEMAP.put(Boolean[].class, FieldVarChar.class);
//		TYPEMAP.put(Short[].class, FieldVarChar.class);
//		TYPEMAP.put(Integer[].class, FieldVarChar.class);
//		TYPEMAP.put(Long[].class, FieldVarChar.class);
//		TYPEMAP.put(Float[].class,FieldVarChar.class);
//		TYPEMAP.put(Double[].class, FieldVarChar.class);
//		TYPEMAP.put(BigInteger[].class, FieldVarChar.class);
//		TYPEMAP.put(BigDecimal[].class, FieldVarChar.class);
//		TYPEMAP.put(String[].class,FieldVarChar.class);
//		TYPEMAP.put(JsonObject.class, FieldJson.class);
//		TYPEMAP.put(JsonArray.class, FieldJson.class);
//		TYPEMAP.put(Properties.class, FieldJson.class);
//		TYPEMAP.put(LocalDateTime.class,FieldDateTime.class);
//		TYPEMAP.put(LocalTime.class,FieldTime.class);
//		TYPEMAP.put(LocalDate.class,FieldDate.class);
//		TYPEMAP.put(java.util.Date.class,FieldDateTime.class);
//		TYPEMAP.put(java.sql.Time.class,FieldTime.class);
//		TYPEMAP.put(java.sql.Date.class,FieldDate.class);
//		TYPEMAP.put(java.sql.Timestamp.class,FieldTimestamp.class);
//	}
//	
//	public static void setType(Class<?> type,Class<? extends BaseColumnFieldScriptDef> def) {
//		TYPEMAP.put(type, def);
//	}
//	
//	public static BaseColumnFieldScriptDef getType(Class<?> type) {
//		if (type.isEnum()) {
//			return new FieldEnum(type);
//		} else {
//			if(!TYPEMAP.containsKey(type)) {
//				//默认字段脚本为varchar
////				throw new ApplicationException("无法生成表解析类型为："+type+"-SqlTable脚本生成失败");
//				return ReflectUtils.newInstance(FieldVarChar.class);
//			}
//			return ReflectUtils.newInstance(TYPEMAP.get(type));
//		}
//	}
//	
//	/**
//	 * alter add添加表字段
//	 * @param mTableFieldName
//	 * @param field
//	 * @return
//	 */
//	public String tableAlterAddScript(String tableName,String mTableFieldName,Field field) {
//		//生成字段定义
//		StringBuilder fieldScripts = new StringBuilder();
//		fieldScripts.append("ALTER TABLE ").append(String.format("`%s`", tableName)).append(" ADD ").append(mTableFieldName).append(" ");
//		BaseColumnFieldScriptDef type=null;
//		ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
//		if (converter != null) {
//			type=ReflectUtils.newInstance(converter.value());
//		}else {
//			type=getType(field.getType());
//		}
//		//获取列定义
//		ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
//		if(columnDef!=null) {
//			//字段类型
//			if(columnDef.length()>0) {
//				fieldScripts.append(type.buildScripts(columnDef.length(),columnDef.decimal()));
//			}else {
//				fieldScripts.append(type.buildScripts());
//			}
//			//是否允许为Null
//			if(!columnDef.isNull()) {
//				fieldScripts.append(" NOT NULL");
//			}
//			//默认值
//			if(!StringUtils.isEmpty(columnDef.defaultValue())) {
//				fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
//			}
//			//更多信息
//			if(!StringUtils.isEmpty(columnDef.more())) {
//				fieldScripts.append(String.format(" %s",columnDef.more()));
//			}
//			//注释
//			String comment=columnDef.comment();
//			if(!StringUtils.isEmpty(comment)){
//				comment=comment+getEnumComment(field.getType());
//				fieldScripts.append(String.format(" COMMENT '%s'", comment));
//			}
//		}else {
//			fieldScripts.append(type.buildScripts());
//			fieldScripts.append(" NOT NULL");
//		}
//		fieldScripts.append(";");
//		return fieldScripts.toString();
//	}
//	
//	
//	/**
//	 * alter change修改表字段
//	 * @param mTableFieldName
//	 * @param field
//	 * @return
//	 */
//	public String tableAlterChangeScript(String tableName,String mTableFieldName,Field field,FieldDefInfo def) {
//		Boolean changeFlag=false;
//		//生成字段定义
//		StringBuilder fieldScripts = new StringBuilder();
//		fieldScripts.append("ALTER TABLE ")
//		.append(String.format("`%s`", tableName))
//		.append(" CHANGE ")
//		.append(String.format("`%s`", mTableFieldName))
//		.append(" ")
//		.append(String.format("`%s`", mTableFieldName))
//		.append(" ");
//		BaseColumnFieldScriptDef type=null;
//		ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
//		if (converter != null) {
//			type=ReflectUtils.newInstance(converter.value());
//		}else {
//			type=getType(field.getType());
//		}
//		//获取列定义
//		ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
//		if(columnDef!=null) {
//			//字段类型
//			String tempScripts=type.buildScripts(def.getLength(),def.getDecimal());
//			String defaultScript=null;
//			if(columnDef.length()>0) {
//				defaultScript=type.buildScripts(columnDef.length(),columnDef.decimal());
//			}else {
//				//默认
//				defaultScript=type.buildScripts();
//			}
//			if(!def.getTypeName().equalsIgnoreCase(String.valueOf(type.getDataType()))) {
//				changeFlag=true;
//			}else if(!defaultScript.equalsIgnoreCase(tempScripts)) {
//				changeFlag=true;
//			}
//			fieldScripts.append(defaultScript);
//			//是否允许为Null
//			if(!columnDef.isNull()) {
//				if(def.isNullable()) {
//					changeFlag=true;
//				}
//				fieldScripts.append(" NOT NULL");
//			}else {
//				if(!def.isNullable()) {
//					changeFlag=true;
//				}
//			}
//			//默认值
//			if(!StringUtils.isEmpty(columnDef.defaultValue())) {
//				if(!(columnDef.defaultValue().equals(def.getDefaultValue())||
//						columnDef.defaultValue().equals("'"+def.getDefaultValue()+"'"))) {
//					changeFlag=true;
//				}
//				fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
//			}else {
//				if(!StringUtils.isEmpty(def.getDefaultValue())) {
//					changeFlag=true;
//				}
//			}
//			//更多信息
//			if(!StringUtils.isEmpty(columnDef.more())) {
//				fieldScripts.append(String.format(" %s",columnDef.more()));
//			}
//			//注释
//			String comment=columnDef.comment();
//			if(!StringUtils.isEmpty(comment)){
//				comment=comment+getEnumComment(field.getType());
//				if(!comment.equals((def.getRemark()))) {
//					changeFlag=true;
//				}
//				fieldScripts.append(String.format(" COMMENT '%s'", comment));
//			}else {
//				if(!StringUtils.isEmpty((def.getRemark()))) {
//					changeFlag=true;
//				}
//			}
//		}else {
//			if(def.isNullable()) {
//				changeFlag=true;
//			}
//			if(!StringUtils.isEmpty(def.getDefaultValue())) {
//				changeFlag=true;
//			}
//			if(!StringUtils.isEmpty(def.getRemark())) {
//				changeFlag=true;
//			}
//			String tempScripts=type.buildScripts(def.getLength(),def.getDecimal());
//			String defaultScript=type.buildScripts();
//			fieldScripts.append(defaultScript);
//			if(!def.getTypeName().equalsIgnoreCase(String.valueOf(type.getDataType()))) {
//				changeFlag=true;
//			} else if(!defaultScript.equalsIgnoreCase(tempScripts)) {
//				changeFlag=true;
//			}
//			fieldScripts.append(" NOT NULL");
//		}
//		fieldScripts.append(";");
//		return changeFlag?fieldScripts.toString():"";
//	}
//	
//	
//	public String getEnumComment(Class<?> type) {
//		StringBuilder sb=new StringBuilder();
//		if(type.isEnum()) {
////			List<String> names=new ArrayList<>();
////			for(Field f:type.getFields()) {
////				Schema property=f.getAnnotation(Schema.class);
////				if(property!=null) {
////					names.add(property.title()+":"+f.getName());
////				}else {
////					names.add(f.getName());
////					if(LOGGER.isWarnEnabled()) {
////						LOGGER.warn("枚举:{},字段:{}未添加@Schema注解",type,f.getName());
////					}
////				}
////			}
//			List<String> des=SchemaEnumUtils.get(type);
//			sb.append("(");
//			sb.append(StringUtils.listToString(des));
//			sb.append(")");
//		}
//		return sb.toString();
//	}
//	
//}
