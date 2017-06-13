package com.sat.mobilesafe.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sat.mobilesafe.Engine.AddressDao;
import com.sat.mobilesafe.R;


public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyPhoneStateListener myPhoneStateListener;
    private View mViewToast;
    private WindowManager mWM;
    private TextView tv_toast;

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
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String incomingNumber) {
        //Toast.makeText(this,incomingNumber,Toast.LENGTH_LONG).show();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //指定吐司所在位置
        params.gravity = Gravity.LEFT + Gravity.BOTTOM;
        //指定吐司布局 xml-->view,将view挂载到windowManager上
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
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
