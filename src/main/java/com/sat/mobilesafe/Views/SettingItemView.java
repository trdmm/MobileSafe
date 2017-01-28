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

public class SettingItemView extends RelativeLayout {

    public static final String tag = "SettingItemView";
    public static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_item_view, this);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        cb_box = (CheckBox) view.findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作
        initAttrs(attrs);
        tv_title.setText(mDestitle);
    }

    /**
     * 返回属性集合中自定义属性值
     * @param attrs 构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs) {
        /*Log.d(tag,"Total:"+attrs.getAttributeCount());
        for (int i=0;i<attrs.getAttributeCount();i++){
            Log.d(tag,"name:"+attrs.getAttributeName(i));
            Log.d(tag,"value:"+attrs.getAttributeValue(i));
            Log.d(tag,"----------------------------------");

        }*/
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
    }

    /**
     * 判断条目是否被选中
     * @return true:条目(CheckBox)被选中  false:条目(CheckBox)未被选中
     */
    public boolean isChecked(){
        return cb_box.isChecked();
    }

    public void setChecked(boolean isChecked){
        cb_box.setChecked(isChecked);
        if (isChecked){
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }

}
