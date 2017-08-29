package com.sat.mobilesafe.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.RocketSmokeActivity;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Service
 * Created by 乳龙帝 on 2017/7/15.
 */

public class RocketService extends Service {

    private WindowManager mWM;
    private View mRocketView;
    private int mWidthPixels;
    private int mHeightPixels;
    private WindowManager.LayoutParams params;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mWM.updateViewLayout(mRocketView,params);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = mWM.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;
        showRocket();
    }

    private void showRocket() {
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                //| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //params.gravity = Gravity.LEFT + Gravity.TOP;
        mRocketView = View.inflate(this, R.layout.rocket_view, null);
        ImageView iv_rocket = (ImageView) mRocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable background = (AnimationDrawable) iv_rocket.getBackground();
        background.start();
        mRocketView.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            public int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //0.初始点的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        //Log.d("TAG","Service中移动按下时"+startX+"---"+startY);
                        Log.d("TAG","params.x="+params.x+"====params.y="+params.y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //0.移动后的坐标
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        //0.移动的距离
                        int disX = moveX - startX;
                        int disY = moveY - startY;


                        //1.当前控件所在屏幕的左(上)距离
                        params.x += disX;
                        params.y += disY;
                        //Log.d("TAG","Service中移动时"+params.x+"---"+params.y);

                        //2.告知控件，按计算出来的坐标去做展示
                        /*if (params.x<0){
                            params.x=0;
                        }
                        if (params.x>mWidthPixels-mRocketView.getWidth()){
                            params.x=mWidthPixels-mRocketView.getWidth();
                        }
                        if (params.y<0){
                            params.y=0;
                        }
                        if (params.y > mHeightPixels-mRocketView.getHeight()){
                            params.y = mHeightPixels-mRocketView.getHeight();
                        }*/
//                        if (!(params.x<0 || params.x>mWidthPixels-mViewToast.getWidth() ||
//                                params.y<0 ||params.y > mHeightPixels-mViewToast.getHeight())) {
                        mWM.updateViewLayout(mRocketView, params);
//                    }

                        //3.重置 初始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //4.松开上天
                        if (startX > mWidthPixels/6 && startX < mWidthPixels*5/6 &&
                                startY > mHeightPixels*7/10)
                        {
                            Intent intent = new Intent(getApplicationContext(), RocketSmokeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            sendRocket();
                            Log.d("TAG","螺旋升天位置");
                        }
                        break;
                }
                return true;
            }
        });
        mWM.addView(mRocketView, params);
        Log.d("TAG","小火箭开启。。。。");
    }

    private void sendRocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int height = mHeightPixels;
                while (height > -mHeightPixels/2){
                    height = height - mHeightPixels/8;
                    params.y = height;
                    SystemClock.sleep(50);
                    Log.d("TAG","Height = "+height+"y="+params.y);
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWM != null && mRocketView !=null){
            mWM.removeView(mRocketView);
            Log.d("TAG","小火箭关闭。。。。");
        }
    }
}
