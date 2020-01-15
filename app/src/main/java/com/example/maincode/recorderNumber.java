package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class recorderNumber extends SQLiteOpenHelper {
    private final String TAG = "recorderNumber";
    private Context context;
    MySQLiteOpenHelper Number;

    public recorderNumber(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE recorderNumber (number integer primary key)";
        sqLiteDatabase.execSQL(sql);

        String re = "insert into recorderNumber values('" + 1 +"')";
        sqLiteDatabase.execSQL(re);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists recorderNumber;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        Number.close();
    }
}


