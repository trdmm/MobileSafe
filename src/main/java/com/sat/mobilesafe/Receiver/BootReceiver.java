package com.sat.mobilesafe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Receiver
 * Created by 乳龙帝 on 2017/4/23.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG","接收到开机广播");
    }
}
