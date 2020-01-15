package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class talkBoardOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "talkBoardOpenHelper";
    private Context context;
    MySQLiteOpenHelper bHelper;

    public talkBoardOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS tboardList (number integer primary key,user_id text not null, title_text text, content text, reply text, reply_count text)";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists  tboardList;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        bHelper.close();
    }
}