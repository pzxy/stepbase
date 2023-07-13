package com.base.core.context.mvc;

import java.util.ArrayList;
import java.util.List;

import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.Md5;
import com.gitee.magic.framework.base.constant.Config;

/**
 * 常量
 *
 * @author Start
 */
public class Constants {

	public static final String PRE0=":";
	public static final String PRE1="%s";
	public static final String PRE2="_";
	
	/**
	 * Redis前缀
	 */
	public final static String REDISPREFIX=Md5.md5(Config.getSystemName()+PRE2+Config.getActive())+PRE0;

	public static String getKey(Object...args) {
		List<String> params=new ArrayList<>();
		for(int i=0;i<args.length;i++) {
			params.add(PRE1);
		}
		return String.format(REDISPREFIX+StringUtils.listToString(params,PRE0), args);
	}
	
}
