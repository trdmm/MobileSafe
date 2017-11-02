package com.sat.mobilesafe.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sat.mobilesafe.Bean.BlackNumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Database
 * Created by 姬岛朱乃 on 2017/10/24 at 16:44.
 */

public class BlackNumDao {

    private final DbOpenhelper dbOpenhelper;

    //BlackNumDao单例模式
    //1.私有化构造方法
    private BlackNumDao(Context context) {
        dbOpenhelper = new DbOpenhelper(context);
    }

    //2.声明一个类对象
    private static BlackNumDao blackNumDao = null;

    //3.提供一个方法，如果当前类对象为空则创建新的。return 类对象
    public static BlackNumDao getInstance(Context context) {
        if (blackNumDao == null) {
            //synchronized (BlackNumDao.class){
            blackNumDao = new BlackNumDao(context);
            //}
        }
        return blackNumDao;
    }

    /**
     * 查询数据库
     */
    public List<BlackNumInfo> query() {
        SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
        Cursor cursor = db.query("blacknum", new String[]{"_id", "phone", "mode"}, null, null, null, null, "_id " + "DESC");
        List<BlackNumInfo> arrayList = new ArrayList<BlackNumInfo>();
        while (cursor.moveToNext()) {
            BlackNumInfo blackNumInfo = new BlackNumInfo();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            Log.d("Query", id + "---" + phone + "---" + mode);
            //todo 把上面删掉
            blackNumInfo.phone = phone;
            blackNumInfo.mode = mode;
            arrayList.add(blackNumInfo);
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    /**
     * 向表中插入数据
     *
     * @param phone 插入的号码
     * @param mode  黑名单模式 0:电话 1:短信 2:全部
     */
    public void insert(String phone, String mode) {
        SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        db.insert("blacknum", null, values);
        db.close();
    }

    /**
     * 删除数据
     *
     * @param phone 删除的号码
     */
    public void delete(String phone) {
        SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
        db.delete("blacknum", "phone=?", new String[]{phone});
        db.close();
    }

    /**
     * 修改数据
     *
     * @param phone 修改的号码
     * @param mode  黑名单模式 0:电话 1:短信 2:全部
     */
    public void update(String phone, String mode) {
        SQLiteDatabase db = dbOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        db.update("blacknum", values, "phone=?", new String[]{phone});
        db.close();
    }
}
