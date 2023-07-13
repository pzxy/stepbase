package com.base.core.framework.sql.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitee.magic.core.annotations.Column;
import com.gitee.magic.core.annotations.Transient;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.utils.ChangeCharUtils;
import com.gitee.magic.core.utils.ContextSupportType;
import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.framework.base.utils.SchemaEnumUtils;
import com.gitee.magic.jdbc.persistence.EntityDefinitionException;
import com.gitee.magic.jdbc.persistence.annotation.GeneratedValue;
import com.gitee.magic.jdbc.persistence.annotation.Id;
import com.gitee.magic.jdbc.persistence.annotation.Table;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.enums.EngineType;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.FullText;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.FullTextUnion;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.NormalUnion;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.UniqueUnion;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldBoolean;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldChar;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDate;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDateTime;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldDouble;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldEnum;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldFloat;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldInteger;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldJson;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldLong;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldSmallInt;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldTime;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldTimestamp;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.converter.FieldVarChar;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.AnnotationDefInfo;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.BaseColumnFieldScriptDef;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.FieldDefInfo;
import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.FieldIndexesDefInfo;

/**
 * 分表分库环境下不可用
 * @author user
 * 表相关信息导出Excel请使用以下语句
 *<pre>
	-------表信息
	SELECT 
		TABLE_NAME AS 表名,
		TABLE_COMMENT AS 描述
	FROM
		INFORMATION_SCHEMA.TABLES
	WHERE
		table_schema = '数据库名称';
	-------表结构信息
	SELECT
		table_name AS 表名,
		COLUMN_NAME AS 列名,
		COLUMN_TYPE AS 数据类型,
		DATA_TYPE AS 字段类型,
		CHARACTER_MAXIMUM_LENGTH AS 长度,
		IS_NULLABLE AS 是否为空,
		COLUMN_DEFAULT AS 默认值,
		COLUMN_COMMENT AS 备注
	FROM
		INFORMATION_SCHEMA.COLUMNS
	WHERE
		table_schema = '数据库名称';
	-------表索引约束信息
	SELECT
		*
	FROM
		INFORMATION_SCHEMA.INDEX_STATISTICS 
	WHERE
		table_name = '数据表名称';
 *</pre>
 */
public class ScriptDelegate {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final Map<Class<?>, Class<? extends BaseColumnFieldScriptDef>> TYPEMAP = new HashMap<>();

