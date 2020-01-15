package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    Button login, register;
    EditText id, pw;
    String sql, u_id, u_pw, u_name;
    Integer ck = 0;
    Cursor cursor;
    MySQLiteOpenHelper myHelper;
    SQLiteDatabase sqlDB = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인하세요");

        boolean databaseCreated = false;
        boolean tableCreated = false;

        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        login = findViewById(R.id.login_main);
        register = findViewById(R.id.login_register);

        id.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        pw.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (myHelper == null) {
            myHelper = new MySQLiteOpenHelper(login.this, "member", null, 1);
        } //NullPointerException 해결

        //로그인 클릭 시
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(id.getText().toString(), pw.getText().toString());
            }
        });

        //회원가입 클릭
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), register.class);
                startActivity(registerIntent);
            }
        });

        pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    Click(id.getText().toString(),pw.getText().toString());
                }
                return false;
            }
        });
    }
    void Click(String id, String pw){
        try {
            String user_id = id;
            String user_pw = pw;


            sqlDB = myHelper.getWritableDatabase();
            sql = "select user_id, user_pass, user_name from member where user_id='" +
                    user_id + "'";
            cursor = sqlDB.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                u_id = cursor.getString(cursor.getColumnIndex("user_id"));
                u_pw = cursor.getString(cursor.getColumnIndex("user_pass"));
                u_name = cursor.getString(cursor.getColumnIndex("user_name"));
            }


            if (id.length() == 0 || pw.length() == 0) {
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
            } else if (user_id.equals(u_id) && user_pw.equals(u_pw)) {
                //메인 화면으로 이동
                Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                loginIntent.putExtra("login_id", u_id);
                loginIntent.putExtra("user_name", u_name);
                startActivity(loginIntent);

                Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_LONG).show();
            } else if (!user_id.equals(u_id) || !user_pw.equals(u_pw)) {
                Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호 오류입니다.", Toast.LENGTH_LONG).show();
            }

            sqlDB.close();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("login_id", u_id);
        mainIntent.putExtra("user_name", u_name);
        startActivity(mainIntent);
    }
}





