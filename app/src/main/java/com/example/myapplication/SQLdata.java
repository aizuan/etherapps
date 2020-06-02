package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class SQLdata extends SQLiteOpenHelper {
        private final static String DB="DB2019.db";
        private final static String TB="TB2019";
        private final static int VS=2;

    public SQLdata(Context context) {
        //super(context, name, factory, version);
        super(context, DB, null, VS);
    }

        @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL="CREATE TABLE IF NOT EXISTS "+TB+"(_id INTEGER,_title VARCHAR(50),_name INTEGER(20))";
        db.execSQL(" CREATE TABLE IF NOT EXISTS TAB (_id INTEGER,_title VARCHAR(50),_name INTEGER(20))");
        db.execSQL(SQL);

    }
    //
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String SQL="DROP TABLE "+TB;
        db.execSQL("DROP TABLE IF EXISTS TAB");
        db.execSQL(SQL);
    }
}