	static {
		TYPEMAP.put(byte.class, FieldVarChar.class);
		TYPEMAP.put(char.class, FieldChar.class);
		TYPEMAP.put(boolean.class, FieldBoolean.class);
		TYPEMAP.put(short.class, FieldSmallInt.class);
		TYPEMAP.put(int.class, FieldInteger.class);
		TYPEMAP.put(long.class, FieldLong.class);
		TYPEMAP.put(float.class,FieldFloat.class);
		TYPEMAP.put(double.class, FieldDouble.class);
		TYPEMAP.put(Byte.class, FieldVarChar.class);
		TYPEMAP.put(Character.class, FieldChar.class);
		TYPEMAP.put(Boolean.class, FieldBoolean.class);
		TYPEMAP.put(Short.class, FieldSmallInt.class);
		TYPEMAP.put(Integer.class, FieldInteger.class);
		TYPEMAP.put(Long.class, FieldLong.class);
		TYPEMAP.put(Float.class, FieldFloat.class);
		TYPEMAP.put(Double.class, FieldDouble.class);
		TYPEMAP.put(BigInteger.class, FieldVarChar.class);
		//TODO:等稳定后调整为FieldDecimal
//		types.put(BigDecimal.class, FieldDecimal.class);
		TYPEMAP.put(BigDecimal.class, FieldVarChar.class);
		TYPEMAP.put(String.class,FieldVarChar.class);
		TYPEMAP.put(StringBuffer.class, FieldVarChar.class);
		TYPEMAP.put(StringBuilder.class, FieldVarChar.class);
		TYPEMAP.put(byte[].class, FieldVarChar.class);
		TYPEMAP.put(char[].class, FieldVarChar.class);
		TYPEMAP.put(boolean[].class, FieldVarChar.class);
		TYPEMAP.put(short[].class, FieldVarChar.class);
		TYPEMAP.put(int[].class, FieldVarChar.class);
		TYPEMAP.put(long[].class, FieldVarChar.class);
		TYPEMAP.put(float[].class,FieldVarChar.class);
		TYPEMAP.put(double[].class, FieldVarChar.class);
		TYPEMAP.put(Byte[].class, FieldVarChar.class);
		TYPEMAP.put(Character[].class, FieldVarChar.class);
		TYPEMAP.put(Boolean[].class, FieldVarChar.class);
		TYPEMAP.put(Short[].class, FieldVarChar.class);
		TYPEMAP.put(Integer[].class, FieldVarChar.class);
		TYPEMAP.put(Long[].class, FieldVarChar.class);
		TYPEMAP.put(Float[].class,FieldVarChar.class);
		TYPEMAP.put(Double[].class, FieldVarChar.class);
		TYPEMAP.put(BigInteger[].class, FieldVarChar.class);
		TYPEMAP.put(BigDecimal[].class, FieldVarChar.class);
		TYPEMAP.put(String[].class,FieldVarChar.class);
		TYPEMAP.put(JsonObject.class, FieldJson.class);
		TYPEMAP.put(JsonArray.class, FieldJson.class);
		TYPEMAP.put(Properties.class, FieldJson.class);
		TYPEMAP.put(LocalDateTime.class,FieldDateTime.class);
		TYPEMAP.put(LocalTime.class,FieldTime.class);
		TYPEMAP.put(LocalDate.class,FieldDate.class);
		TYPEMAP.put(java.util.Date.class,FieldDateTime.class);
		TYPEMAP.put(java.sql.Time.class,FieldTime.class);
		TYPEMAP.put(java.sql.Date.class,FieldDate.class);
		TYPEMAP.put(java.sql.Timestamp.class,FieldTimestamp.class);
	}
	
	public static void setType(Class<?> type,Class<? extends BaseColumnFieldScriptDef> def) {
		TYPEMAP.put(type, def);
	}
	
	public static BaseColumnFieldScriptDef getType(Class<?> type) {
		if (type.isEnum()) {
			return new FieldEnum(type);
		} else {
			if(!TYPEMAP.containsKey(type)) {
				//默认字段脚本为varchar
//				throw new ApplicationException("无法生成表解析类型为："+type+"-SqlTable脚本生成失败");
				return ReflectUtils.newInstance(FieldVarChar.class);
			}
			return ReflectUtils.newInstance(TYPEMAP.get(type));
		}
	}
	
	/**
	 * 获取当前类的表名
	 * @param prototype
	 * @return
	 * @throws EntityDefinitionException
	 */
	public Table getTable(Class<?> prototype) throws EntityDefinitionException {
		// 获取表名
		Table table = prototype.getAnnotation(Table.class);
		if (table == null) {
			throw new EntityDefinitionException("Class<" + prototype + ">,未定义@Table注解，找不到映射的表名");
		}
		return table;
	}

