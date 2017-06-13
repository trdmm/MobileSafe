package com.sat.mobilesafe.Activities;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sat.mobilesafe.Engine.AddressDao;
import com.sat.mobilesafe.R;

import java.lang.ref.WeakReference;

public class QueryAddressActivity extends AppCompatActivity {

    private static TextView tv_query_result;
    private EditText et_address_phone;
    /*private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_query_result.setText(msg.obj.toString());
            super.handleMessage(msg);
        }
    };*/

    private static class MyHandler extends Handler {
        private final WeakReference<QueryAddressActivity> mActivity;

        public MyHandler(QueryAddressActivity activity) {
            mActivity = new WeakReference<QueryAddressActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            QueryAddressActivity activity = mActivity.get();
            if (activity != null) {
                tv_query_result.setText(msg.obj.toString());
                super.handleMessage(msg);
            }
        }
    }
    MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        Button bt_query = (Button) findViewById(R.id.bt_query);
        et_address_phone = (EditText) findViewById(R.id.et_address_phone);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_address_phone.getText().toString())){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    findViewById(R.id.et_address_phone).startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    vibrator.vibrate(new long[]{2000,5000,2000,5000},2);
                }else {
                    //耗时操作，开启子线程
                    query(et_address_phone.getText().toString());
                }
            }
        });
        et_address_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query(et_address_phone.getText().toString());
            }
        });
    }

    /**
     * 耗时操作
     * 获取电话号码归属地
     * @param phone 查询的号码
     */
    private void query(final String phone) {
        //Log.d("TAG","查询操作");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String location = AddressDao.getAddress(phone);
                //消息机制，查询结束告知主线程
                Message msg = Message.obtain();
                msg.obj = location;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
}
