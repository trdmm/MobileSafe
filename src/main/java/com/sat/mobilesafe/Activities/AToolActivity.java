package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sat.mobilesafe.R;

public class AToolActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        //1.电话归属地查询功能
        initPhoneAddress();

    }

    private void initPhoneAddress() {
        TextView tv_phone_address_query = (TextView) findViewById(R.id.tv_phone_address_query);
        tv_phone_address_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
            }
        });
    }
}
