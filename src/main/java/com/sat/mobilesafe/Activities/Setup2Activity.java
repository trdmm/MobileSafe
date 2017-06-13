package com.sat.mobilesafe.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Utils.ToastUtil;
import com.sat.mobilesafe.Views.SettingItemView;

public class Setup2Activity extends BaseSetUpActivity {


    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED) {
            Log.d("TAG","权限");
            ToastUtil.show(this, "请开启权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        //1.检测是否绑定SIM卡
        String sim_num = SpUtils.getString(this, ConstantValue.SIM_NUM, "");
        if (TextUtils.isEmpty(sim_num)) {
            //为空，提示绑定
            ToastUtil.show(this, "请绑定SIM卡");
        } else {
            //非空，进入下一页
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }
    }

    private void initUI() {
        final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1.回显（读取已有的绑定状态，用作显示，即SP中是否存储了SIM卡序列号）
        String sim_num = SpUtils.getString(getApplicationContext(), ConstantValue.SIM_NUM, "");
        //2.判断序列号是否为空
        if (TextUtils.isEmpty(sim_num)) {
            //SIM卡未绑定
            siv_sim_bound.setChecked(false);
        } else {
            //SIM卡已绑定
            siv_sim_bound.setChecked(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3.获取原有状态
                boolean isChecked = siv_sim_bound.isChecked();
                //4.将原有状态去翻
                //5.将状态设置给条目(View)
                siv_sim_bound.setChecked(!isChecked);
                if (!isChecked) {
                    //4.1 将序列号存进SP中
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    SpUtils.putString(getApplicationContext(), ConstantValue.SIM_NUM, simSerialNumber);
                } else {
                    //4.2 将序列号从SP中移除
                    SpUtils.remove(getApplicationContext(), ConstantValue.SIM_NUM);
                }
            }
        });
    }

}