package com.sat.mobilesafe.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by knight on 17-1-27.
 */

public class Md5 {
    public static String encoder(String pwd){
        StringBuffer stringBuffer =  new StringBuffer();
        try {
            //1.指定加密算法
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2.将需要加密的字符串转换成byte类型的数组，然后进行随机哈希过程
            byte[] bs = digest.digest(pwd.getBytes());
            //3,循环遍历bs,然后让其生成32位字符串,固定写法
            //4.拼接字符串

            for (byte b:bs){
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length()<2){
                    hexString += "0"+hexString;
                }
                stringBuffer.append(hexString);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
