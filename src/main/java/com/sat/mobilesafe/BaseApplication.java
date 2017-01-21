package com.sat.mobilesafe;

import android.app.Application;

import org.xutils.x;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Activities
 * Created by OverLord on 2017/1/20.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
