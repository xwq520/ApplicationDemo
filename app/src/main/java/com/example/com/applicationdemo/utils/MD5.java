package com.example.com.applicationdemo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 */
public class MD5 {

    /**
     * 获取MD5加密结果
     * @param str 要加密的字符串
     * @return 获取MD5加密结果
     */
    public static String getDigest(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public static void main(String[] args) {
        MD5 md = new MD5();
        String content = "1Hello worid!"; // 403A24DA622ADA6CF45A9DBED84A15D6
        String pStr = md.getDigest(content);
        MD5 md1 = new MD5();
        String pStr1 = md1.getDigest("Hello worid!");
        System.out.println("加密前1：" + content);
        System.out.println("加密后1: " + pStr1);
        System.out.println("加密后 : " + pStr);


    }
}