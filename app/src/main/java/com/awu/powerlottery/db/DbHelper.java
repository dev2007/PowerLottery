package com.awu.powerlottery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by awu on 2015-11-02.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static String DB_DETAIL = "create table Detail(" +
            "id integer primary key autoincrement," +
            "type text," +
            "phase integer," +
            "result text," +
            "total integer," +
            "prizemoney integer)";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
