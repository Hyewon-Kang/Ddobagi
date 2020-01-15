package com.example.maincode;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class withdraw extends AppCompatActivity {

    Button ok, back;
    String sql, login_id, user_name;
    MySQLiteOpenHelper myHelper;
    FriendRegisterOpenHelper fHelper;
    ParentRegisterHelper pHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        setTitle("탈퇴하실건가요...?");

        ok = findViewById(R.id.withdraw);
        back = findViewById(R.id.back);

        Intent mainIntent = getIntent();
        login_id = mainIntent.getStringExtra("login_id");
        user_name = mainIntent.getStringExtra("user_name");

        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(withdraw.this, "member", null,1);
        }
        if(fHelper == null){
            fHelper = new FriendRegisterOpenHelper(withdraw.this, "friend", null,1);
        }
        if(pHelper == null){
            pHelper = new ParentRegisterHelper(withdraw.this, "parent", null,1);
        }//NullPointerException 해결


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db = myHelper.getWritableDatabase();
                    sql = "delete from member where user_id='" + login_id +"'";
                    db.execSQL(sql);
                    db.close();

                    db = fHelper.getWritableDatabase();
                    sql = "delete from friend where user_id='" + login_id +"' or friend_id='" + login_id +"'";
                    db.execSQL(sql);
                    db.close();

                    db = pHelper.getWritableDatabase();
                    sql = "delete from parent where p_user_id='" + login_id + "'";
                    db.execSQL(sql);
                    db.close();

                    db.close();

                    login_id = null;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("login_id", login_id);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "탈퇴되었습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


    }
}