	/**
	 * 生成表结构定义
	 * 
	 * @param prototype
	 */
	public String buildTableScript(Class<?> prototype) throws EntityDefinitionException {
		Table table=getTable(prototype);
		//获取表定义
		TableDef tableDef=prototype.getAnnotation(TableDef.class);
		Class<?> classes = prototype;
		List<String> fieldScriptLists = new ArrayList<>();
		List<String> primaryScriptLists = new ArrayList<>();
		List<String> indexesScriptLists = new ArrayList<>();
		while (!classes.equals(Object.class)) {
			Field[] fields = classes.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String fieldName = field.getName();
				// 不生成当前表字段跳过
				Transient trans = field.getAnnotation(Transient.class);
				if (trans != null) {
					continue;
				}
				Class<?> dataType = field.getType();

				String tableFieldName = "";
				Id idA = field.getAnnotation(Id.class);
				if (idA != null) {
					if (!ContextSupportType.isSupportTypeCheckByPrimaryKey(dataType)) {
						throw new EntityDefinitionException(
								"Class<" + classes + ">,@Id注解的字段:" + fieldName + "不支持" + dataType + "类型");
					}
				}
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					tableFieldName = column.value();
				}else {
					tableFieldName = fieldName;
					if(table.fieldNameToUnderline()) {
						tableFieldName =ChangeCharUtils.camelToUnderline(tableFieldName, 1);
					}
				}
//				String mTableFieldName="".equals(tableFieldName.trim()) ? table.fieldPrefix()+fieldName : table.fieldPrefix()+tableFieldName;
				String mTableFieldName=table.fieldPrefix()+tableFieldName;
				if(field.isAnnotationPresent(Id.class)) {
					primaryScriptLists.add(String.format("PRIMARY KEY (`%s`)", mTableFieldName));
				}
				//生成字段定义
				StringBuilder fieldScripts = new StringBuilder();
				fieldScripts.append(String.format("`%s`", mTableFieldName));
				fieldScripts.append(" ");
				BaseColumnFieldScriptDef type=null;
				ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
				if (converter != null) {
					type=ReflectUtils.newInstance(converter.value());
				}else {
					type=getType(field.getType());
				}
				//获取列定义
				ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
				if(columnDef!=null) {
					//字段类型
					if(columnDef.length()>0) {
						fieldScripts.append(type.buildScripts(columnDef.length(),columnDef.decimal()));
					}else {
						fieldScripts.append(type.buildScripts());
					}
					//是否允许为Null
					if(!columnDef.isNull()) {
						fieldScripts.append(" NOT NULL");
					}
					//默认值
					if(!StringUtils.isEmpty(columnDef.defaultValue())) {
						fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
					}
					//更多信息
					if(!StringUtils.isEmpty(columnDef.more())) {
						fieldScripts.append(String.format(" %s",columnDef.more()));
					}
					if(idA!=null) {
						if(idA.value()==GeneratedValue.AUTO) {
							fieldScripts.append(" AUTO_INCREMENT");
						}
					}
					//注释
					String comment=columnDef.comment();
					if(!StringUtils.isEmpty(comment)){
						comment=comment+getEnumComment(field.getType());
						fieldScripts.append(String.format(" COMMENT '%s'", comment));
					}
					for(Normal param:columnDef.indexes().normal()) {
						StringBuilder indexesFieldScripts = new StringBuilder();
						indexesFieldScripts.append("KEY");
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="normal_"+mTableFieldName;
						}
						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
						indexesFieldScripts.append(" USING "+param.method());
						indexesScriptLists.add(indexesFieldScripts.toString());
					}
					for(Unique param:columnDef.indexes().unique()) {
						StringBuilder indexesFieldScripts = new StringBuilder();
						indexesFieldScripts.append("Unique KEY");
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="unique_"+mTableFieldName;
						}
						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
						indexesFieldScripts.append(" USING "+param.method());
						indexesScriptLists.add(indexesFieldScripts.toString());
					}
					for(FullText param:columnDef.indexes().fullText()) {
						StringBuilder indexesFieldScripts = new StringBuilder();
						indexesFieldScripts.append("FullText KEY");
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="fulltext_"+mTableFieldName;
						}
						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
						indexesScriptLists.add(indexesFieldScripts.toString());
					}
				}else {
					if(LOGGER.isWarnEnabled()) {
						LOGGER.warn("{} 字段 {} 未定义@ColumnDef",table.value(),mTableFieldName);
					}
					fieldScripts.append(type.buildScripts());
					fieldScripts.append(" NOT NULL");
				}
				fieldScriptLists.add(fieldScripts.toString());
			}
			classes = classes.getSuperclass();
		}
		fieldScriptLists.addAll(primaryScriptLists);
		if(tableDef!=null) {
			for(NormalUnion param:tableDef.indexes().normal()) {
				StringBuilder indexesFieldScripts = new StringBuilder();
				indexesFieldScripts.append("KEY");
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="normal_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
				indexesFieldScripts.append(" USING "+param.method());
				indexesScriptLists.add(indexesFieldScripts.toString());
			}
			for(UniqueUnion param:tableDef.indexes().unique()) {
				StringBuilder indexesFieldScripts = new StringBuilder();
				indexesFieldScripts.append("Unique KEY");
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="unique_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
				indexesFieldScripts.append(" USING "+param.method());
				indexesScriptLists.add(indexesFieldScripts.toString());
			}
			for(FullTextUnion param:tableDef.indexes().fullText()) {
				StringBuilder indexesFieldScripts = new StringBuilder();
				indexesFieldScripts.append("FullText KEY");
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="fulltext_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
				indexesScriptLists.add(indexesFieldScripts.toString());
			}
		}
		fieldScriptLists.addAll(indexesScriptLists);
		StringBuilder tableScripts = new StringBuilder();
		tableScripts.append(String.format("CREATE TABLE `%s` (", table.value()));
		tableScripts.append(StringUtils.listToString(fieldScriptLists));
		tableScripts.append(")");
		if(tableDef!=null) {
			//定义
			tableScripts.append(" ENGINE="+tableDef.engine());
			if(!StringUtils.isEmpty(tableDef.charset())) {
				tableScripts.append(" DEFAULT");
				tableScripts.append(" CHARSET="+tableDef.charset());
				if(!StringUtils.isEmpty(tableDef.collate())) {
					tableScripts.append(" COLLATE="+tableDef.collate());
				}
			}
			if (!StringUtils.isEmpty(tableDef.comment())) {
				tableScripts.append(String.format(" COMMENT '%s'", tableDef.comment()));
			}
		}else {
			//默认
			tableScripts.append(" ENGINE="+EngineType.InnoDB);
		}
		tableScripts.append(";");
		return tableScripts.toString();
	}

	/**
	 * alter add添加表字段
	 * @param mTableFieldName
	 * @param field
	 * @return
	 */
	public String tableAlterAddScript(String tableName,String mTableFieldName,Field field) {
		//生成字段定义
		StringBuilder fieldScripts = new StringBuilder();
		fieldScripts.append("ALTER TABLE ").append(String.format("`%s`", tableName)).append(" ADD ").append(mTableFieldName).append(" ");
		BaseColumnFieldScriptDef type=null;
		ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
		if (converter != null) {
			type=ReflectUtils.newInstance(converter.value());
		}else {
			type=getType(field.getType());
		}
		//获取列定义
		ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
		if(columnDef!=null) {
			//字段类型
			if(columnDef.length()>0) {
				fieldScripts.append(type.buildScripts(columnDef.length(),columnDef.decimal()));
			}else {
				fieldScripts.append(type.buildScripts());
			}
			//是否允许为Null
			if(!columnDef.isNull()) {
				fieldScripts.append(" NOT NULL");
			}
			//默认值
			if(!StringUtils.isEmpty(columnDef.defaultValue())) {
				fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
			}
			//更多信息
			if(!StringUtils.isEmpty(columnDef.more())) {
				fieldScripts.append(String.format(" %s",columnDef.more()));
			}
			//注释
			String comment=columnDef.comment();
			if(!StringUtils.isEmpty(comment)){
				comment=comment+getEnumComment(field.getType());
				fieldScripts.append(String.format(" COMMENT '%s'", comment));
			}
		}else {
			fieldScripts.append(type.buildScripts());
			fieldScripts.append(" NOT NULL");
		}
		fieldScripts.append(";");
		return fieldScripts.toString();
	}
	
	
	/**
	 * alter change修改表字段
	 * @param mTableFieldName
	 * @param field
	 * @return
	 */
	public String tableAlterChangeScript(String tableName,String mTableFieldName,Field field,FieldDefInfo def) {
		Boolean changeFlag=false;
		//生成字段定义
		StringBuilder fieldScripts = new StringBuilder();
		fieldScripts.append("ALTER TABLE ")
		.append(String.format("`%s`", tableName))
		.append(" CHANGE ")
		.append(String.format("`%s`", mTableFieldName))
		.append(" ")
		.append(String.format("`%s`", mTableFieldName))
		.append(" ");
		BaseColumnFieldScriptDef type=null;
		ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
		if (converter != null) {
			type=ReflectUtils.newInstance(converter.value());
		}else {
			type=getType(field.getType());
		}
		//获取列定义
		ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
		if(columnDef!=null) {
			//字段类型
			String tempScripts=type.buildScripts(def.getLength(),def.getDecimal());
			String defaultScript=null;
			if(columnDef.length()>0) {
				defaultScript=type.buildScripts(columnDef.length(),columnDef.decimal());
			}else {
				//默认
				defaultScript=type.buildScripts();
			}
			if(!def.getTypeName().equalsIgnoreCase(String.valueOf(type.getDataType()))) {
				changeFlag=true;
			}else if(!defaultScript.equalsIgnoreCase(tempScripts)) {
				changeFlag=true;
			}
			fieldScripts.append(defaultScript);
			//是否允许为Null
			if(!columnDef.isNull()) {
				if(def.isNullable()) {
					changeFlag=true;
				}
				fieldScripts.append(" NOT NULL");
			}else {
				if(!def.isNullable()) {
					changeFlag=true;
				}
			}
			//默认值
			if(!StringUtils.isEmpty(columnDef.defaultValue())) {
				if(!(columnDef.defaultValue().equals(def.getDefaultValue())||
						columnDef.defaultValue().equals("'"+def.getDefaultValue()+"'"))) {
					changeFlag=true;
				}
				fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
			}else {
				if(!StringUtils.isEmpty(def.getDefaultValue())) {
					changeFlag=true;
				}
			}
			//更多信息
			if(!StringUtils.isEmpty(columnDef.more())) {
				fieldScripts.append(String.format(" %s",columnDef.more()));
			}
			//注释
			String comment=columnDef.comment();
			if(!StringUtils.isEmpty(comment)){
				comment=comment+getEnumComment(field.getType());
				if(!comment.equals((def.getRemark()))) {
					changeFlag=true;
				}
				fieldScripts.append(String.format(" COMMENT '%s'", comment));
			}else {
				if(!StringUtils.isEmpty((def.getRemark()))) {
					changeFlag=true;
				}
			}
		}else {
			if(def.isNullable()) {
				changeFlag=true;
			}
			if(!StringUtils.isEmpty(def.getDefaultValue())) {
				changeFlag=true;
			}
			if(!StringUtils.isEmpty(def.getRemark())) {
				changeFlag=true;
			}
			String tempScripts=type.buildScripts(def.getLength(),def.getDecimal());
			String defaultScript=type.buildScripts();
			fieldScripts.append(defaultScript);
			if(!def.getTypeName().equalsIgnoreCase(String.valueOf(type.getDataType()))) {
				changeFlag=true;
			} else if(!defaultScript.equalsIgnoreCase(tempScripts)) {
				changeFlag=true;
			}
			fieldScripts.append(" NOT NULL");
		}
		fieldScripts.append(";");
		return changeFlag?fieldScripts.toString():"";
	}

	/**
	 * alter add添加表字段
	 * @param tableName
	 * @param indexesName
	 * @param ann
	 * @param isDrop
	 * @return
	 */
	public String tableAlterAddIndexesScript(String tableName,String indexesName,AnnotationDefInfo ann,Boolean isDrop) {
		String columnNames=StringUtils.listToString(ann.getColumnName()).replace(",", "`,`");
		//生成字段定义
		StringBuilder fieldScripts = new StringBuilder();
		fieldScripts.append("ALTER TABLE ").append(String.format("`%s`", tableName));
		if(isDrop) {
			fieldScripts.append(" DROP INDEX ").append(String.format("`%s`", indexesName)).append(",");
		}
		fieldScripts.append(" ADD ").append(" ");
		if(ann.getAnnotation().annotationType()==Id.class) {
			fieldScripts.append("PRIMARY KEY("+columnNames+")");
		} else if(ann.getAnnotation().annotationType()==Normal.class||
				ann.getAnnotation().annotationType()==NormalUnion.class) {
			fieldScripts.append("INDEX ")
			.append(String.format("`%s`", indexesName))
			.append(" USING ");
			if(ann.getAnnotation().annotationType()==Normal.class) {
				fieldScripts.append(((Normal)ann.getAnnotation()).method());
			}else if(ann.getAnnotation().annotationType()==NormalUnion.class) {
				fieldScripts.append(((NormalUnion)ann.getAnnotation()).method());
			}
			fieldScripts.append(" ").append(String.format("(`%s`)", columnNames));
		} else if(ann.getAnnotation().annotationType()==Unique.class||
				ann.getAnnotation().annotationType()==UniqueUnion.class) {
			fieldScripts.append("UNIQUE ")
			.append(String.format("`%s`", indexesName))
			.append(" USING ");
			if(ann.getAnnotation().annotationType()==Unique.class) {
				fieldScripts.append(((Unique)ann.getAnnotation()).method());
			}else if(ann.getAnnotation().annotationType()==UniqueUnion.class) {
				fieldScripts.append(((UniqueUnion)ann.getAnnotation()).method());
			}
			fieldScripts.append(" ").append(String.format("(`%s`)", columnNames));
		} else if(ann.getAnnotation().annotationType()==FullText.class||
				ann.getAnnotation().annotationType()==FullTextUnion.class) {
			fieldScripts.append("fulltext "+indexesName+"("+columnNames+")");
		}
		fieldScripts.append(";");
		return fieldScripts.toString();
	}

	/**
	 * 解析表定义
	 * 	 不足：
	 * 	 1、无法自动生成修改了索引类型,索引方法的执行脚本
	 * @param conn
	 * @param prototype
	 * @throws Exception
	 */
	public void tableAnalysis(Connection conn,Class<?> prototype) throws Exception {
		TableDef tableDef=prototype.getAnnotation(TableDef.class);
		if(tableDef!=null) {
			if(tableDef.skip()) {
				return;
			}
		}else {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("{} 未定义@TableDef",prototype.getName());
			}
		}
		Table table=getTable(prototype);
		String tableName=table.value();
		Map<String,Field> fieldDef=new HashMap<>(100);
		Map<String,AnnotationDefInfo> fieldIndexesDef=new HashMap<>(100);
		if(tableDef!=null) {
			for(NormalUnion param:tableDef.indexes().normal()) {
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="normal_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				AnnotationDefInfo def=new AnnotationDefInfo();
				def.setColumnName(Arrays.asList(param.fields()));
				def.setAnnotation(param);
				fieldIndexesDef.put(indexesAlias, def);
			}
			for(UniqueUnion param:tableDef.indexes().unique()) {
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="unique_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				AnnotationDefInfo def=new AnnotationDefInfo();
				def.setColumnName(Arrays.asList(param.fields()));
				def.setAnnotation(param);
				fieldIndexesDef.put(indexesAlias, def);
			}
			for(FullTextUnion param:tableDef.indexes().fullText()) {
				String indexesAlias=param.value();
				if("".equals(param.value())) {
					indexesAlias="fulltext_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
				}
				AnnotationDefInfo def=new AnnotationDefInfo();
				def.setColumnName(Arrays.asList(param.fields()));
				def.setAnnotation(param);
				fieldIndexesDef.put(indexesAlias, def);
			}
		}
		Class<?> classes = prototype;
		while (!classes.equals(Object.class)) {
			Field[] fields = classes.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String fieldName = field.getName();
				// 不生成当前表字段跳过
				Transient trans = field.getAnnotation(Transient.class);
				if (trans != null) {
					continue;
				}
				Class<?> dataType = field.getType();

				String tableFieldName = "";
				Id idA = field.getAnnotation(Id.class);
				if (idA != null) {
					if (!ContextSupportType.isSupportTypeCheckByPrimaryKey(dataType)) {
						throw new EntityDefinitionException(
								"Class<" + classes + ">,@Id注解的字段:" + fieldName + "不支持" + dataType + "类型");
					}
				}
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					tableFieldName = column.value();
				}else {
					tableFieldName = fieldName;
					if(table.fieldNameToUnderline()) {
						tableFieldName =ChangeCharUtils.camelToUnderline(tableFieldName, 1);
					}
				}
