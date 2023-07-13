package com.base.blockchain.wallet.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author  start
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	public static String listToString(List<?> lists) {
		return listToString(lists, " ");
	}

	public static String listToString(List<?> lists, String comma) {
		StringBuilder strBuilder = new StringBuilder();
		for (Object str : lists) {
			strBuilder.append(str);
			strBuilder.append(comma);
		}
		if (strBuilder.length() > 0) {
			return strBuilder.substring(0, strBuilder.length() - comma.length());
		}
		return strBuilder.toString();
	}
	
	public static List<String> stringToList(String str) {
		return stringToList(str," ");
	}

	public static List<String> stringToList(String str, String comma) {
		return Arrays.asList(str.split(comma));
	}

}
