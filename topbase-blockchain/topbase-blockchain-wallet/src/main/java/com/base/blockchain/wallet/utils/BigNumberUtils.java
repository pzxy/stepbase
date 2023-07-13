package com.base.blockchain.wallet.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 
 * @author start
 *
 */
public class BigNumberUtils {
	
	public static BigInteger pow6(BigDecimal value) {
		return pow(value,6);
	}
	
	public static BigDecimal pow6(BigInteger value) {
		return pow(value,6);
	}

	public static BigInteger pow18(BigDecimal value) {
		return pow(value,18);
	}
	
	public static BigDecimal pow18(BigInteger value) {
		return pow(value,18);
	}
	
	public static BigInteger pow(BigDecimal value,int pow) {
		return value.multiply(BigDecimal.TEN.pow(pow)).toBigInteger();
	}
	
	public static BigDecimal pow(BigInteger value,int pow) {
		return new BigDecimal(value).divide(BigDecimal.TEN.pow(pow));
	}
	
	public static BigDecimal div(BigDecimal value,int pow) {
		return value.divide(BigDecimal.TEN.pow(pow));
	}
	
	public static void main(String[] args) {
		System.out.println(pow18(new BigDecimal("2.53")));
		System.out.println(pow18(new BigInteger("254324000000000000")));
	}
	
}
