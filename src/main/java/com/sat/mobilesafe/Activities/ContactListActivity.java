package com.sat.mobilesafe.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sat.mobilesafe.R;
import com.sat.mobilesafe.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by overlord on 17-2-16.
 */
public class ContactListActivity extends AppCompatActivity{
    List<String> contactsList = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        initUI();
        initData();
    }

    private void initData() {
        readContacts();
    }

    private void initUI() {
        ListView lv_contact = (ListView) findViewById(R.id.lv_contact);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        lv_contact.setAdapter(adapter);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将信息返回去
                String info = contactsList.get(position);
                //ToastUtil.show(getApplicationContext(),info);
                Intent intent = new Intent();
                intent.putExtra("phone",info);
                setResult(0,intent);

                finish();
            }
        });
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null
            );
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(name+"\n"+phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor !=null){
                cursor.close();
            }
        }
    }
}
