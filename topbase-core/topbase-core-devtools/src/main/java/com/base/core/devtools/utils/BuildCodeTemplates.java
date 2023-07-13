package com.base.core.devtools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.ChangeCharUtils;
import com.gitee.magic.framework.head.utils.TimeUtils;

/**
 * 模板生成类
 * @author Start
 */
public class BuildCodeTemplates{
	
	private String path="src/main/java/";
	
	public void setPath(String path) {
		this.path = path;
	}

	
	/**
	 * 包路径
	 */
	private String packagepath;
	/**
	 * 包名全小写
	 */
	private String packageName;
	/**
	 * 完整的实体名,首字母大写
	 */
	private String fullEntityName;
	/**
	 * 实体名简写,首字母小写
	 */
	private String simpleEntityName;
	
	private DaoTypeEnum type;
	
	private String author;

	public BuildCodeTemplates(String packagepath,String packageName,String entityName){
		String logName=System.getenv("LOGNAME");
		if(logName==null) {
			logName= System.getProperty("user.name");
		}
		build(packagepath, packageName, entityName, DaoTypeEnum.Sql,logName);
	}
	
	public BuildCodeTemplates(String packagepath,String packageName,String entityName,String author){
		build(packagepath, packageName, entityName, DaoTypeEnum.Sql,author);
	}
	
	public void build(String packagepath,String packageName,String entityName,DaoTypeEnum type,String author){
		this.packagepath=packagepath;
		//默认包名为小写顾全转为小写
		this.packageName=packageName.toLowerCase();
		//把实体名首字母转为大写
		this.fullEntityName=entityName.substring(0,1).toUpperCase()+entityName.substring(1);
		//把实体名首字母转为小写
		this.simpleEntityName=entityName.substring(0,1).toLowerCase()+entityName.substring(1);
		
		this.type=type;
		
		this.author=author;
	}

	public void buildCodeFile(){
		buildCodeFile(1);
	}
	
	public void buildCodeFile(int type){
		if(type==1) {
			String[] fileCodes={"AO","UpdateAO","PageAO","VO","Controller","Mapping","Service","ServiceImpl","Dao","DaoImpl","DO"};
			String[] fileNames={"AO","UpdateAO","PageAO","VO","Controller","Mapping","Service","ServiceImpl","Dao","DaoImpl","DO"};
			String[] fileTypes={"controller","controller","controller","controller","controller","controller","service","service/impl","dao","dao/impl","entity"};
			buildCodeFile(fileCodes, fileNames, fileTypes);
		}else {
			String[] fileCodes={"AO","UpdateAO","PageAO","VO","Controller1","Service","ServiceImpl","Dao","DaoImpl","DO"};
			String[] fileNames={"AO","UpdateAO","PageAO","VO","Controller","Service","ServiceImpl","Dao","DaoImpl","DO"};
			String[] fileTypes={"controller","controller","controller","controller","controller","service","service/impl","dao","dao/impl","entity"};
			buildCodeFile(fileCodes, fileNames, fileTypes);
		}
	}
	
	public void buildCodeFile(String[] fileCodes,String[] fileNames,String[] fileTypes){
		for(int i=0;i<fileTypes.length;i++){
			//文本的内容
			StringBuilder txtContentBuilder=new StringBuilder();
			ClassPathResource classPath=new ClassPathResource("templates/mvc/"+fileCodes[i]+".txt");
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(classPath.getInputStream()));
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
	        try {
	        	//每一行文本的内容
	        	String lineContent;
	            while((lineContent=reader.readLine())!=null){
	            	txtContentBuilder.append(lineContent+"\n");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	try {
					reader.close();
				} catch (IOException e) {
					throw new ApplicationException(e);
				}
	        }
	        String types=fileTypes[i];
	        writerFile(txtContentBuilder.toString(),types,fileNames[i]);
		}
	}

	/**
	 *
	 * @param content
	 * @param builderType
	 * @param fileCode
	 * @throws IOException
	 */
	void writerFile(String content,String builderType,String fileCode){
		String directory=path+packagepath+"/"+packageName+"/"+builderType.toLowerCase()+"/";
		String c="controller";
		if(c.equals(builderType)) {
			directory=directory+simpleEntityName+"/";
		}
		//目录不存在则创建
		File codeDirectory=new File(directory);
		if(!codeDirectory.exists()){
			codeDirectory.mkdirs();
		}
		File writerFile=new File(directory+fullEntityName+fileCode+".java");
		if(!writerFile.exists()){
			//表的前缀默认取包名的前三位
			String tabHeader;
			int l=3;
			if(packageName.length()>l){
				tabHeader=packageName.substring(0,l);
			}else{
				tabHeader=packageName;
			}
			content=content.
				replaceAll("@\\{AUTHOR\\}",author).
				replaceAll("@\\{DATE\\}",TimeUtils.getSysTime()).
				replaceAll("@\\{PACKAGENAME\\}",packageName).
				replaceAll("@\\{ENTITYNAME\\}", fullEntityName).
				replaceAll("@\\{SIMPLEENTITYNAME\\}", simpleEntityName).
				replaceAll("@\\{UENTITYNAME\\}", ChangeCharUtils.camelToUnderline(simpleEntityName, 1)).
				replaceAll("@\\{TABEXTESION\\}",tabHeader).
				replaceAll("@\\{DAOTYPE\\}",this.type.name()).
				replaceAll("@\\{COMPANYNAME\\}",packagepath.replace("/", "."));
			try {
				writerFile.createNewFile();
				BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
				writer.write(content);
				writer.flush();
				writer.close();
				System.out.println(fullEntityName+fileCode+".java 源文件创建成功！");
			} catch (IOException e) {
				throw new ApplicationException(e);
			}
		}else{
			System.out.println(fullEntityName+fileCode+".java  源文件已经存在创建失败！");
		}
	}
	
}