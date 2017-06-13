package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Service.AddressService;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.ServiceUtil;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Views.SettingClickView;
import com.sat.mobilesafe.Views.SettingItemView;

/**
 * Created by knight on 17-1-23.
 */

public class SettingActivity extends AppCompatActivity {

    private String[] mToastStyle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
        initAddress();
        initToastStyle();
    }

    private void initToastStyle() {
        SettingClickView scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置归属地显示风格");
        mToastStyle = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        int toast_style = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        scv_toast_style.setDes(mToastStyle[toast_style]);
    }

    /**
     * 开启来电归属地显示
     */
    private void initAddress() {
        final SettingItemView siv_phone_address = (SettingItemView) findViewById(R.id.siv_phone_address);
        boolean isRunning = ServiceUtil.isRunning(this, "com.sat.mobilesafe.Service.AddressService");
        if (isRunning){
            Log.d("TAG","服务已开启....................");
        }else {
            Log.d("TAG","服务已关闭....................");

        }
        siv_phone_address.setChecked(isRunning);
        siv_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = siv_phone_address.isChecked();
                siv_phone_address.setChecked(!isChecked);
                if (!isChecked){
                    //开启状态，开启服务，管理吐司
                    startService(new Intent(getApplicationContext(),AddressService.class));
                }else {
                    //关闭状态，关闭服务，不需要吐司
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

    /**
     * 是否开启版本更新
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的开关状态用作显示
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setChecked(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = siv_update.isChecked();
                siv_update.setChecked(!isChecked);
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isChecked);
            }
        });
    }
}
