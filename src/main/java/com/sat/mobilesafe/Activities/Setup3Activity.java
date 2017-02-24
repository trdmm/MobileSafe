package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Utils.ToastUtil;

public class Setup3Activity extends BaseSetUpActivity {

    private EditText et_phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
        initData();
    }

    private void initData() {
        String phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_NUM, "");
        et_phone_num.setText(phone);
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String phone = et_phone_num.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            //非空，保存，进入下一页
            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_NUM,phone);
            ToastUtil.show(getApplicationContext(),et_phone_num.getText().toString());
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            //为空，提示输入号码
            ToastUtil.show(getApplicationContext(),"请输入联系人号码");
        }

    }

    private void initUI() {
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        Button bt_select_num = (Button) findViewById(R.id.bt_select_num);
        bt_select_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    //返回的电话号码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            String phone = data.getStringExtra("phone");
            phone = phone.split("\n")[1].replace("-", "").replace(" ", "").trim();
            et_phone_num.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
