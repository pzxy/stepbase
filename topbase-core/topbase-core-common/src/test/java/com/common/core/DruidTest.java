package com.common.core;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.druid.filter.config.ConfigTools;
import com.gitee.magic.framework.head.utils.IoUtils;

public class DruidTest {
	
	@Test
	public void build() throws Exception {
		String[] arr = ConfigTools.genKeyPair(512);
		System.out.println("privateKey:" + arr[0]);
		System.out.println("publicKey:" + arr[1]);
	}
	
	@Test
	public void encrypt() throws Exception {
		ClassPathResource cpr=new ClassPathResource("privatekey.txt");
		String privateKey=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(), -1));
		String password = "LPMVii4ZgWrFMjkDDTLWvg8t2RiWpNce3GEZI9N9M6D3kj1x0a3POG4l3GJraMw5PPp42rRN073BV0SbA2R0Og";
		String txt=ConfigTools.encrypt(privateKey, password);
		System.out.println(txt);
	}
	
	@Test
	public void decrypt() throws Exception {
		ClassPathResource cpr=new ClassPathResource("publickey.txt");
		String publicKey=new String(IoUtils.inputStreamConvertBytes(cpr.getInputStream(), -1));
		String txt = "UxGPojI0ByerBQ3b2EZh8B82zWR/5gkXoGdzrXnSvp1c2kIEKLekJ4i7CVGcU+/V8R1nVPN5ewG0XbgrXpJD0YW2W/JONczDTzPb30Zf6tq1KSc8jfZMiLecMNIqcJi4eDSr7HAdH7tVSKG5HIaYDvk2TVH7pvHZatI2eKeG8/QBl4wuQq6Tb2MUIw1zofWduQ0q76LnKCbqOlVZZr9CoUyfoHMJdIow96mWouKYLBa43V9hKgUbfOINW5rmUw+j/6JvgcNevvgJ3BL/blX3doXfcgtXpHlXs4Lt3dJJZMVU6K7LgJxjukGiA2y8qeI86fFIzJ5vLsBXccldWJKSpq15COAvMx2MXsZrX3MBnbBBGC4r+FzgOwvT4vaW3grNWEbO7YFVt1wpy23ahFK8ITX2G9ux0PFa+nnvI2sskLYmQl+1HknzaF5d7FQgsTDfb06WX4ap1rBaWnc6KP2zCqqAbjNfJVXSJ/fkBAB1riy+SYsaWn/2+UMEaZ1Sb/Nr8n4zxPB1Rl2HsINE8k8z3m6qdyS2MYblq+QKgYv9E1u3Qt4QFx76MPta0MizME85neel8uFKG1okEapY3mQ7AfBqdxuLb8yT3Mn6IKE1Flq1t1J6vx/BA7Ckv7PC+zGXNkDZl84V10+bGfhobFZjnzvAPD8u+fk87kbKn3AXAt99hpzqJp5RsJyRc242Z+XR0G5xFsCV1SKosVX+y2+njlzCVjbfrAOhTnAPDBVquiZ0Nz0S4HDX0m1FyH5LQf2yW1PCXHamJow7gXRUcnXsU0ZvYkO0Rbnh/zJvus005pvfUWLf4UKctM5jie9n/Qkm6sHk3Eqza9K3InDTlp5WwQ==";
		System.out.println(ConfigTools.decrypt(publicKey,txt));
	}

}
