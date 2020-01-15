package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class messageOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "FriendRegisterOpenHelper";
    private Context context;
    MySQLiteOpenHelper mHelper;

    public messageOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE message (number integer primary key, friend_id text, user_id text, title text, content text)";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists message;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }


    public void close() {
        mHelper.close();
    }
}