package com.sat.mobilesafe.Database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Project:Working
 * Package:com.sat.mobilesafe.Database
 * Created by 姬岛朱乃 on 2017/10/25 at 20:05.
 */
@RunWith(AndroidJUnit4.class)
public class BlackNumDaoTest {
    @Test
    public void query() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumDao instance = BlackNumDao.getInstance(context);
        instance.insert("110","1");

    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

}