package com.sat.mobilesafe.Engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Engine
 * Created by 乳龙帝 on 2017/5/18.
 */

public class AddressDao {
    //1.指定访问数据库的路径
    public static String path = "/data/data/com.sat.mobilesafe/files/address.db";
    private static String outkey;
    static String location = "未知号码";
    private static SQLiteDatabase db;

    /**
     * 传递一个电话号码，开启数据库连接，进行访问，返回一个归属地
     * @param phone  查询的电话号码
     */
    public static String getAddress(String phone){
        String  regularExpression = "^1[3-8]\\d{9}";
        if (phone.matches(regularExpression)){
            phone = phone.substring(0, 7);
            Log.d("TAG",phone);
            //2.开启数据库连接
            db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id=?", new String[]{phone}, null, null, null);
            if (cursor.moveToNext()){
                outkey = cursor.getString(0);
                Log.d("TAG","outkey="+ outkey);
                Cursor indexCursor = db.query("data2", new String[]{"location"}, "id=?", new String[]{outkey}, null, null, null);
                if (indexCursor.moveToNext()){
                    location = indexCursor.getString(0);
                    //下一句执行不到，报此错误
                    //Log.d("TAG","location="+phone);
                }
            }
        }
        else {
            int length = phone.length();
            switch (length){
                //3,5,
                case 3:
                    location = "报警电话";
                    break;
                case 4://119 110 120 114
                    location = "模拟器";
                    break;
                case 5://10086 99555
                    location = "服务电话";
                    break;
                case 7:
                    location = "本地电话";
                    break;
                case 8:
                    location = "本地电话";
                    break;
                case 11:
                    //(3+8) 区号+座机号码(外地),查询data2
                    String area = phone.substring(1, 3);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                    if(cursor.moveToNext()){
                        location = cursor.getString(0);
                    }else{
                        location = "未知号码";
                    }
                    break;
                case 12:
                    //(4+8) 区号(0791(江西南昌))+座机号码(外地),查询data2
                    String area1 = phone.substring(1, 4);
                    Cursor cursor1 = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area1}, null, null, null);
                    if(cursor1.moveToNext()){
                        location = cursor1.getString(0);
                    }else{
                        location = "未知号码";
                    }
                    break;
            }
        }

        return location;
    }
}
