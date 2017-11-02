package com.sat.mobilesafe.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.sat.mobilesafe.Bean.BlackNumInfo;
import com.sat.mobilesafe.Database.BlackNumDao;
import com.sat.mobilesafe.Engine.BlackNumAdapter;
import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BlackNumActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private EditText et_black_num;
    private BlackNumDao instance;
    private int mode = 0;
    public static List<BlackNumInfo> arrayList = new ArrayList<BlackNumInfo>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blackNumAdapter = new BlackNumAdapter(getApplicationContext(), R.layout.black_num_item, arrayList);
            lv_black_num.setAdapter(blackNumAdapter);
        }
    };
    private ListView lv_black_num;
    public static BlackNumAdapter blackNumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_num);
        mContext = this;
        initUI();
        initData();
    }

    private void initData() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();*/
        new Thread() {
            @Override
            public void run() {
                instance = BlackNumDao.getInstance(mContext);
                arrayList = instance.query();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_black_num = (ListView) findViewById(R.id.lv_black_num);
        Button bt_black_num_add = (Button) findViewById(R.id.bt_black_num_add);
        //arrayList = instance.query();
        bt_black_num_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_black_num_add:
                showDialog();
                break;
        }
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.black_num_add_dialog, null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        RadioGroup rg_black_num_mode = (RadioGroup) view.findViewById(R.id.rg_black_num_mode);
        et_black_num = (EditText) view.findViewById(R.id.et_black_num);
        bt_submit.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        rg_black_num_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = 0;
                        Log.d("MODE", "模式是:-----" + mode);
                        break;
                    case R.id.rb_tel:
                        mode = 1;
                        Log.d("MODE", "模式是:-----" + mode);
                        break;
                    case R.id.rb_all:
                        mode = 2;
                        Log.d("MODE", "模式是:-----" + mode);
                        break;
                }
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_black_num.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    instance.insert(et_black_num.getText().toString(), mode + "");
                    BlackNumInfo blackNumInfo = new BlackNumInfo();
                    blackNumInfo.phone = phone;
                    blackNumInfo.mode = mode + "";
                    arrayList.add(0, blackNumInfo);
                    blackNumAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    ToastUtil.show(mContext, "请输入号码");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
