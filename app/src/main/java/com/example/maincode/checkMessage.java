package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class checkMessage extends AppCompatActivity {
    Button resend,list;
    TextView content_ck, in_id, title_ck;
    String title, user_id, user_name;
    messageOpenHelper mHelper;
    SQLiteDatabase m_DB = null;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_message);
        setTitle("쪽지 확인");

        resend = findViewById(R.id.resend);
        list = findViewById(R.id.list);
        content_ck = findViewById(R.id.content_ck);
        in_id = findViewById(R.id.in_id);
        title_ck = findViewById(R.id.title_ck);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        user_id = intent.getStringExtra("login_id");
        user_name = intent.getStringExtra("user_name");

        title_ck.setText(title);


        if(mHelper == null){
            mHelper = new messageOpenHelper(checkMessage.this, "message", null, 1);
        }

        try{
            m_DB = mHelper.getWritableDatabase();

            String sql = "select user_id, title, content from message where title='" + title +"'";
            cursor = m_DB.rawQuery(sql,null);
            while (cursor.moveToNext()){
                String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));

                in_id.setText(user_id);
                content_ck.setText(content);
            }
            m_DB.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"쪽지 로딩 실패 !",Toast.LENGTH_SHORT).show();
        }

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),message.class);
                intent.putExtra("login_id",user_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = in_id.getText().toString();
                Intent intent = new Intent(getApplicationContext(),writeMessage.class);
                intent.putExtra("login_id",user_id);
                intent.putExtra("user_name",user_name);
                intent.putExtra("to_id",id);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), message.class);
        mainIntent.putExtra("login_id", user_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }

}
