package com.sat.mobilesafe.Engine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sat.mobilesafe.Activities.BlackNumActivity;
import com.sat.mobilesafe.Bean.BlackNumInfo;
import com.sat.mobilesafe.Database.BlackNumDao;
import com.sat.mobilesafe.R;

import java.util.List;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Engine
 * Created by 乳龙帝 on 2017/10/26.
 */

public class BlackNumAdapter extends ArrayAdapter {
    int resourceId = 0;

    public BlackNumAdapter(@NonNull Context context, int resource, List<BlackNumInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Log.d("getView","Coming..."+position);
        final BlackNumInfo blackNum = (BlackNumInfo) getItem(position);
        Log.d("getView","Coming..."+position+"..."+blackNum.phone);
        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.black_num_item, null);
            viewHolder.phone = (TextView) view.findViewById(R.id.tv_phone);
            viewHolder.mode = (TextView) view.findViewById(R.id.tv_mode);
            viewHolder.iv_del = (ImageView) view.findViewById(R.id.iv_del);

            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("getView","Position==="+position+"==="+blackNum.phone);
                BlackNumDao instance = BlackNumDao.getInstance(getContext());
                instance.delete(blackNum.phone);
                BlackNumActivity.arrayList.remove(position);
                BlackNumActivity.blackNumAdapter.notifyDataSetChanged();
            }
        });
        viewHolder.phone.setText(blackNum.phone);
        String mode = blackNum.mode;
        switch (mode){
            case "0":
                viewHolder.mode.setText("拦截短信");
                break;
            case "1":
                viewHolder.mode.setText("拦截电话");
                break;
            case "2":
                viewHolder.mode.setText("拦截所有");
                break;
        }
        return view;
    }
    class ViewHolder {
        TextView phone,mode;
        ImageView iv_del;
    }
}
