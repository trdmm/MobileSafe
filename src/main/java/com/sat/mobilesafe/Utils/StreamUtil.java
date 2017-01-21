package com.sat.mobilesafe.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Utils
 * Created by OverLord on 2017/1/19.
 */
public class StreamUtil {

    /**
     * @param inputStream 流对象
     * @return 流转换的字符串  返回null代表异常
     */
    public static String stream2string(InputStream inputStream) {
        //1,在读取的过程中,将读取的内容存储值缓存中,然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2,读流操作,读到没有为止(循环)
        byte[] buffer = new byte[1024];
        //3,记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
