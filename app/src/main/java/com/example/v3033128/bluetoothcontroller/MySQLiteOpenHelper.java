package com.example.v3033128.bluetoothcontroller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BTcon.db";
    public static final String TABLE = "button_table";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "create table " +TABLE+ "("+"_id integer primary key autoincrement not null,"+"name text,"+"width integer,"+"height integer,"+"x integer,"+"y integer,"+"send text);";

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
