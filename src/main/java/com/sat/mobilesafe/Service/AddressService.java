package com.sat.mobilesafe.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sat.mobilesafe.Engine.AddressDao;
import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;


public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyPhoneStateListener myPhoneStateListener;
    private View mViewToast;
    private WindowManager mWM;
    private TextView tv_toast;
    private int mHeightPixels;
    private int mWidthPixels;

    public AddressService() {
    }

    public static String tag = "AddressService";

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.d("TAG",mAddress);
            String mAddress = (String) msg.obj;
            tv_toast.setText(mAddress);
        }
    };
    @Override
    public void onCreate() {
        //第一次开启服务，就需要管理吐司
        //电话状态监听
        mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //获取窗体对象
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        //销毁吐司，取消监听
        if (mTM != null && myPhoneStateListener != null) {
            mTM.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态
                    Log.d(tag, "挂断电话，空闲了................");
                    if (mWM != null && mViewToast != null){
                        mWM.removeView(mViewToast);
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃
                    Log.d(tag, "来电话了，响铃................");
                    showToast(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机(相当于座机拿起话筒)
                    Log.d(tag, "摘机了................");
                    showToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String incomingNumber) {
        //Toast.makeText(this,incomingNumber,Toast.LENGTH_LONG).show();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //指定吐司所在位置
        params.gravity = Gravity.LEFT + Gravity.TOP;
        //指定吐司布局 xml-->view,将view挂载到windowManager上
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);

        params.x = SpUtils.getInt(getApplicationContext(),ConstantValue.IMAGE_LOCATION_LEFT,0);
        params.y = SpUtils.getInt(getApplicationContext(),ConstantValue.IMAGE_LOCATION_TOP,0);
        //Log.d("TAG","来电时"+params.x+"---"+params.y);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mHeightPixels = dm.heightPixels;
        mWidthPixels = dm.widthPixels;
        mViewToast.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //0.初始点的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
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
                        Log.d("TAG","Service中移动时"+params.x+"---"+params.y);

                        //2.告知控件，按计算出来的坐标去做展示
                        if (params.x<0){
                            params.x=0;
                        }
                        if (params.x>mWidthPixels-mViewToast.getWidth()){
                            params.x=mWidthPixels-mViewToast.getWidth();
                        }
                        if (params.y<0){
                            params.y=0;
                        }
                        if (params.y > mHeightPixels-mViewToast.getHeight()){
                            params.y = mHeightPixels-mViewToast.getHeight();
                        }
//                        if (!(params.x<0 || params.x>mWidthPixels-mViewToast.getWidth() ||
//                                params.y<0 ||params.y > mHeightPixels-mViewToast.getHeight())) {
                        mWM.updateViewLayout(mViewToast, params);
//                    }

                        //3.重置初始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //4.记录当前位置
                        SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_LEFT, params.x);
                        SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_TOP, params.y);
                        break;
                }
                return true;
            }
        });


        //从SP中获取色值文字的索引，匹配图片
        //"透明", "橙色", "蓝色", "灰色", "绿色"
        int[] drawableIds = new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,
                R.drawable.call_locate_grey,R.drawable.call_locate_green};
        int toastStyleIndex = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(drawableIds[toastStyleIndex]);
        //将布局和params添加到窗体对象中(权限)
        mWM.addView(mViewToast,params);
        query(incomingNumber);
    }

    private void query(final String incomingNumber) {
 /*       new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
*/
        new Thread(){
            @Override
            public void run() {
                super.run();
                String mAddress = AddressDao.getAddress(incomingNumber);
                Message msg = Message.obtain();
                msg.obj = mAddress;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
}
