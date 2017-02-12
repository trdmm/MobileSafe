package com.sat.mobilesafe.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;
import com.sat.mobilesafe.Views.SettingItemView;

public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUpdate();
    }
public void next_page(View v){
    Intent intent = new Intent(this, Setup3Activity.class);
    startActivity(intent);
    finish();
}
    public void pre_page(View v){
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
    private void initUpdate() {
    }
}