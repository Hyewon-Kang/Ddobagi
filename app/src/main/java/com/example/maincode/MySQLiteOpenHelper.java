package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "MySQLiteOpenHelper";
    private Context context;
    MySQLiteOpenHelper myHelper;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE member (user_id text primary key, user_pass text,user_name text,user_sex text,user_email text,user_ad text, user_tel text)";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists member;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        myHelper.close();
    }
}


