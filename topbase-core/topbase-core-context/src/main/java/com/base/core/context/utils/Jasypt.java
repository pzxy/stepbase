package com.base.core.context.utils;

import java.io.File;
import java.util.Scanner;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * https://blog.csdn.net/tang_jian_dong/article/details/124586924
 * <pre>
 * jasypt-spring-boot-starter 3.0以上默认使用PBEWITHHMACSHA512ANDAES_256算法无法直接解密该类的加密方式
 * 直接使用该类进行加解密通过--jasypt.encryptor.password处理需要在配置文件中添加以下配置
 * jasypt.encryptor.algorithm=PBEWithMD5AndDES
 * jasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator
 * 或使用3.0以下版本，建议:2.1.1版本
 * </pre>
 * @author start
 */
public class Jasypt {

	private static final Logger LOGGER = LoggerFactory.getLogger(Jasypt.class);

	public String password;

	public Jasypt(String password) {
		this.password = password;
	}

	public String ecrypt(String content) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(password);
		return encryptor.encrypt(content);
	}

	public String decrypt(String content) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(password);
		return encryptor.decrypt(content);
	}

	/**
	 * 解密内容
	 */
	public static String scannerInput(String epdFilePath) {
		File epdFile = new File(epdFilePath);
		if (epdFile.exists()) {
			try (Scanner scan = new Scanner(System.in);) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("请输入密文文件 {} 内容的密码:", epdFilePath);
				}
				String pwd = scan.nextLine();
				String content = new String(IoUtils.getFileBytes(epdFile));
				Jasypt j = new Jasypt(pwd);
				return j.decrypt(content);
			} finally {
				Boolean f = IoUtils.deleteFile(epdFile);
				if(!f) {
	    			throw new ApplicationException("执行 rm -rf "+epdFile+"该文件必须删除请检查权限");
	    		}
			}
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("没有执行 nohup java -jar xx.jar <<　EOF ＆ 命令输入密码启动,需确认敏感数据是否安全");
			}
		}
		return null;
	}
	
	/**
	 * 获取密文中的Key 格式:key@value
	 * @param split @
	 * @param content
	 * @return
	 */
	public static String getEncKey(String split,String content) {
		if(content.indexOf(split)>=0) {
			return content.substring(0,content.indexOf(split));
		}
		return null;
	}
	
	/**
	 * 获取密文中的Value 格式:key@value
	 * @param split @
	 * @param content
	 * @return
	 */
	public static String getEncValue(String split,String content) {
		String key=getEncKey(split,content);
		if(key!=null) {
			return content.substring(key.length()+split.length(),content.length());
		}
		return content;
	}

	public static void main(String[] args) {
		Jasypt jasy = new Jasypt("Afei@2018");
		System.out.println(jasy.ecrypt("7a0f48198ad1cbf5c47c9b665dbbd50a3ee30de65aac026edef77958d07e20a9"));
		System.out.println(jasy.decrypt(
				"zZLsKQ/Ang9zjNhVA9ynmZB5cW2hJE9vby9msRqoxIcyzs0MqfkVhlA6LvXVKGDM6/38LaEOSztFXs582Orsx/nZ5K9SKD5WTKlY6myjQtE="));
	}

}
