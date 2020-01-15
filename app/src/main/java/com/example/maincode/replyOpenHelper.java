package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class replyOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "replyOpenHelper";
    private Context context;
    MySQLiteOpenHelper rHelper;

    public replyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS replyList (number integer primary key,user_id text not null, title_text text not null, content text)";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists  replyList;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        rHelper.close();
    }
}
