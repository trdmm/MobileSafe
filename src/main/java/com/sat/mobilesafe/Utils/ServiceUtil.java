package com.sat.mobilesafe.Utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Utils
 * Created by 乳龙帝 on 2017/6/6.
 */

public class ServiceUtil {
    public static boolean isRunning(Context ctx, String service) {
        //获取ActivityManager管理对象
        ActivityManager mAM = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);
        //遍历获取的所有的服务集合，拿到每一个服务的类的名称
        for (ActivityManager.RunningServiceInfo serviceInfo :
                runningServices) {
            //和传递进来的类名称做对比，如果一致，正在运行
            if (service.equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
