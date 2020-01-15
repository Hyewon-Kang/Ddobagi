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
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class modify extends AppCompatActivity {
    Button btn_back, btn_modify;
    TextView text_id, wdraw;
    EditText pw, pw_ck, addr, tel, email, parent_name, parent_tel;
    String user_id, user_name, u_pw, user_pw, user_pw_ck, user_email, user_addr, user_tel, p_name, p_tel, sql;
    MySQLiteOpenHelper myHelper;
    ParentRegisterHelper pHelper;
    SQLiteDatabase sqlDB = null;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        setTitle("정보 수정");

        if(pHelper == null){
            pHelper = new ParentRegisterHelper(modify.this, "parent", null,1);
        }
        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(modify.this, "member", null,1);
        } //NullPointerException 해결

        Intent modifyIntent = getIntent();
        user_id = modifyIntent.getStringExtra("login_id");
        user_name = modifyIntent.getStringExtra("user_name");

        text_id = findViewById(R.id.text_id);
        wdraw = findViewById(R.id.wdraw);
        btn_back = findViewById(R.id.btn_back);
        btn_modify = findViewById(R.id.btn_modify);
        pw = findViewById(R.id.pw);
        pw_ck = findViewById(R.id.pw_ck);
        addr = findViewById(R.id.addr);
        tel = findViewById(R.id.tel);
        email = findViewById(R.id.email);
        parent_name = findViewById(R.id.parent_name);
        parent_tel = findViewById(R.id.parent_tel);

        text_id.setText(user_id + " (" + user_name + ") 님");


        try{
            String id = user_id;
            sqlDB = myHelper.getWritableDatabase();
            sql = "select user_pass, user_email, user_ad, user_tel from member where user_id='" +
                    id + "'";
            cursor = sqlDB.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                u_pw = cursor.getString(cursor.getColumnIndex("user_pass"));
                user_email = cursor.getString(cursor.getColumnIndex("user_email"));
                user_addr = cursor.getString(cursor.getColumnIndex("user_ad"));
                user_tel = cursor.getString(cursor.getColumnIndex("user_tel"));
            }

            addr.setText(user_addr);
            email.setText(user_email);
            tel.setText(user_tel);

            sqlDB.close();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "정보 로딩에 실패했습니다. ",Toast.LENGTH_SHORT).show();
        }

        try{
            String id = user_id;
            sqlDB = null;
            sqlDB = pHelper.getWritableDatabase();
            sql = "select parent_name, parent_number from parent where p_user_id='" +
                    id + "'";
            cursor = sqlDB.rawQuery(sql, null);

            while(cursor.moveToNext()){
                p_name = cursor.getString(cursor.getColumnIndex("parent_name"));
                p_tel = cursor.getString(cursor.getColumnIndex("parent_number"));
            }

            parent_name.setText(p_name);
            parent_tel.setText(p_tel);

            sqlDB.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "보호자 정보 로딩에 실패했습니다. ", Toast.LENGTH_SHORT).show();
        }

        try{
            btn_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = user_id;
                    user_pw = pw.getText().toString();
                    user_pw_ck = pw_ck.getText().toString();
                    user_email = email.getText().toString();
                    user_addr = addr.getText().toString();
                    user_tel = tel.getText().toString();
                    p_name = parent_name.getText().toString();
                    p_tel = parent_tel.getText().toString();

                    if(!user_pw.equals(user_pw_ck)){
                        Toast.makeText(getApplicationContext(), "수정하려는 비밀번호가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sqlDB = null;
                    sqlDB = myHelper.getWritableDatabase();
                    if(user_pw ==  null){
                        sql = "UPDATE member SET user_email='" + user_email +"', user_ad ='" + user_addr + "', user_tel='" + user_tel +"' WHERE user_id='" + id +"'";
                    }
                    else if(user_pw != null){
                        sql = "UPDATE member SET user_pass='" + user_pw +"', user_email='" + user_email +"', user_ad ='" + user_addr + "', user_tel='"
                                + user_tel +"' WHERE user_id='" + id +"'";
                    }
                    sqlDB.execSQL(sql);
                    sqlDB.close();

                    sqlDB = null;
                    sqlDB = pHelper.getWritableDatabase();
                    sql = "UPDATE parent SET parent_name='" + p_name +"', parent_number='" + p_tel+ "' WHERE p_user_id='" + id +"'";
                    sqlDB.execSQL(sql);
                    sqlDB.close();

                    Toast.makeText(getApplicationContext(),"수정이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "정보 수정에 실패했습니다. ", Toast.LENGTH_SHORT).show();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("login_id", user_id);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
            }
        });

        wdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wdIntent = new Intent(getApplicationContext(), withdraw.class);
                wdIntent.putExtra("login_id", user_id);
                wdIntent.putExtra("user_name", user_name);
                startActivity(wdIntent);
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("login_id", user_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }
}
