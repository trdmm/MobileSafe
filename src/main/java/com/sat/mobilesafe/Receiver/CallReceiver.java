package com.sat.mobilesafe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Receiver
 * Created by 乳龙帝 on 2017/8/7.
 */

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CALL)){
            Log.d("TAG",Intent.ACTION_CALL+"===");
        }
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Log.d("TAG",Intent.ACTION_NEW_OUTGOING_CALL+"===");
        }
    }
}
