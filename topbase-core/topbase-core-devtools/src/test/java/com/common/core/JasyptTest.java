package com.common.core;

import org.junit.Test;

import com.base.core.context.utils.Jasypt;
import com.gitee.magic.core.utils.StringUtils;

public class JasyptTest {
	
	/**
	 * 手输入密码生成方法
	 * 1、写入/data/tmp/epd.json密文内容
	 * 2、启动程序 
	 * 		nohup java -jar xxx.jar << EOF &
	 * 			手输密码
	 * 		EOF 
	 * 3、查看启动日志
	 * 		tail -200f /data/app_data/logs/xxx.log
	 * 4、删除密文删除
	 * 		启动后记得删除密文文件:rm -rf /data/tmp/epd.json文件
	 */
	@Test
	public void test_input_pwd() {
		//加密内容
    	String content="aa";
    	
		String inputPwd=StringUtils.numberRandom(4);
    	System.out.println("输入密码:"+inputPwd);
    	String pwd2=StringUtils.numberRandom(3);
    	Jasypt jasy=new Jasypt(inputPwd);
    	//启动程序前写入到服务端文件:/data/tmp/epd.json
    	System.out.println("文件密文:"+jasy.ecrypt(pwd2));
    	//密码给到开发人员配置到程序中或硬编码至程序中
    	String pwd1=StringUtils.random();
    	System.out.println("配置密码:"+pwd1);
    	String pwd=pwd1+pwd2;
    	Jasypt jasy1=new Jasypt(pwd);
    	//密文给到开发人员配置到程序中
    	String jasyContent=jasy1.ecrypt(content);
    	System.out.println("配置密文:"+jasyContent);
	}

}
