package com.common.core;

import java.security.KeyPair;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.base.core.context.utils.Jasypt;
import com.gitee.magic.core.utils.GoogleAuthenticator;
import com.gitee.magic.core.utils.codec.Rsa;
import com.gitee.magic.framework.head.utils.IoUtils;
import com.gitee.magic.framework.head.utils.TimeUtils;

/**
 * 争对TOP的RSA公私钥加解密处理
 * @author start
 *
 */
public class TopRsaTest {

	@Test
	public void encrypt() throws Exception {
		ClassPathResource cpr=new ClassPathResource("publickey.txt");
		String publicKey=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(), -1));
		String password = "123456";
		String txt=Rsa.encrypt(password,publicKey);
		System.out.print(txt);
	}
	
	@Test
	public void decrypt() throws Exception {
		ClassPathResource cpr=new ClassPathResource("privatekey.txt");
		String privateKey=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(), -1));
		Jasypt jasypt=new Jasypt("fyo7whbj3cz4o5fh");
		String priKey=jasypt.decrypt(privateKey);
		String txt = "LGmzGZ+H7wIJ8IeSN8Wz98jJAwzXip7ObEZchijJXZ4F80ArinNIs+IKS/jXzOsDAu6S0SIQxIHleJtX3t4tyg==";
		System.out.print(Rsa.decrypt(txt,priKey));
	}
	
	@Test
	public void verify() throws Exception {
		ClassPathResource cpr=new ClassPathResource("privatekey.txt");
		String privateKey=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(), -1));
		//请设置真实MFA密钥
		Jasypt jasypt=new Jasypt("");
		String priKey=jasypt.decrypt(privateKey);

		String data = "123456";
        String sign=Rsa.sign(data, priKey, Rsa.WITHSHA1);
        System.out.println("签名:"+sign);

		ClassPathResource cpr1=new ClassPathResource("publickey.txt");
		String publicKey=new String(IoUtils.inputStreamConvertBytes(cpr1.getInputStream(), -1));
        Boolean f=Rsa.verify(data, sign, publicKey, Rsa.WITHSHA1);
        System.out.println("验证:"+f);
	}
	
	@Test
	public void rest() throws Exception {
		System.out.println("请在安全环境下操作，注意信息安全!!!!");
		String code=GoogleAuthenticator.generateSecretKey();
		String barcode=GoogleAuthenticator.getQrBarcode("配置中心_"+TimeUtils.getSysdate(),code);
		System.out.println("访问https://cli.im/生成二维码:"+barcode);
		KeyPair keyPair=Rsa.genKeyPair(512); 
        String privateKey=Rsa.getPrivateKey(keyPair);
        String publickey=Rsa.getPublicKey(keyPair);
		//密码为Google MFA KEY
		Jasypt jasypt=new Jasypt(code);
		System.out.println("私钥通过MFA加密请配置到application-pro中jasypt.privateKey -> \n" + jasypt.ecrypt(privateKey));
		System.out.println("公钥加密数据使用 -> \n" + publickey);
	}
	
}
