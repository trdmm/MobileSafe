package com.sat.mobilesafe.Receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Receiver
 * Created by 乳龙帝 on 2017/4/23.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG","接收到开机广播");
        String sim_num = SpUtils.getString(context, ConstantValue.SIM_NUM, "");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber();
        if (sim_num != simSerialNumber){
            SmsManager smsManager = SmsManager.getDefault();
            String contact_num = SpUtils.getString(context, ConstantValue.CONTACT_NUM, "");
            smsManager.sendTextMessage(contact_num,null,"SIM卡变更",null,null);
        }
    }
}
