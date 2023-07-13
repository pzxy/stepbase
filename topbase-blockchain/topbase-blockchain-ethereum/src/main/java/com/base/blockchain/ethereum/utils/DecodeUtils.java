package com.base.blockchain.ethereum.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author start 
 */
public class DecodeUtils {

	public static Integer decodeInt(String hexStr) {
		BigInteger bigInteger = decodeBigInt(hexStr);
		return bigInteger.intValue();
	}

	public static Long decodeLong(String hexStr) {
		BigInteger bigInteger = decodeBigInt(hexStr);
		return bigInteger.longValue();
	}

	public static BigInteger decodeBigInt(String hexStr) {
		byte[] dataBytes = hexToByte(hexStr);
		BigInteger data = new BigInteger(dataBytes);
		return data;
	}

	public static String decodeString(String hexStr) {
		byte[] dataBytes = hexToByte(hexStr);
		if (dataBytes == null) {
			return null;
		}
		String str = new String(dataBytes);
		return str;
	}

	public static String decodeAddress(String hexStr) {
		String address = null;
		int len = hexStr.length();
		for (int i = 0; i < len; i++) {
			if (hexStr.charAt(i) != '0') {
				address = hexStr.substring(i, len);
				break;
			}
		}
		return address;
	}

	/**
	 * hex转byte数组
	 */
	public static byte[] hexToByte(String hex) {
		int m = 0, n = 0;
		int byteLen = hex.length() / 2;
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = Byte.valueOf((byte) intVal);
		}
		return ret;
	}
	
	////////////////////////////////
	
	public static List<String> decodeArray(String hexStr){
        if(hexStr.length() <= 2) {
            return null;
        }
        if ("0x".equals(hexStr.substring(0, 2))) {
            hexStr = hexStr.replaceFirst("0x", "");
        }
        // 1.数据区
        String dataHex = DecodeUtils.getDataHex(hexStr);
        
        // 2.数据长度
        int length=DecodeUtils.getDataLength(dataHex);

        // 3.把数据区切分成数组
        List<String> dataHexList = DecodeUtils.getDataBlock(dataHex,length);

        return dataHexList;
    }

	public static String getDataHex(String hexStr) {
		Integer dataIndex = DecodeUtils.decodeInt(hexStr.substring(0, 64));
		dataIndex = dataIndex * 2;
		return hexStr.substring(dataIndex);
	}

	public static int getDataLength(String hexStr) {
		Integer arrayLength = DecodeUtils.decodeInt(hexStr.substring(0, 64));
		String dataStr = hexStr.substring(64);
		return dataStr.length()/arrayLength;
	}

	public static List<String> getDataBlock(String hexStr, Integer dataLength) {
		List<String> list = new ArrayList<>();
		Integer arrayLength = DecodeUtils.decodeInt(hexStr.substring(0, 64));
		String dataStr = hexStr.substring(64);
		for (int i = 0; i < arrayLength; i++) {
			Integer currentIndex = i * dataLength;
			list.add(dataStr.substring(currentIndex, currentIndex + dataLength));
		}
		return list;
	}

}
