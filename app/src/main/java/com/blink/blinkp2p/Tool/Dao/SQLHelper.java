package com.blink.blinkp2p.Tool.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/5.
 */
public class SQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "FileData";
    public static final String ID = "_id";
    public static final String Send = "sender";
    public static final String RECEIVE = "receiver";
    public static final String MSG = "msg";
    public static final String TIME = "time";
    public static final String FILEPATH = "filepath";
    public static final int I_SEND = 1;
    public static final int I_RECEIVE = 2;
    public static final int I_MSG = 4;
    public static final int I_TIME = 3;
    public static final int I_FILEPATH = 5;

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLHelper(Context cxt) {
        this(cxt, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "( "
                + ID + "  integer primary key,"
                + Send + " varchar,"
                + RECEIVE + " varchar,"
                + TIME + " varchar,"
                + MSG + " varchar"
                //    +FILEPATH+" varchar"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}
