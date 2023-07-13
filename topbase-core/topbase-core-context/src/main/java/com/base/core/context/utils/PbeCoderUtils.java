package com.base.core.context.utils;

import java.util.Scanner;

import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.PbeCoder;

/**
 * @description: 解密工具类
 * @author: start
 * @create: 2019-09-11 10:22
 */
public class PbeCoderUtils {
	
	public static String password="123456";
	
	static {
		if(StringUtils.isEmpty(password)) {
			//密码取自控制台 运行命令：./catalina.sh run &
			try(Scanner s = new Scanner(System.in)){
	        	System.out.println("Input PBE password:");
	        	password = s.nextLine();
	        }
			//密码取自环境变量
//			password=System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
		}
	}
	
	/**
	 * 根据环境变量密码加密
	 * @param content
	 * @return
	 */
    public static String ecryptEnv(String content){
    	return PbeCoder.encrypt(content, password);
    }
    
    /**
	 * 根据环境变量密码解密
	 * @param content
	 * @return
	 */
    public static String decryptEnv(String content){
    	return PbeCoder.decrypt(content, password);
    }

    public static void main(String[] args) {
    	String pwd=StringUtils.numberRandom(4);
    	System.out.println("密码："+pwd);
    	String content="top123@com";
    	String encrypt=PbeCoder.encrypt(content,pwd);
    	System.out.println("密文："+encrypt);
    	content=PbeCoder.decrypt(encrypt,pwd);
        System.out.println("原文："+content);
    }
}
