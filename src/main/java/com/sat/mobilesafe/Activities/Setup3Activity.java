package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sat.mobilesafe.R;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }
    public void next_page(View v){
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
    }
    public void pre_page(View v){
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
