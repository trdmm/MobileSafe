package com.sat.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class RocketSmokeActivity extends Activity {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_smoke);
        ImageView iv_smoke_bottom = (ImageView) findViewById(R.id.iv_smoke_bottom);
        ImageView iv_smoke_top = (ImageView) findViewById(R.id.iv_smoke_top);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        iv_smoke_bottom.startAnimation(alphaAnimation);
        iv_smoke_top.startAnimation(alphaAnimation);
        handler.sendEmptyMessageDelayed(0,1000);
    }
}
