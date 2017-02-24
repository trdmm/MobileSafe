package com.sat.mobilesafe.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;

public class Setup4Activity extends BaseSetUpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
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
        Intent intent = new Intent(this, SetupOverActivity.class);
        startActivity(intent);
        finish();
        SpUtils.putBoolean(this, ConstantValue.SETUP_OVER,true);
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
}
