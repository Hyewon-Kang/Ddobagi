package com.example.maincode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ParentRegisterHelper extends SQLiteOpenHelper {
    private final String TAG = "ParnetRegisterHelper";
    private Context context;
    MySQLiteOpenHelper pHelper;

    public ParentRegisterHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE parent(p_user_id text, parent_name text, parent_number text," +
                "primary key(p_user_id)," +
                "FOREIGN KEY(p_user_id) REFERENCES member (user_id)) ";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "drop table if exists parent;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public void close() {
        pHelper.close();
    }
}

