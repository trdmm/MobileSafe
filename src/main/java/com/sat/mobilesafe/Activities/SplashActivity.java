package com.sat.mobilesafe.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.StreamUtil;
import com.sat.mobilesafe.Utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Activities
 * Created by OverLord on 2017/1/19.
 */

public class SplashActivity extends AppCompatActivity {

    public static final String tag = "SplashActivity";
    private static final int UPDATE_VERSION = 1;
    private static final int ENTER_HOME = 2;
    private static final int URLEXCEPTION = 3;
    private static final int IOEXCEPTION = 4;
    private static final int JSONEXCEPTION = 5;
    private TextView tv_version_name;
    private int mLocalVersionCode;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URLEXCEPTION:
                    ToastUtil.show(SplashActivity.this,"URL异常");
                    enterHome();
                    break;
                case IOEXCEPTION:
                    ToastUtil.show(SplashActivity.this,"IO异常");
                    enterHome();
                    break;
                case JSONEXCEPTION:
                    ToastUtil.show(SplashActivity.this,"JSON异常");
                    enterHome();
                    break;
            }
        }
    };
    private String mVersionDes;
    private String mVersionUrl;

    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("升级提醒");
        builder.setIcon(R.drawable.app_icon);
        builder.setMessage(mVersionDes);
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();
            }
        });
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 2017/1/20  xUtils3实现下载功能
                download();
            }
        });
        builder.show();
    }

    private void download() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            String url = mVersionUrl;
            RequestParams requestParams = new RequestParams(url);
            //String savePath = Environment.getExternalStorageDirectory().getPath()+ File.separator+"mobilesafe.apk";
            String savePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "mobilesafe.apk";
            requestParams.setSaveFilePath(savePath);
            Log.d(tag, url + "\n" + savePath);
            org.xutils.x.http().get(requestParams, new Callback.CommonCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.d(tag, "onSuccess");
                    installApk(result);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    
                    Log.d(tag, "error");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.d(tag, "cancelled");
                }

                @Override
                public void onFinished() {
                    Log.d(tag, "finish");
                }
            });
        }
    }
    private void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化UI
        initUI();
        //初始化Data
        initData();
    }

    /**
     * 初始化Data
     */
    private void initData() {
        //1.设置版本名称
        tv_version_name.setText("版本名称:" + getVersionName());
        //检测（本地版本号跟服务器版本号）是否有更新，如果有更新，提示用户下载
        //2.获取本地版本号
        mLocalVersionCode = getmLocalVersionCode();
        //3.获取服务器版本号
        //http://www.xxx.com/updateVersion.json?key=value  流的方式将数据读取下来
        checkVersion();
    }

    /**
     * 获取应用版本名
     *
     * @return 非0代表成功
     */
    private int getmLocalVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 子进程检测版本号
     */
    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                //发送http请求
                try {
                    //1.封装url地址
                    URL url = new URL("http://192.168.1.104:8080/updateVersion.json");
                    //2.开启HttpURLConnection连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置参数。(请求头信息)
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(5000);
                    //4,获取请求码
                    int responseCode = connection.getResponseCode();
                    if (responseCode==200){
                        //5.以流的方式读取返回的信息
                        InputStream inputStream = connection.getInputStream();
                        //6.将流转字符串
                        String json = StreamUtil.stream2string(inputStream);
                        Log.d(tag,json);
                        //7.JSON解析
                        JSONObject jsonObject = new JSONObject(json);

                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mVersionUrl = jsonObject.getString("versionUrl");
                        //8.比对版本号
                        if (Integer.parseInt(versionCode)>mLocalVersionCode){
                            Log.d(tag,"服务器："+Integer.parseInt(versionCode)+"本地："+mLocalVersionCode);
                            msg.what = UPDATE_VERSION;
                        //提示用户更新
                        }else {
                            //进入主界面
                            //Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                            msg.what = ENTER_HOME;
                        }
                        mHandler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    msg.what = URLEXCEPTION;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = IOEXCEPTION;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSONEXCEPTION;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 获取应用版本名称
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }
}
