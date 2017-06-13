package com.sat.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.sat.mobilesafe.Activities.SetupOverActivity;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Utils.ToastUtil;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Service
 * Created by 乳龙帝 on 2017/5/4.
 */

public class LocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取坐标
        //1.获得位置管理者
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.以最优方式获得经纬度
        Criteria criteria = new Criteria();
        criteria.isCostAllowed();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        //3.以一定的时间间隔、位置移动距离获得坐标
        MyLocationListener locationListener = new MyLocationListener();
        //4.判断api版本，是否使用checkSelfPermission,如果版本>=23，且2个权限都未允许，则提示开启权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        == PackageManager.PERMISSION_GRANTED
//        )) {
//            ToastUtil.show(this, "请开启权限");
//            ActivityCompat.requestPermissions(SetupOverActivity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
//        } else {
            lm.requestLocationUpdates(bestProvider,0,0,locationListener);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.d("TAG",longitude+"+"+latitude);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5554",null,"location:"+longitude+","+latitude,null,null);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
