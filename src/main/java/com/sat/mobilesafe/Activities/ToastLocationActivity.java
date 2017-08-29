package com.sat.mobilesafe.Activities;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;

/**
 * 来电归属地吐司位置设置
 */
public class ToastLocationActivity extends AppCompatActivity {

    private ImageView iv_drag;
    private Button bt_top;
    private Button bt_bottom;
    private int mHeightPixels;
    private int mWidthPixels;
    private long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        initUI();
    }

    private void initUI() {
        //可拖曳双击居中的控件
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mHeightPixels = dm.heightPixels;
        mWidthPixels = dm.widthPixels;
        //根据SP中保存的坐标设置iv_drag的位置
        int locationX = SpUtils.getInt(this, ConstantValue.IMAGE_LOCATION_LEFT, 0);
        int locationY = SpUtils.getInt(this, ConstantValue.IMAGE_LOCATION_TOP, 0);
        //左上角坐标在iv_drag上，iv_drag在父控件相对布局上，其所在位置的规则需要由相对布局提供
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;
        //iv_drag设置上面的参数
        iv_drag.setLayoutParams(layoutParams);
        if (locationY > mHeightPixels / 2) {
            //隐藏下Button
            Log.d("TAG", locationY + "========" + mHeightPixels / 2);
            bt_bottom.setVisibility(View.INVISIBLE);
            bt_top.setVisibility(View.VISIBLE);
        } else {
            //隐藏上Button
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }

        //监听某一个控件的拖曳动作(按下(1次)，拖曳，抬起(1次))
        iv_drag.setOnTouchListener(new View.OnTouchListener() {

            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //0.初始点的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        v.performClick();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //0.移动后的坐标
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        //0.移动的距离
                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        //1.当前控件所在屏幕的左(上)距离
                        int left = iv_drag.getLeft() + disX;
                        int top = iv_drag.getTop() + disY;
                        int right = iv_drag.getRight() + disX;
                        int bottom = iv_drag.getBottom() + disY;


                        //2.告知控件，按计算出来的坐标去做展示
                        if (!(left < 0 || top < 0 || right > mWidthPixels || bottom > mHeightPixels)) {
                            iv_drag.layout(left, top, right, bottom);
                        }

                        if (top > mHeightPixels / 2) {
                            //隐藏下Button
                            bt_bottom.setVisibility(View.INVISIBLE);
                            bt_top.setVisibility(View.VISIBLE);
                        } else {
                            //隐藏上Button
                            bt_top.setVisibility(View.INVISIBLE);
                            bt_bottom.setVisibility(View.VISIBLE);
                        }

                        //3.重置初始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //4.记录当前位置
                        SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_LEFT, iv_drag.getLeft());
                        SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_TOP, iv_drag.getTop());
                        Log.d("tag","移动后保存"+iv_drag.getLeft()+"---"+iv_drag.getTop());
                        //v.performClick();
                        break;
                }
                //返回false不会响应事件
                return true;
            }

        });

        /**
         * 双击居中
         */
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "点击事件"+mHits[0] + "======" + mHits[1]+"长度："+mHits.length);
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[mHits.length - 1] - mHits[0] < 1000) {
                    Log.d("TAG", "双击事件");
                    //间隔小于1000,则是双击 iv_drag居中
                    int height = iv_drag.getHeight();
                    int width = iv_drag.getWidth();

                    int left = (mWidthPixels - width) / 2;
                    int top = (mHeightPixels - height) / 2;
                    int right = (mWidthPixels + width) / 2;
                    int bottom = (mHeightPixels + height) / 2;

                    iv_drag.layout(left,top,right,bottom);
                    SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_LEFT, iv_drag.getLeft());
                    SpUtils.putInt(getApplicationContext(), ConstantValue.IMAGE_LOCATION_TOP, iv_drag.getTop());
                }
            }
        });
    }
}
