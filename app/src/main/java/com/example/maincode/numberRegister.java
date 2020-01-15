package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class numberRegister extends AppCompatActivity {
    EditText f_number;
    Button f_add;
    String name, number, select_id, select_tel, sql, user_id, fsql, user_name;
    MySQLiteOpenHelper myHelper;
    FriendRegisterOpenHelper fHelper;
    SQLiteDatabase memberDB, friendDB;
    Cursor cursor;
    SimpleDateFormat date;
    Date d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_register);
        setTitle("전화번호로 등록하기");

        Intent login = getIntent();
        user_id = login.getStringExtra("login_id");
        user_name = login.getStringExtra("user_name");

        f_number = findViewById(R.id.f_number);
        f_add = findViewById(R.id.f_add);

        d = new Date();

        //에디트 텍스트에 포커스 맞춰지면 초기화
        if (f_number.isFocused()==true){
            f_number.setText(null);
        }

        f_number.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //NullPointerException 해결
        if(fHelper == null){
            fHelper = new FriendRegisterOpenHelper(numberRegister.this, "friend", null,1);
        }
        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(numberRegister.this,"member",null,1 );
        }

        f_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    number = f_number.getText().toString();
                    date = new SimpleDateFormat("yyyyMMdd");
                    String sdate = date.format(d);


                    if(number.length() ==0){
                        Toast.makeText(numberRegister.this, "번호를 입력하세요",Toast.LENGTH_LONG).show();
                        return;
                    }

                    memberDB = myHelper.getWritableDatabase();
                    friendDB = fHelper.getWritableDatabase();
                    sql = "select user_id, user_tel from member where user_tel='" +
                            number +"'";
                    cursor = memberDB.rawQuery(sql,null);

                    while (cursor.moveToNext()){
                        select_id = cursor.getString(cursor.getColumnIndex("user_id"));
                        select_tel = cursor.getString(cursor.getColumnIndex("user_tel"));
                    }
                    memberDB.close();


                    if(!number.equals(select_tel)){
                        Toast.makeText(getApplicationContext(), "등록되지 않은 번호입니다.", Toast.LENGTH_LONG).show();
                        return;
                    } else if(number.equals(select_tel)){
                        fsql = "insert into friend values('" +
                                user_id+"','" + select_id + "','" +sdate+ "','null','null','"+ select_tel+
                                "')";
                        friendDB.execSQL(fsql);
                        friendDB.close();

                        Intent friendIntent = new Intent(getApplicationContext(), friendRegister.class);
                        friendIntent.putExtra("login_id",user_id);
                        friendIntent.putExtra("user_name",user_name);
                        startActivity(friendIntent);
                        Toast.makeText(getApplicationContext(), "친구 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "번호 검색에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        f_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    f_add.performClick();
                }
                return false;
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

