package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Utils.ToastUtil;

public class Setup4Activity extends BaseSetUpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        final CheckBox cb_setup_over = (CheckBox) findViewById(R.id.cb_setup_open);
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        cb_setup_over.setChecked(setup_over);
        if (setup_over){
            cb_setup_over.setText("您已开启防盗保护");
        }else {
            cb_setup_over.setText("您没有开启防盗保护");
        }

        cb_setup_over.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                if (isChecked){
                    cb_setup_over.setText("您已开启防盗保护");
                }else {
                    cb_setup_over.setText("您没有开启防盗保护");
                }
            }
        });
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if (open_security){
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER,true);
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            ToastUtil.show(this,"请开启防盗保护");
        }
    }
}
