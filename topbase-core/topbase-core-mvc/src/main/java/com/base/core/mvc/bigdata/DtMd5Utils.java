package com.base.core.mvc.bigdata;



import com.gitee.magic.core.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mac
 */
public class DtMd5Utils {
    public DtMd5Utils() {
    }

    public static final String Md5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        if (StringUtils.isEmpty(s)) {
            return "";
        } else {
            try {
                byte[] btInput = s.getBytes();
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                mdInst.update(btInput);
                byte[] md = mdInst.digest();
                int j = md.length;
                char[] str = new char[j * 2];
                int k = 0;

                for(int i = 0; i < j; ++i) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 15];
                    str[k++] = hexDigits[byte0 & 15];
                }

                return (new String(str)).toLowerCase();
            } catch (Exception var10) {
                var10.printStackTrace();
                return "";
            }
        }
    }

    public static String sha256(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        } else {
            MessageDigest md = null;

            try {
                md = MessageDigest.getInstance("SHA-256");
                md.update(string.getBytes("UTF-8"));
                return byte2Hex(md.digest());
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException var3) {
                return "";
            }
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        for(int i = 0; i < bytes.length; ++i) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }
}