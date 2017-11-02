package com.sat.mobilesafe.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Utils
 * Created by OverLord on 2017/1/20.
 */
public class ToastUtil {
    /**
     * 弹出吐司
     * @param context  上下文环境
     * @param s 显示的内容
     */
    public static void show(Context context,String string) {
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }
}
