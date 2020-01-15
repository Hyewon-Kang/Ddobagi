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

public class idSearch extends AppCompatActivity {
    EditText id_search;
    Button btn_search;
    String msql, fsql, id, select_id, select_tel, user_id, user_name;
    FriendRegisterOpenHelper fHelper;
    MySQLiteOpenHelper myHelper;
    SQLiteDatabase memberDB, friendDB;
    Cursor cursor;
    SimpleDateFormat date;
    Date d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_search);
        setTitle("id로 검색하기");

        Intent login = getIntent();
        user_id = login.getStringExtra("login_id");
        user_name = login.getStringExtra("user_name");

        id_search = findViewById(R.id.id_search);
        btn_search = findViewById(R.id.btn_search);
        date = new SimpleDateFormat("yyyy-MM-dd");

        d = new Date();

        id_search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //에디트 텍스트에 포커스 맞춰지면 초기화
        if (id_search.isFocused()==true){
            id_search.setText(null);
        }

        //NullPointerException 해결
        if(fHelper == null){
            fHelper = new FriendRegisterOpenHelper(idSearch.this, "friend", null,1);
        }
        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(idSearch.this,"member",null,1 );
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    id = id_search.getText().toString();
                    String sdate = date.format(d);


                    if(id.length()==0){
                        Toast.makeText(idSearch.this, "id를 입력하세요",Toast.LENGTH_LONG).show();
                        return;
                    }

                    memberDB = myHelper.getWritableDatabase();
                    friendDB = fHelper.getReadableDatabase();
                    msql = "select user_id,user_tel from member where user_id='" +
                            id +"'";
                    cursor = memberDB.rawQuery(msql,null);

                    //id와 tel 받아옴
                    while (cursor.moveToNext()){
                        select_id = cursor.getString(cursor.getColumnIndex("user_id"));
                        select_tel = cursor.getString(cursor.getColumnIndex("user_tel"));
                    }
                    memberDB.close();

                    if(!id.equals(select_id)){
                        Toast.makeText(getApplicationContext(), "존재하지 않는 id입니다.", Toast.LENGTH_LONG).show();
                        return;
                    }else if(id.equals(select_id)){
                        //friend Table에 값 집어넣기
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
                    Toast.makeText(getApplicationContext(), "id 검색에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        id_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    btn_search.performClick();
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent friendIntent = new Intent(getApplicationContext(), friendRegister.class);
        friendIntent.putExtra("login_id", user_id);
        friendIntent.putExtra("user_name", user_name);
        startActivity(friendIntent);
    }
}

