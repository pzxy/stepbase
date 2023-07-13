package com.base.core.devtools.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.core.io.ClassPathResource;

import com.gitee.magic.core.annotations.Column;
import com.gitee.magic.core.annotations.Transient;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.ContextSupportType;
import com.gitee.magic.framework.head.utils.IoUtils;
import com.gitee.magic.jdbc.persistence.annotation.Entity;
import com.gitee.magic.jdbc.persistence.annotation.MappedSuperclass;
import com.gitee.magic.jdbc.persistence.annotation.Table;

/**
 * 模板生成类
 * @author Start
 */
public class BuildModelExampleTemplates{
	
	private String path="src/main/java/";
	
	public BuildModelExampleTemplates(Class<?> prototype) throws IOException {
		if(prototype.isAnnotationPresent(Entity.class)) {
			StringBuilder context=new StringBuilder();
			// 获取表名
			Table table = prototype.getAnnotation(Table.class);
			if (table == null) {
				System.out.println("Class<"+prototype+">,未定义@Table注解，找不到映射的表名");
				return;
			}
			Class<?> classes = prototype;
			while (classes.isAnnotationPresent(Entity.class)||
					classes.isAnnotationPresent(MappedSuperclass.class)) {
				Field[] fields=classes.getDeclaredFields();
				for(Field field:fields) {
					if(Modifier.isFinal(field.getModifiers())||Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					String fieldName=field.getName();
					// 不生成当前表字段跳过
					Transient trans = field.getAnnotation(Transient.class);
					if (trans != null) {
						continue;
					}
					String tableFieldName = "";
					Column column = field.getAnnotation(Column.class);
					if (column != null) {
						tableFieldName = column.value();
					}
					tableFieldName="".equals(tableFieldName.trim()) ? table.fieldPrefix()+fieldName : table.fieldPrefix()+tableFieldName;
					String templateName="ModelExampleStringField";
					String dateType=field.getType().getSimpleName();
					if(ContextSupportType.isSupportTypeCheckByNumber(field.getType())) {
						if(ContextSupportType.isSupportTypeCheckByBigInteger(field.getType())||
								ContextSupportType.isSupportTypeCheckByBigDecimal(field.getType())) {
							dateType="String";
						}
						templateName="ModelExampleNumberField";
					} else if(ContextSupportType.isSupportTypeCheckByDate(field.getType())||
							field.getType().equals(java.sql.Date.class)||
							field.getType().equals(java.sql.Timestamp.class)){
						templateName="ModelExampleNumberField";
						dateType=field.getType().getName();
					} else {
						dateType="String";
					}
					String content=getFileStringContentClassPath("templates/example/"+templateName+".txt");
					String mFieldName=fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
					content=content
							.replaceAll("@\\{FIELDNAME\\}", fieldName)
							.replaceAll("@\\{TABLEFIELDNAME\\}", tableFieldName)
							.replaceAll("@\\{DATETYPE\\}", dateType)
							.replaceAll("@\\{MFIELDNAME\\}", mFieldName);
					context.append(content+"\n");
				}
				classes = classes.getSuperclass();
			}
			String directory=path+prototype.getPackage().getName().replace(".", "/");
			File dir=new File(directory);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File writerFile=new File(directory+"/"+prototype.getSimpleName()+"Example.java");
			if(writerFile.exists()){
				writerFile.delete();
			}
			String content=getFileStringContentClassPath("templates/example/ModelExample.txt");
			content=content
					.replaceAll("@\\{PACKAGENAME\\}", prototype.getPackage().getName())
					.replaceAll("@\\{ENTITYNAME\\}", prototype.getSimpleName())
					.replaceAll("@\\{CONTEXT\\}", context.toString());
			writerFile.createNewFile();
			BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
			writer.write(content);
			writer.flush();
			writer.close();
			System.out.println(writerFile.getPath()+" 模版文件生成成功！");
		}else {
			System.out.println(prototype+"不是表实体类");
		}
	}
	
	public static String getFileStringContentClassPath(String clsPath) {
		ClassPathResource resource=new ClassPathResource(clsPath);
		try {
			return new String(IoUtils.inputStreamConvertBytes(resource.getInputStream(), resource.getInputStream().available()));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
}