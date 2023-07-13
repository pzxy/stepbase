//package com.base.core.framework.mybatis.utils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.gitee.magic.core.utils.ContextSupportType;
//import com.gitee.magic.core.utils.ReflectUtils;
//import com.gitee.magic.core.utils.StringUtils;
//import com.gitee.magic.jdbc.persistence.EntityDefinitionException;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ColumnDef;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.ScriptConverter;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.TableDef;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.enums.EngineType;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.FullText;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.FullTextUnion;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Normal;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.NormalUnion;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.Unique;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.annotations.indexes.UniqueUnion;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.AnnotationDefInfo;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.BaseColumnFieldScriptDef;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.FieldDefInfo;
//import com.gitee.magic.jdbc.persistence.source.jdbc.script.def.FieldIndexesDefInfo;
//
///**
// * 分表分库环境下不可用
// * @author user
// * 表相关信息导出Excel请使用以下语句
// *<pre>
//	-------表信息
//	SELECT 
//		TABLE_NAME AS 表名,
//		TABLE_COMMENT AS 描述
//	FROM
//		INFORMATION_SCHEMA.TABLES
//	WHERE
//		table_schema = '数据库名称';
//	-------表结构信息
//	SELECT
//		table_name AS 表名,
//		COLUMN_NAME AS 列名,
//		COLUMN_TYPE AS 数据类型,
//		DATA_TYPE AS 字段类型,
//		CHARACTER_MAXIMUM_LENGTH AS 长度,
//		IS_NULLABLE AS 是否为空,
//		COLUMN_DEFAULT AS 默认值,
//		COLUMN_COMMENT AS 备注
//	FROM
//		INFORMATION_SCHEMA.COLUMNS
//	WHERE
//		table_schema = '数据库名称';
//	-------表索引约束信息
//	SELECT
//		*
//	FROM
//		INFORMATION_SCHEMA.INDEX_STATISTICS 
//	WHERE
//		table_name = '数据表名称';
// *</pre>
// */
//public class MybatisScriptDelegate extends BaseScriptDelete {
//
//	/**
//	 * 获取当前类的表名
//	 * @param prototype
//	 * @return
//	 * @throws EntityDefinitionException
//	 */
//	public TableName getTable(Class<?> prototype) throws EntityDefinitionException {
//		// 获取表名
//		TableName table = prototype.getAnnotation(TableName.class);
//		if (table == null) {
//			throw new EntityDefinitionException("Class<" + prototype + ">,未定义@Table注解，找不到映射的表名");
//		}
//		return table;
//	}
//
//	/**
//	 * 生成表结构定义
//	 * 
//	 * @param prototype
//	 */
//	public String buildTableScript(Class<?> prototype) throws EntityDefinitionException {
//		TableName table=getTable(prototype);
//		//获取表定义
//		TableDef tableDef=prototype.getAnnotation(TableDef.class);
//		Class<?> classes = prototype;
//		List<String> fieldScriptLists = new ArrayList<>();
//		List<String> primaryScriptLists = new ArrayList<>();
//		List<String> indexesScriptLists = new ArrayList<>();
//		while (!classes.equals(Object.class)) {
//			Field[] fields = classes.getDeclaredFields();
//			for (Field field : fields) {
//				if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
//					continue;
//				}
//				String fieldName = field.getName();
//				Class<?> dataType = field.getType();
//
//				String tableFieldName = "";
//				TableId idA = field.getAnnotation(TableId.class);
//				if (idA != null) {
//					if (!ContextSupportType.isSupportTypeCheckByPrimaryKey(dataType)) {
//						throw new EntityDefinitionException(
//								"Class<" + classes + ">,@Id注解的字段:" + fieldName + "不支持" + dataType + "类型");
//					}
//				}
//				TableField column = field.getAnnotation(TableField.class);
//				if (column != null) {
//					// 不生成当前表字段跳过
//					if(!column.exist()) {
//						continue;
//					}
//					tableFieldName = column.value();
//				}else {
//					tableFieldName = fieldName;
//				}
////				String mTableFieldName="".equals(tableFieldName.trim()) ? table.fieldPrefix()+fieldName : table.fieldPrefix()+tableFieldName;
//				String mTableFieldName=tableFieldName;
//				if(field.isAnnotationPresent(TableId.class)) {
//					primaryScriptLists.add(String.format("PRIMARY KEY (`%s`)", mTableFieldName));
//				}
//				//生成字段定义
//				StringBuilder fieldScripts = new StringBuilder();
//				fieldScripts.append(String.format("`%s`", mTableFieldName));
//				fieldScripts.append(" ");
//				BaseColumnFieldScriptDef type=null;
//				ScriptConverter converter = field.getAnnotation(ScriptConverter.class);
//				if (converter != null) {
//					type=ReflectUtils.newInstance(converter.value());
//				}else {
//					type=getType(field.getType());
//				}
//				//获取列定义
//				ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
//				if(columnDef!=null) {
//					//字段类型
//					if(columnDef.length()>0) {
//						fieldScripts.append(type.buildScripts(columnDef.length(),columnDef.decimal()));
//					}else {
//						fieldScripts.append(type.buildScripts());
//					}
//					//是否允许为Null
//					if(!columnDef.isNull()) {
//						fieldScripts.append(" NOT NULL");
//					}
//					//默认值
//					if(!StringUtils.isEmpty(columnDef.defaultValue())) {
//						fieldScripts.append(String.format(" DEFAULT %s",columnDef.defaultValue()));
//					}
//					//更多信息
//					if(!StringUtils.isEmpty(columnDef.more())) {
//						fieldScripts.append(String.format(" %s",columnDef.more()));
//					}
//					if(idA!=null) {
//						if(idA.type()==IdType.AUTO) {
//							fieldScripts.append(" AUTO_INCREMENT");
//						}
//					}
//					//注释
//					String comment=columnDef.comment();
//					if(!StringUtils.isEmpty(comment)){
//						comment=comment+getEnumComment(field.getType());
//						fieldScripts.append(String.format(" COMMENT '%s'", comment));
//					}
//					for(Normal param:columnDef.indexes().normal()) {
//						StringBuilder indexesFieldScripts = new StringBuilder();
//						indexesFieldScripts.append("KEY");
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="normal_"+mTableFieldName;
//						}
//						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
//						indexesFieldScripts.append(" USING "+param.method());
//						indexesScriptLists.add(indexesFieldScripts.toString());
//					}
//					for(Unique param:columnDef.indexes().unique()) {
//						StringBuilder indexesFieldScripts = new StringBuilder();
//						indexesFieldScripts.append("Unique KEY");
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="unique_"+mTableFieldName;
//						}
//						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
//						indexesFieldScripts.append(" USING "+param.method());
//						indexesScriptLists.add(indexesFieldScripts.toString());
//					}
//					for(FullText param:columnDef.indexes().fullText()) {
//						StringBuilder indexesFieldScripts = new StringBuilder();
//						indexesFieldScripts.append("FullText KEY");
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="fulltext_"+mTableFieldName;
//						}
//						indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,mTableFieldName));
//						indexesScriptLists.add(indexesFieldScripts.toString());
//					}
//				}else {
//					if(LOGGER.isWarnEnabled()) {
//						LOGGER.warn("{} 字段 {} 未定义@ColumnDef",table.value(),mTableFieldName);
//					}
//					fieldScripts.append(type.buildScripts());
//					fieldScripts.append(" NOT NULL");
//				}
//				fieldScriptLists.add(fieldScripts.toString());
//			}
//			classes = classes.getSuperclass();
//		}
//		fieldScriptLists.addAll(primaryScriptLists);
//		if(tableDef!=null) {
//			for(NormalUnion param:tableDef.indexes().normal()) {
//				StringBuilder indexesFieldScripts = new StringBuilder();
//				indexesFieldScripts.append("KEY");
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="normal_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
//				indexesFieldScripts.append(" USING "+param.method());
//				indexesScriptLists.add(indexesFieldScripts.toString());
//			}
//			for(UniqueUnion param:tableDef.indexes().unique()) {
//				StringBuilder indexesFieldScripts = new StringBuilder();
//				indexesFieldScripts.append("Unique KEY");
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="unique_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
//				indexesFieldScripts.append(" USING "+param.method());
//				indexesScriptLists.add(indexesFieldScripts.toString());
//			}
//			for(FullTextUnion param:tableDef.indexes().fullText()) {
//				StringBuilder indexesFieldScripts = new StringBuilder();
//				indexesFieldScripts.append("FullText KEY");
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="fulltext_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				indexesFieldScripts.append(String.format(" `%s`(`%s`)",indexesAlias,indexesAlias,StringUtils.listToString(Arrays.asList(param.fields())).replace(",", "`,`")));
//				indexesScriptLists.add(indexesFieldScripts.toString());
//			}
//		}
//		fieldScriptLists.addAll(indexesScriptLists);
//		StringBuilder tableScripts = new StringBuilder();
//		tableScripts.append(String.format("CREATE TABLE `%s` (", table.value()));
//		tableScripts.append(StringUtils.listToString(fieldScriptLists));
//		tableScripts.append(")");
//		if(tableDef!=null) {
//			//定义
//			tableScripts.append(" ENGINE="+tableDef.engine());
//			if(!StringUtils.isEmpty(tableDef.charset())) {
//				tableScripts.append(" DEFAULT");
//				tableScripts.append(" CHARSET="+tableDef.charset());
//				if(!StringUtils.isEmpty(tableDef.collate())) {
//					tableScripts.append(" COLLATE="+tableDef.collate());
//				}
//			}
//			if (!StringUtils.isEmpty(tableDef.comment())) {
//				tableScripts.append(String.format(" COMMENT '%s'", tableDef.comment()));
//			}
//		}else {
//			//默认
//			tableScripts.append(" ENGINE="+EngineType.InnoDB);
//		}
//		tableScripts.append(";");
//		return tableScripts.toString();
//	}
//
//	/**
//	 * alter add添加表字段
//	 * @param tableName
//	 * @param indexesName
//	 * @param ann
//	 * @param isDrop
//	 * @return
//	 */
//	public String tableAlterAddIndexesScript(String tableName,String indexesName,AnnotationDefInfo ann,Boolean isDrop) {
//		String columnNames=StringUtils.listToString(ann.getColumnName()).replace(",", "`,`");
//		//生成字段定义
//		StringBuilder fieldScripts = new StringBuilder();
//		fieldScripts.append("ALTER TABLE ").append(String.format("`%s`", tableName));
//		if(isDrop) {
//			fieldScripts.append(" DROP INDEX ").append(String.format("`%s`", indexesName)).append(",");
//		}
//		fieldScripts.append(" ADD ").append(" ");
//		if(ann.getAnnotation().annotationType()==TableId.class) {
//			fieldScripts.append("PRIMARY KEY("+columnNames+")");
//		} else if(ann.getAnnotation().annotationType()==Normal.class||
//				ann.getAnnotation().annotationType()==NormalUnion.class) {
//			fieldScripts.append("INDEX ")
//			.append(String.format("`%s`", indexesName))
//			.append(" USING ");
//			if(ann.getAnnotation().annotationType()==Normal.class) {
//				fieldScripts.append(((Normal)ann.getAnnotation()).method());
//			}else if(ann.getAnnotation().annotationType()==NormalUnion.class) {
//				fieldScripts.append(((NormalUnion)ann.getAnnotation()).method());
//			}
//			fieldScripts.append(" ").append(String.format("(`%s`)", columnNames));
//		} else if(ann.getAnnotation().annotationType()==Unique.class||
//				ann.getAnnotation().annotationType()==UniqueUnion.class) {
//			fieldScripts.append("UNIQUE ")
//			.append(String.format("`%s`", indexesName))
//			.append(" USING ");
//			if(ann.getAnnotation().annotationType()==Unique.class) {
//				fieldScripts.append(((Unique)ann.getAnnotation()).method());
//			}else if(ann.getAnnotation().annotationType()==UniqueUnion.class) {
//				fieldScripts.append(((UniqueUnion)ann.getAnnotation()).method());
//			}
//			fieldScripts.append(" ").append(String.format("(`%s`)", columnNames));
//		} else if(ann.getAnnotation().annotationType()==FullText.class||
//				ann.getAnnotation().annotationType()==FullTextUnion.class) {
//			fieldScripts.append("fulltext "+indexesName+"("+columnNames+")");
//		}
//		fieldScripts.append(";");
//		return fieldScripts.toString();
//	}
//
//	/**
//	 * 解析表定义
//	 * 	 不足：
//	 * 	 1、无法自动生成修改了索引类型,索引方法的执行脚本
//	 * @param conn
//	 * @param prototype
//	 * @throws Exception
//	 */
//	public void tableAnalysis(Connection conn,Class<?> prototype) throws Exception {
//		TableDef tableDef=prototype.getAnnotation(TableDef.class);
//		if(tableDef!=null) {
//			if(tableDef.skip()) {
//				return;
//			}
//		}else {
//			if(LOGGER.isWarnEnabled()) {
//				LOGGER.warn("{} 未定义@TableDef",prototype.getName());
//			}
//		}
//		TableName table=getTable(prototype);
//		String tableName=table.value();
//		Map<String,Field> fieldDef=new HashMap<>(100);
//		Map<String,AnnotationDefInfo> fieldIndexesDef=new HashMap<>(100);
//		if(tableDef!=null) {
//			for(NormalUnion param:tableDef.indexes().normal()) {
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="normal_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				AnnotationDefInfo def=new AnnotationDefInfo();
//				def.setColumnName(Arrays.asList(param.fields()));
//				def.setAnnotation(param);
//				fieldIndexesDef.put(indexesAlias, def);
//			}
//			for(UniqueUnion param:tableDef.indexes().unique()) {
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="unique_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				AnnotationDefInfo def=new AnnotationDefInfo();
//				def.setColumnName(Arrays.asList(param.fields()));
//				def.setAnnotation(param);
//				fieldIndexesDef.put(indexesAlias, def);
//			}
//			for(FullTextUnion param:tableDef.indexes().fullText()) {
//				String indexesAlias=param.value();
//				if("".equals(param.value())) {
//					indexesAlias="fulltext_"+StringUtils.listToString(Arrays.asList(param.fields()),"_");
//				}
//				AnnotationDefInfo def=new AnnotationDefInfo();
//				def.setColumnName(Arrays.asList(param.fields()));
//				def.setAnnotation(param);
//				fieldIndexesDef.put(indexesAlias, def);
//			}
//		}
//		Class<?> classes = prototype;
//		while (!classes.equals(Object.class)) {
//			Field[] fields = classes.getDeclaredFields();
//			for (Field field : fields) {
//				if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
//					continue;
//				}
//				String fieldName = field.getName();
//				Class<?> dataType = field.getType();
//
//				String tableFieldName = "";
//				TableId idA = field.getAnnotation(TableId.class);
//				if (idA != null) {
//					if (!ContextSupportType.isSupportTypeCheckByPrimaryKey(dataType)) {
//						throw new EntityDefinitionException(
//								"Class<" + classes + ">,@Id注解的字段:" + fieldName + "不支持" + dataType + "类型");
//					}
//				}
//				TableField column = field.getAnnotation(TableField.class);
//				if (column != null) {
//					// 不生成当前表字段跳过
//					if(!column.exist()) {
//						continue;
//					}
//					tableFieldName = column.value();
//				}else {
//					tableFieldName = fieldName;
//				}
////				String mTableFieldName="".equals(tableFieldName.trim()) ? table.fieldPrefix()+fieldName : table.fieldPrefix()+tableFieldName;
//				String mTableFieldName=tableFieldName;
//				fieldDef.put(mTableFieldName,field);
//				TableId idd=field.getAnnotation(TableId.class);
//				if(idd!=null) {
//					AnnotationDefInfo def=new AnnotationDefInfo();
//					def.setColumnName(Arrays.asList(mTableFieldName));
//					def.setAnnotation(idd);
//					fieldIndexesDef.put("PRIMARY", def);
//				}
//				//获取列定义
//				ColumnDef columnDef=field.getAnnotation(ColumnDef.class);
//				if(columnDef!=null) {
//					for(Normal param:columnDef.indexes().normal()) {
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="normal_"+mTableFieldName;
//						}
//						AnnotationDefInfo def=new AnnotationDefInfo();
//						def.setColumnName(Arrays.asList(mTableFieldName));
//						def.setAnnotation(param);
//						fieldIndexesDef.put(indexesAlias, def);
//					}
//					for(Unique param:columnDef.indexes().unique()) {
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="unique_"+mTableFieldName;
//						}
//						AnnotationDefInfo def=new AnnotationDefInfo();
//						def.setColumnName(Arrays.asList(mTableFieldName));
//						def.setAnnotation(param);
//						fieldIndexesDef.put(indexesAlias, def);
//					}
//					for(FullText param:columnDef.indexes().fullText()) {
//						String indexesAlias=param.value();
//						if("".equals(param.value())) {
//							indexesAlias="fulltext_"+mTableFieldName;
//						}
//						AnnotationDefInfo def=new AnnotationDefInfo();
//						def.setColumnName(Arrays.asList(mTableFieldName));
//						def.setAnnotation(param);
//						fieldIndexesDef.put(indexesAlias, def);
//					}
//				}else {
//					if(LOGGER.isWarnEnabled()) {
//						LOGGER.warn("{} 字段 {} 未定义@ColumnDef",table.value(),mTableFieldName);
//					}
//				}
//			}
//			classes = classes.getSuperclass();
//		}
//		DatabaseMetaData dbmd = conn.getMetaData();
//		ResultSet columnRs = dbmd.getColumns(conn.getCatalog(), "%", tableName, "%");
//		if(columnRs.next()) {
//			//列
//			Map<String,FieldDefInfo> tableColumnDef=new HashMap<>(100);
//			do {
////				ResultSetMetaData rsmd = columnRs.getMetaData();
////				for(int i=0;i<rsmd.getColumnCount();i++) {
////					String name=rsmd.getColumnName(i+1);
////					System.out.println(name+"        "+columnRs.getString(name));
////				}
//				FieldDefInfo def=new FieldDefInfo();
//				def.setColumnName(columnRs.getString("COLUMN_NAME"));
//				def.setTypeName(columnRs.getString("TYPE_NAME"));
//				def.setLength(columnRs.getInt("COLUMN_SIZE"));
//				def.setDecimal(columnRs.getInt("DECIMAL_DIGITS"));
//				def.setNullable("1".equals(columnRs.getString("NULLABLE")));
//				def.setDefaultValue(columnRs.getString("COLUMN_DEF"));
//				def.setRemark(columnRs.getString("REMARKS"));
//				tableColumnDef.put(def.getColumnName(),def);
//			}while(columnRs.next());
//			for(String mTableFieldName:fieldDef.keySet()) {
//				if(tableColumnDef.containsKey(mTableFieldName)) {
//					Field field=fieldDef.get(mTableFieldName);
//						FieldDefInfo def=tableColumnDef.get(mTableFieldName);
//	//					System.out.println("字段名："+def.getColumnName());
//	//					System.out.println("类型名："+def.getTypeName());
//	//					System.out.println("列大小："+def.getLength());
//	//					System.out.println("列小数："+def.getDecimal());
//	//					System.out.println("是否为空："+def.isNullable());
//	//					System.out.println("默认值："+def.getDefaultValue());
//	//					System.out.println("备注："+def.getRemark());
//					String scripts=tableAlterChangeScript(tableName,mTableFieldName,field,def);
//					if(!StringUtils.isEmpty(scripts)) {
//						//为空则表示字段无更改
//						System.out.println(scripts);
//					}
//				}else {
//					System.out.println(tableAlterAddScript(tableName,mTableFieldName,fieldDef.get(mTableFieldName)));
//				}
//			}
//			for(String mTableFieldName:tableColumnDef.keySet()) {
//				if(!fieldDef.containsKey(mTableFieldName)) {
//					//列字段不存在实体类中则删除
//					System.out.println("ALTER TABLE "+String.format("`%s`", tableName)+" DROP "+String.format("`%s`", mTableFieldName)+";");
//				}
//			}
//		}else {
//			//没该表直接打印表语句
//			System.out.println(buildTableScript(prototype));
//			return;
//		}
//		columnRs.close();
//		//索引
//		ResultSet indexRs = dbmd.getIndexInfo(conn.getCatalog(), null, tableName, false, false);
//		if(indexRs.next()) {
//			Map<String,List<FieldIndexesDefInfo>> tableColumnIndexesDef=new HashMap<>(100);
//			do {
//				FieldIndexesDefInfo indexesDef=new FieldIndexesDefInfo();
//				indexesDef.setColumnName(indexRs.getString("COLUMN_NAME"));
//				indexesDef.setUnique( indexRs.getString("NON_UNIQUE"));
//				indexesDef.setName(indexRs.getString("INDEX_NAME"));
//				indexesDef.setType(indexRs.getString("TYPE"));
//				List<FieldIndexesDefInfo> indexesDefs=tableColumnIndexesDef.get(indexesDef.getName());
//				if(indexesDefs==null) {
//					indexesDefs=new ArrayList<>();
//				}
//				indexesDefs.add(indexesDef);
//				tableColumnIndexesDef.put(indexesDef.getName(), indexesDefs);
//			}while(indexRs.next());
//			for(String mFieldIndexesName:fieldIndexesDef.keySet()) {
//				AnnotationDefInfo annDefInfo=fieldIndexesDef.get(mFieldIndexesName);
//				if(tableColumnIndexesDef.containsKey(mFieldIndexesName)) {
//					List<FieldIndexesDefInfo> indexesDefs=tableColumnIndexesDef.get(mFieldIndexesName);
//					List<String> fieldIndexesDefNames=new ArrayList<>();
//					for(FieldIndexesDefInfo def:indexesDefs) {
//						fieldIndexesDefNames.add(def.getColumnName());
//					}
//					if(fieldIndexesDefNames.containsAll(annDefInfo.getColumnName())&&
//							annDefInfo.getColumnName().containsAll(fieldIndexesDefNames)) {
//						//表索引未调整
//					}else {
//						System.out.println(tableAlterAddIndexesScript(tableName,mFieldIndexesName,fieldIndexesDef.get(mFieldIndexesName),true));
//					}
//				}else {
//					System.out.println(tableAlterAddIndexesScript(tableName,mFieldIndexesName,fieldIndexesDef.get(mFieldIndexesName),false));
//				}
//			}
//			for(String mFieldIndexesName:tableColumnIndexesDef.keySet()) {
//				if(!fieldIndexesDef.containsKey(mFieldIndexesName)) {
//					//列字段不存在实体类中则删除
//					System.out.println("ALTER TABLE "+String.format("`%s`", tableName)+" DROP INDEX "+String.format("`%s`", mFieldIndexesName)+";");
//				}
//			}
//		}
//		indexRs.close();
//	}
//	
//}
