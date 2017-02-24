package com.sat.mobilesafe.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by overlord on 17-2-22.
 */

public abstract class BaseSetUpActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 0) {
                    //由右向左移动
                    showNextPage();
                }
                if (e1.getX() - e2.getX() < 0) {
                    //由左向右移动
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void next_page(View v){
        showNextPage();
    }
    public void pre_page(View v){
        showPrePage();
    }
    protected abstract void showPrePage();

    protected abstract void showNextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过手势处理类，接受多种类型的事件，用做处理
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
