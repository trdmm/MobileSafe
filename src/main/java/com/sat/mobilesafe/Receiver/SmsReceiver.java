package com.sat.mobilesafe.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Service.LocationService;
import com.sat.mobilesafe.Utils.ConstantValue;
import com.sat.mobilesafe.Utils.SpUtils;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Receiver
 * Created by 乳龙帝 on 2017/4/29.
 */

public class SmsReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(final Context context, Intent intent) {
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        SmsMessage smsMessage;
        //1.查看是否开启防盗保护
        Log.d("TAG","接收到短信");
        boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if (open_security){
            //2.获取短信内容
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            String format = intent.getStringExtra("format");
            //3.循环遍历短信
            for (Object object : pdus){
                //4.获取短信对象
                if (Build.VERSION.SDK_INT>23){
                    smsMessage = SmsMessage.createFromPdu((byte[]) object,format);
                }else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) object);
                }
                //5.获取短信基本信息
                String body = smsMessage.getMessageBody();
                //6.短信内容是否包含播放音乐的#*alarm*#
                if(body.contains("#*alarm*#")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                //7.短信内容是否包含定位的#*location*#
                if(body.contains("#*location*#")){
                    context.startService(new Intent(context, LocationService.class));
                    Log.d("TAG","开启位置服务");
                }
                //8.短信内容是否包含删除远程数据的#*wipedata*#
                if (body.contains("#*wipedata*#")) {

                    mDPM.wipeData(0);
                }
                //9.短信内容是否包含远程锁屏的#*lockscreen*#
                if(body.contains("#*lockscreen*#")){
                    mDPM.lockNow();
                    mDPM.resetPassword("qwer",0);
                }
            }
        }
    }
}
