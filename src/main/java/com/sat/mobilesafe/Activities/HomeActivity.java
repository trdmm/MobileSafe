package com.sat.mobilesafe.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.Md5Util;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Utils.ToastUtil;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Activities
 * Created by OverLord on 2017/1/20.
 */
public class HomeActivity extends AppCompatActivity {

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mDrawableIds = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDialog();
                        Log.d("TAG","手机防盗");
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showDialog() {
        //判断本地是否有存储密码(SP 字符串)
        String pwd = SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");
        Log.d("TAG",pwd);
        if (TextUtils.isEmpty(pwd)) {
            //密码为空，初始设置密码框
            showSetPwdDialog();
            Log.d("TAG","开启对话框");
        } else {
            //密码不为空，确认密码对话框
            showConfirmPwdDialog();
        }
    }

    private void showConfirmPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
                String pwd = Md5Util.encoder(et_confirm_pwd.getText().toString());
                String pwd_sp = SpUtils.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PWD, "");
                Log.d("TAG","pwd:"+pwd+"\n"+"pwd_sp:"+pwd_sp);
                if (pwd.equals(pwd_sp)){
                    Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入正确密码");
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

    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);

                String pwd = et_set_pwd.getText().toString();
                String confirmPwd = et_confirm_pwd.getText().toString();
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)){
                    //密码非空，检验密码是否相等
                    if (pwd.equals(confirmPwd)){
                        //密码相同进入防盗页面
                        SpUtils.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PWD,pwd);
                        Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else {
                        ToastUtil.show(getApplicationContext(),"请确认2次密码相同");
                    }
                }else{
                    //密码为空
                    ToastUtil.show(getApplicationContext(),"密码不能为空");
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

    /**
     * 初始化UI
     */
    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStr[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return view;
        }
    }

}
