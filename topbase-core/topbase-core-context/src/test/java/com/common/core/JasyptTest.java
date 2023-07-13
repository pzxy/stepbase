package com.common.core;

import org.junit.Test;

import com.base.core.context.utils.Jasypt;
import com.gitee.magic.core.utils.StringUtils;

public class JasyptTest {
	
	@Test
	public void test_input_pwd() {
    	String content="aa";
    	
		String inputPwd=StringUtils.numberRandom(4);
    	System.out.println("输入密码:"+inputPwd);
    	String pwd2=StringUtils.numberRandom(3);
    	Jasypt jasy=new Jasypt(inputPwd);
    	System.out.println("文件密文:"+jasy.ecrypt(pwd2));
    	String pwd1="&*9L3fkdEr*";
    	System.out.println("配置密码:"+pwd1);
    	String pwd=pwd1+pwd2;
    	Jasypt jasy1=new Jasypt(pwd);
    	System.out.println("配置密文:"+jasy1.ecrypt(content));
	}
	
}
