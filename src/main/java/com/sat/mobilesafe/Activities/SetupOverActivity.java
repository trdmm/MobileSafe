package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;

/**
 * Created by knight on 17-1-24.
 */
public class SetupOverActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over){
            setContentView(R.layout.activity_setup_over);
            initUI();
        }else {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initUI() {
        String num = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_NUM, "");
        TextView tv_safe_num = (TextView) findViewById(R.id.tv_safe_num);
        TextView re_set = (TextView) findViewById(R.id.re_set);

        tv_safe_num.setText(num);
        re_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
