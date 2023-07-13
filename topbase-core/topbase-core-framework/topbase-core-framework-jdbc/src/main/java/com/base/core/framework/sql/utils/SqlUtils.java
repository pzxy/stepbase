package com.base.core.framework.sql.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author start
 */
public class SqlUtils {

	/**
	 * 把原SQL语句转化为count(1)统计语句
	 * @param sqlStr
	 * @return
	 */
	public static String convertCountSql(String sqlStr) {
//		String regex="Select ([\\w\\W]*) From ";
		String regex="Select (.*?) From ";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(sqlStr);
		if(m.find()){
			sqlStr="Select count(1) From "+sqlStr.substring(m.group(0).length(), sqlStr.length());
		}
		String orderbyRegex=" Order by ([\\w\\W]*)";
		Pattern oypattern = Pattern.compile(orderbyRegex,Pattern.CASE_INSENSITIVE);
		Matcher oyma = oypattern.matcher(sqlStr);
		if(oyma.find()){
			sqlStr=sqlStr.substring(0, sqlStr.length()-oyma.group(0).length());
		}
		String limitRegex=" Limit ([\\w\\W]*)";
		Pattern patternl = Pattern.compile(limitRegex,Pattern.CASE_INSENSITIVE);
		Matcher ma = patternl.matcher(sqlStr);
		if(ma.find()){
			sqlStr=sqlStr.substring(0, sqlStr.length()-ma.group(0).length());
		}
		return sqlStr;
	}
	
	public static void main(String[] args) {
		String sql="SELECT\n" + 
				"			be.address as address,\n" + 
				"			be.amount as amount,\n" + 
				"			be.fromHash as fromHash,\n" + 
				"			be.fromChain as fromChain,\n" + 
				"			be.toChain as toChain,\n" + 
				"			be.transferStatus as transferStatus,\n" + 
				"			be.created_date as createdDate,\n" + 
				"			bt.targetHash as targetHash,\n" + 
				"			bt.gasFee as gasFee\n" + 
				"		FROM\n" + 
				"			bri_exchange be\n" + 
				"			LEFT JOIN bri_tranfer bt ON bt.exchangeId = be.id\n" + 
				"		WHERE 1=1";
		sql=sql.replaceAll("\n", "")
		.replaceAll("\t", " ")
		.replaceAll("  ", " ")
		.replaceAll("   ", " ");
		System.out.println(convertCountSql(sql));
	}
	
}
