package com.base.core.context.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author start
 *
 */
public class HexUtils {
	
	/**
	 * 00 12 22 2C 转为 byte[]
	 * @param inHex
	 * @return
	 */
	public static byte[] hexToBytes(String inHex) {
		int length = inHex.length() / 2;
		List<String> hexs = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			hexs.add(inHex.substring(i * 2, i * 2 + 2));
		}
		//断路器+1 
//		byte[] byteArray = new byte[hexs.size() + 1];
		//温湿度
		byte[] byteArray = new byte[hexs.size()];
		for (int i = 0; i < hexs.size(); i++) {
			byteArray[i] = (byte) Integer.parseInt(hexs.get(i), 16);
		}
		return byteArray;
	}

	/**
	 * byte[] 转 008822332C
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		if (bytes != null && bytes.length > 0) {
			for (int i = 0; i < bytes.length; i++) {
				String hex = byteToHex(bytes[i]);
				sb.append(hex);
			}
		}
		return sb.toString();
	}

	/**
	 * Byte字节转Hex
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b) {
		String hexString = Integer.toHexString(b & 0xFF);
		// 由于十六进制是由0~9、A~F来表示1~16，所以如果Byte转换成Hex后如果是<16,就会是一个字符（比如A=10），通常是使用两个字符来表示16进制位的,
		// 假如一个字符的话，遇到字符串11，这到底是1个字节，还是1和1两个字节，容易混淆，如果是补0，那么1和1补充后就是0101，11就表示纯粹的11
		if (hexString.length() < 2) {
			hexString = new StringBuilder(String.valueOf(0)).append(hexString).toString();
		}
		return hexString.toUpperCase();
	}
	
	public static String toHex(int n) {
		String h=Integer.toHexString(n);
		if(h.length()==1) {
			return "0"+h;
		}else {
			return h;
		}
	}
	
}
