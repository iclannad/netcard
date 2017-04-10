package com.blink.blinkp2p.View.pop.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/7.
 */
public class LoginSQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "LoginData";
    public static final String ID = "_id";
    public static final String USER = "user";


    public LoginSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public LoginSQLHelper(Context cxt) {
        this(cxt, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "( "
                + ID + "  integer primary key,"
                + USER + " text not null unique"
                //+ RECEIVE + " varchar,"
                //+ TIME + " varchar,"
                //+ MSG + " varchar"
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
