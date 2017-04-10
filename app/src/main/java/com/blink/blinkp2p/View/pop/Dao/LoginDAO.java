package com.blink.blinkp2p.View.pop.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7.
 */
public class LoginDAO {
    private static final String TAG = LoginDAO.class.getSimpleName();
    private LoginSQLHelper helper;
    private SQLiteDatabase sqlite;

    public LoginDAO(Context context) {
        this.helper = new LoginSQLHelper(context);
        sqlite = this.helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }


    public void add(String userId) {
        sqlite = this.helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LoginSQLHelper.USER, userId);
        long result = sqlite.insert(LoginSQLHelper.TABLE_NAME, null, cv);

        if (result != -1) {
            Log.e(TAG, "add: 插入成功");
        } else {
            Log.e(TAG, "add: 插入失败");
        }
        sqlite.close();
    }

    public void delete(String userId) {
        sqlite = this.helper.getWritableDatabase();
        int delete = sqlite.delete(LoginSQLHelper.TABLE_NAME, LoginSQLHelper.USER + "=?", new String[]{userId});
        sqlite.close();
        if (delete != 0) {
            Log.e(TAG, "delete: 删除成功");
        } else {
            Log.e(TAG, "delete: 删除失败");
        }
    }

    public ArrayList<String> QueryDataAll() {
        sqlite = this.helper.getWritableDatabase();
        String tablename = LoginSQLHelper.TABLE_NAME;
        String sql = "Select " + "*" + " From " + tablename;
        Log.d("run", sql);
        int k = 0;
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = sqlite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String res = "";
                int columnIndex = cursor.getColumnIndex(LoginSQLHelper.USER);
                res = cursor.getString(columnIndex);
                Log.e(TAG, "QueryDataAll2: cursor.getString(columnIndex)===" + res);

                list.add(res);
            } while (cursor.moveToNext());

            cursor.close();
            sqlite.close();
        }
        return list;
    }
}
