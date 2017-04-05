package blink.com.blinkcard320.Tool.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/5.
 */
public class MsgDAO {

    private SQLHelper helper;
    private SQLiteDatabase sqlite;
    public final static String SQLNAME = "sqlname";
    public final static String SQLKEY = "sqlkey";
    public final static String SQLTYPE = "sqltype";
    public final static String SQL_TABLENAME_USER = "User";


    public MsgDAO(Context context) {
        this.helper = new SQLHelper(context);
        sqlite = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public void InsertData(String tablename, String text, String name) {
        ContentValues cv = new ContentValues();
        cv.put(name, text);
        sqlite.insert(tablename, null, cv);
    }

    public void AddTableColumn(String tablename, List<Map<String, Object>> list) {

        try {
            ContentValues cv = new ContentValues();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get(SQLTYPE).equals("String")) {
                    cv.put(list.get(i).get(SQLNAME).toString(), list.get(i).get(SQLKEY).toString());
                    Log.d("run", list.get(i).get(SQLKEY).toString());
                }
                if (list.get(i).get(SQLTYPE).equals("int")) {
                    cv.put(list.get(i).get(SQLNAME).toString(), ((Integer) list.get(i).get(SQLKEY)).intValue());
                }
                if (list.get(i).get(SQLTYPE).equals("float")) {
                    cv.put(list.get(i).get(SQLNAME).toString(), Float.valueOf(list.get(i).get(SQLKEY).toString()));
                }
                if (list.get(i).get(SQLTYPE).equals("double")) {
                    cv.put(list.get(i).get(SQLNAME).toString(), Double.valueOf(list.get(i).get(SQLKEY).toString()));
                }

            }
            sqlite.insert(tablename, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void insertdb(String time, String send, String revice, String msg, String path) {
        ContentValues cv = new ContentValues();
        cv.put(SQLHelper.MSG, msg);
        cv.put(SQLHelper.TIME, time);
        cv.put(SQLHelper.Send, send);
        cv.put(SQLHelper.RECEIVE, revice);
        //  	 cv.put(SQLHelper.FILEPATH,path);
        sqlite.insert(SQLHelper.TABLE_NAME, null, cv);
    }

    public ArrayList<Map<Integer, Object>> QueryDataAll2(String tablename) {

        String sql = "Select " + "*" + " From " + tablename;
        Log.d("run", sql);
        int k = 0;
        ArrayList<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
        Cursor cursor = sqlite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String res = "";
                HashMap<Integer, Object> hashmap = new HashMap<Integer, Object>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    res = cursor.getString(i);
                    if (res == null) {
                        res = "emtity";
                    }
                    hashmap.put(i, res);
                    Log.d("run", "res==" + res);
                    k++;
                }
                list.add(hashmap);
            } while (cursor.moveToNext());
            return list;
        }
        return null;
    }

    public void DeleteAllData() {
        try {
            String sql = "delete from " + helper.TABLE_NAME;
            sqlite.execSQL(sql);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


}
