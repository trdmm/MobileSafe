package com.sat.mobilesafe.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_style;
    private SettingClickView scv_toast_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
        initAddress();
        initToastStyle();
        initToastLocation();
        initTest();
    }

    private void initTest() {
        Button bt_test = (Button) findViewById(R.id.bt_test);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置来电归属地Toast的显示位置
     */
    private void initToastLocation() {
        scv_toast_location = (SettingClickView) findViewById(R.id.scv_toast_location);
        scv_toast_location.setTitle("归属地提示框的位置");
        scv_toast_location.setDes("设置归属地提示框的位置");
        scv_toast_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ToastLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToastStyle() {
        scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置归属地显示风格");
        mToastStyleDes = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        mToastStyle = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        scv_toast_style.setDes(mToastStyleDes[mToastStyle]);
        scv_toast_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastStyleDialog();
            }
        });
    }

    /**
     * 创建选中显示样式对话框
     */
    private void showToastStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app_icon);
        builder.setTitle("请选择归属地显示样式");
        mToastStyle = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        builder.setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1.记录选中的索引值 2.关闭对话框 3.显示选中色值的文字
                SpUtils.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,which);
                dialog.dismiss();
                scv_toast_style.setDes(mToastStyleDes[which]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
