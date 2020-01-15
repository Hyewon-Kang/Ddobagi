package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class check extends AppCompatActivity {
    String user_id, user_name, user_pw, id, pw, sql;
    EditText pw_ck;
    Button ok;
    MySQLiteOpenHelper myHelper;
    SQLiteDatabase sqlDB = null;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTitle("비밀번호 확인");

        Intent intent = getIntent();
        user_id = intent.getStringExtra("login_id");
        user_name = intent.getStringExtra("user_name");

        pw_ck = findViewById(R.id.pw_ck);
        ok = findViewById(R.id.btn_ok);

        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(check.this, "member", null,1);
        } //NullPointerException 해결

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    sqlDB = myHelper.getWritableDatabase();
                    sql = "select user_pass from member where user_id='" + user_id + "'";
                    cursor = sqlDB.rawQuery(sql, null);

                    while(cursor.moveToNext()){
                        pw = cursor.getString(cursor.getColumnIndex("user_pass"));
                    }
                    sqlDB.close();

                    if(!pw.equals(pw_ck.getText().toString())){
                        Toast.makeText(getApplicationContext(), "현재 비밀번호가 틀립니다. ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        Intent modifyIntent = new Intent(getApplicationContext(), modify.class);
                        modifyIntent.putExtra("login_id", user_id);
                        modifyIntent.putExtra("user_name",user_name);
                        startActivity(modifyIntent);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
