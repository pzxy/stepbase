package com.base.core.devtools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

import com.gitee.magic.core.utils.ChangeCharUtils;

/**
 * 模板生成类
 * @author Start
 */
public class BuildMongoCodeTemplates{
	
	private String path="src/main/java/";
	
	public void setPath(String path) {
		this.path = path;
	}

	private static String[] FILECODES={"Service","ServiceImpl","Dao","Entity"};
	
	private static String[] FILETYPES={"service","service/impl","dao","entity"};
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

	public BuildMongoCodeTemplates(String packagepath,String packageName,String entityName){
		this(packagepath, packageName, entityName, DaoTypeEnum.Mongo);
	}
	
	public BuildMongoCodeTemplates(String packagepath,String packageName,String entityName,DaoTypeEnum type){
		this.packagepath=packagepath;
		/**
		 * 默认包名为小写顾全转为小写
		 */
		this.packageName=packageName.toLowerCase();
		/**
		 * 把实体名首字母转为大写
		 */
		this.fullEntityName=entityName.substring(0,1).toUpperCase()+entityName.substring(1);
		/**
		 * 把实体名首字母转为小写
		 */
		this.simpleEntityName=entityName.substring(0,1).toLowerCase()+entityName.substring(1);
		
		this.type=type;
	}
	
	public void buildCodeFile() throws IOException{
		for(int i=0;i<FILETYPES.length;i++){
			/**
			 * 文本的内容
			 */
			StringBuilder txtContentBuilder=new StringBuilder();
			ClassPathResource classPath=new ClassPathResource("templates/mongo/"+FILECODES[i]+".txt");
			
			BufferedReader reader =new BufferedReader(new InputStreamReader(classPath.getInputStream()));
	        try {
	        	/**
	        	 * 每一行文本的内容
	        	 */
	        	String lineContent;
	            while((lineContent=reader.readLine())!=null){
	            	txtContentBuilder.append(lineContent+"\n");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	reader.close();
	        }
	        String types=FILETYPES[i];
	        writerFile(txtContentBuilder.toString(),types,FILECODES[i]);
		}
	}

	/**
	 * 生成文件
	 * @param content
	 * @param builderType
	 * @param fileCode
	 * @throws IOException
	 */
	void writerFile(String content,String builderType,String fileCode) throws IOException{
		String directory=path+packagepath+"/"+packageName+"/"+builderType.toLowerCase()+"/";
		//目录不存在则创建
		File codeDirectory=new File(directory);
		if(!codeDirectory.exists()){
			codeDirectory.mkdirs();
		}
		String e="Entity";
		if(e.equals(fileCode)){
			fileCode="";
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
				replaceAll("@\\{PACKAGENAME\\}",packageName).
				replaceAll("@\\{ENTITYNAME\\}", fullEntityName).
				replaceAll("@\\{SIMPLEENTITYNAME\\}", simpleEntityName).
				replaceAll("@\\{UENTITYNAME\\}", ChangeCharUtils.camelToUnderline(simpleEntityName, 1)).
				replaceAll("@\\{TABEXTESION\\}",tabHeader).
				replaceAll("@\\{DAOTYPE\\}",this.type.name()).
				replaceAll("@\\{COMPANYNAME\\}",packagepath.replace("/", "."));
			writerFile.createNewFile();
			BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
			writer.write(content);
			writer.flush();
			writer.close();
			System.out.println(fullEntityName+fileCode+".java 源文件创建成功！");
		}else{
			System.out.println(fullEntityName+fileCode+".java  源文件已经存在创建失败！");
		}
	}
	
}