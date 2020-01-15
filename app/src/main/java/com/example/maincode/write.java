package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class write extends AppCompatActivity {
    public EditText title_text, content_text;
    TextView text_count;
    Button input, cancel;
    int i;
    public String login_id, user_name;
    talkBoardOpenHelper bHelper;
    SQLiteDatabase b_DB = null;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setTitle("게시판 글 쓰기");

        title_text = findViewById(R.id.title_text);
        content_text = findViewById(R.id.content);
        text_count = findViewById(R.id.text_count);
        input = findViewById(R.id.input);
        cancel = findViewById(R.id.cancel);

        Intent writeIntent = getIntent();
        login_id = writeIntent.getStringExtra("login_id");
        user_name = writeIntent.getStringExtra("user_name");

        Toast.makeText(getApplicationContext(), login_id+"님", Toast.LENGTH_SHORT).show();

        //DB NullPointException
        if(bHelper == null){
            bHelper = new talkBoardOpenHelper(write.this, "tboardList", null,1);
        }


        //글자수 카운트
        content_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                //텍스트 변화 있을 때 호출됨
                text_count.setText(s.length() + "/150자");
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //입력 끝났을 때 호출됨
                text_count.setText(s.length() + "/150자");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 전에 호출됨
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(content_text.length() > 150){
                        Toast.makeText(getApplicationContext(), "글은 150자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String title = title_text.getText().toString();
                    String content = content_text.getText().toString();


                    b_DB = bHelper.getWritableDatabase();

                    String select_sql ="select number from tboardList";
                    cursor = b_DB.rawQuery(select_sql, null);
                    while(cursor.moveToNext()){
                        i = cursor.getInt(cursor.getColumnIndex("number"));
                    }
                    i++;
                    String sql = "INSERT INTO tboardList VALUES('"+ i +"','"+ login_id +
                            "','" + title +
                            "','" + content +
                            "','" + 0 + "','" + 0 + //reply + reply_count
                            "')";
                    b_DB.execSQL(sql);
                    b_DB.close();


                    Intent writeIntent = new Intent(getApplicationContext(), talkBoard.class);
                    writeIntent.putExtra("login_id",login_id);
                    writeIntent.putExtra("user_name",user_name);
                    startActivity(writeIntent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"글쓰기 실패 !",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }


    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), talkBoard.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }
}
