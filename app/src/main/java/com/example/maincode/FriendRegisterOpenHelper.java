package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendRegisterOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "FriendRegisterOpenHelper";
    private Context context;
    MySQLiteOpenHelper fHelper;

    public FriendRegisterOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE friend (user_id text, friend_id text, friend_add_date date, friend_accept date, cancle text, friend_tel text," +
                "primary key(user_id, friend_id))";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists friend;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        fHelper.close();
    }
}


