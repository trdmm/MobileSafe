package com.sat.mobilesafe.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Service.RocketService;
import com.sat.mobilesafe.Utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {

    Timer timer = new Timer();
    /*TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(getApplicationContext(),"Timer调用=====");
                }
            });
        }
    };*/
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                ToastUtil.show(getApplicationContext(),"Timer调用=====");
            }
        }
    };
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //checkPermission();
        timer.schedule(task,2000);
        Button bt_start = (Button) findViewById(R.id.bt_start);
        Button bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(23)
            @Override
            public void onClick(View v) {
                checkPermission();
                Uri uri = Uri.parse("package:" + getPackageName());
                Log.d("TAG","Uri:====="+uri);
                if (Build.VERSION.SDK_INT>=23){
                    if (Settings.canDrawOverlays(TestActivity.this)){
                        Intent intent = new Intent(getApplicationContext(), RocketService.class);
                        startService(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), RocketService.class);
                    startService(intent);
                    finish();
                }
            }
        });
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RocketService.class);
                stopService(intent);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @TargetApi(23)
    private void checkPermission() {
        //Log.d("TAG",Build.VERSION.SDK_INT+"");
        if (Build.VERSION.SDK_INT >= 23){
            if (!Settings.canDrawOverlays(TestActivity.this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,0);
            }
        }
    }

    @TargetApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            if (!Settings.canDrawOverlays(this)){
                ToastUtil.show(this,"未能允许应用未与其他桌面之上");
            }
        }
    }
}
