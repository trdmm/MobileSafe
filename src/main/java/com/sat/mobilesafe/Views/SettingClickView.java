package com.sat.mobilesafe.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sat.mobilesafe.R;

/**
 * Created by knight on 17-1-23.
 */

public class SettingClickView extends RelativeLayout {

    public static final String tag = "SettingItemView";
    public static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private TextView tv_title;
    private TextView tv_des;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_click_view, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
    }

    /**
     * 设置标题内容
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置描述内容
     *
     * @param des 描述内容
     */
    public void setDes(String des) {
        tv_des.setText(des);
    }


}