//				String mTableFieldName="".equals(tableFieldName.trim()) ? table.fieldPrefix()+fieldName : table.fieldPrefix()+tableFieldName;
				String mTableFieldName=table.fieldPrefix()+tableFieldName;
				fieldDef.put(mTableFieldName,field);
				Id idd=field.getAnnotation(Id.class);
				if(idd!=null) {
					AnnotationDefInfo def=new AnnotationDefInfo();
					def.setColumnName(Arrays.asList(mTableFieldName));
					def.setAnnotation(idd);
					fieldIndexesDef.put("PRIMARY", def);
				}
				//获取列定义
				ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
				if(columnDef!=null) {
					for(Normal param:columnDef.indexes().normal()) {
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="normal_"+mTableFieldName;
						}
						AnnotationDefInfo def=new AnnotationDefInfo();
						def.setColumnName(Arrays.asList(mTableFieldName));
						def.setAnnotation(param);
						fieldIndexesDef.put(indexesAlias, def);
					}
					for(Unique param:columnDef.indexes().unique()) {
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="unique_"+mTableFieldName;
						}
						AnnotationDefInfo def=new AnnotationDefInfo();
						def.setColumnName(Arrays.asList(mTableFieldName));
						def.setAnnotation(param);
						fieldIndexesDef.put(indexesAlias, def);
					}
					for(FullText param:columnDef.indexes().fullText()) {
						String indexesAlias=param.value();
						if("".equals(param.value())) {
							indexesAlias="fulltext_"+mTableFieldName;
						}
						AnnotationDefInfo def=new AnnotationDefInfo();
						def.setColumnName(Arrays.asList(mTableFieldName));
						def.setAnnotation(param);
						fieldIndexesDef.put(indexesAlias, def);
					}
				}else {
					if(LOGGER.isWarnEnabled()) {
						LOGGER.warn("{} 字段 {} 未定义@ColumnDef",table.value(),mTableFieldName);
					}
				}
			}
			classes = classes.getSuperclass();
		}
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet columnRs = dbmd.getColumns(conn.getCatalog(), "%", tableName, "%");
		if(columnRs.next()) {
			//列
			Map<String,FieldDefInfo> tableColumnDef=new HashMap<>(100);
			do {
//				ResultSetMetaData rsmd = columnRs.getMetaData();
//				for(int i=0;i<rsmd.getColumnCount();i++) {
//					String name=rsmd.getColumnName(i+1);
//					System.out.println(name+"        "+columnRs.getString(name));
//				}
				FieldDefInfo def=new FieldDefInfo();
				def.setColumnName(columnRs.getString("COLUMN_NAME"));
				def.setTypeName(columnRs.getString("TYPE_NAME"));
				def.setLength(columnRs.getInt("COLUMN_SIZE"));
				def.setDecimal(columnRs.getInt("DECIMAL_DIGITS"));
				def.setNullable("1".equals(columnRs.getString("NULLABLE")));
				def.setDefaultValue(columnRs.getString("COLUMN_DEF"));
				def.setRemark(columnRs.getString("REMARKS"));
				tableColumnDef.put(def.getColumnName(),def);
			}while(columnRs.next());
			for(String mTableFieldName:fieldDef.keySet()) {
				if(tableColumnDef.containsKey(mTableFieldName)) {
					Field field=fieldDef.get(mTableFieldName);
						FieldDefInfo def=tableColumnDef.get(mTableFieldName);
	//					System.out.println("字段名："+def.getColumnName());
	//					System.out.println("类型名："+def.getTypeName());
	//					System.out.println("列大小："+def.getLength());
	//					System.out.println("列小数："+def.getDecimal());
	//					System.out.println("是否为空："+def.isNullable());
	//					System.out.println("默认值："+def.getDefaultValue());
	//					System.out.println("备注："+def.getRemark());
					String scripts=tableAlterChangeScript(tableName,mTableFieldName,field,def);
					if(!StringUtils.isEmpty(scripts)) {
						//为空则表示字段无更改
						System.out.println(scripts);
					}
				}else {
					System.out.println(tableAlterAddScript(tableName,mTableFieldName,fieldDef.get(mTableFieldName)));
				}
			}
			for(String mTableFieldName:tableColumnDef.keySet()) {
				if(!fieldDef.containsKey(mTableFieldName)) {
					//列字段不存在实体类中则删除
					System.out.println("ALTER TABLE "+String.format("`%s`", tableName)+" DROP "+String.format("`%s`", mTableFieldName)+";");
				}
			}
		}else {
			//没该表直接打印表语句
			System.out.println(buildTableScript(prototype));
			return;
		}
		columnRs.close();
		//索引
		ResultSet indexRs = dbmd.getIndexInfo(conn.getCatalog(), null, tableName, false, false);
		if(indexRs.next()) {
			Map<String,List<FieldIndexesDefInfo>> tableColumnIndexesDef=new HashMap<>(100);
			do {
				FieldIndexesDefInfo indexesDef=new FieldIndexesDefInfo();
				indexesDef.setColumnName(indexRs.getString("COLUMN_NAME"));
				indexesDef.setUnique( indexRs.getString("NON_UNIQUE"));
				indexesDef.setName(indexRs.getString("INDEX_NAME"));
				indexesDef.setType(indexRs.getString("TYPE"));
				List<FieldIndexesDefInfo> indexesDefs=tableColumnIndexesDef.get(indexesDef.getName());
				if(indexesDefs==null) {
					indexesDefs=new ArrayList<>();
				}
				indexesDefs.add(indexesDef);
				tableColumnIndexesDef.put(indexesDef.getName(), indexesDefs);
			}while(indexRs.next());
			for(String mFieldIndexesName:fieldIndexesDef.keySet()) {
				AnnotationDefInfo annDefInfo=fieldIndexesDef.get(mFieldIndexesName);
				if(tableColumnIndexesDef.containsKey(mFieldIndexesName)) {
					List<FieldIndexesDefInfo> indexesDefs=tableColumnIndexesDef.get(mFieldIndexesName);
					List<String> fieldIndexesDefNames=new ArrayList<>();
					for(FieldIndexesDefInfo def:indexesDefs) {
						fieldIndexesDefNames.add(def.getColumnName());
					}
					if(fieldIndexesDefNames.containsAll(annDefInfo.getColumnName())&&
							annDefInfo.getColumnName().containsAll(fieldIndexesDefNames)) {
						//表索引未调整
					}else {
						System.out.println(tableAlterAddIndexesScript(tableName,mFieldIndexesName,fieldIndexesDef.get(mFieldIndexesName),true));
					}
				}else {
					System.out.println(tableAlterAddIndexesScript(tableName,mFieldIndexesName,fieldIndexesDef.get(mFieldIndexesName),false));
				}
			}
			for(String mFieldIndexesName:tableColumnIndexesDef.keySet()) {
				if(!fieldIndexesDef.containsKey(mFieldIndexesName)) {
					//列字段不存在实体类中则删除
					System.out.println("ALTER TABLE "+String.format("`%s`", tableName)+" DROP INDEX "+String.format("`%s`", mFieldIndexesName)+";");
				}
			}
		}
		indexRs.close();
	}
	
	public String getEnumComment(Class<?> type) {
		StringBuilder sb=new StringBuilder();
		if(type.isEnum()) {
//			List<String> names=new ArrayList<>();
//			for(Field f:type.getFields()) {
//				Schema property=f.getAnnotation(Schema.class);
//				if(property!=null) {
//					names.add(property.title()+":"+f.getName());
//				}else {
//					names.add(f.getName());
//					if(LOGGER.isWarnEnabled()) {
//						LOGGER.warn("枚举:{},字段:{}未添加@Schema注解",type,f.getName());
//					}
//				}
//			}
			List<String> des=SchemaEnumUtils.get(type);
			sb.append("(");
			sb.append(StringUtils.listToString(des));
			sb.append(")");
		}
		return sb.toString();
	}
	
}
