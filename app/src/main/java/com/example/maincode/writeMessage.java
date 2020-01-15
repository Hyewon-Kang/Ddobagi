package com.example.maincode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class writeMessage extends AppCompatActivity {
    Button send,cancel, search;
    EditText content,title, to_id;
    TextView text_count;
    String user_name, login_id, user_id;
    ArrayList<String> ids;
    ArrayAdapter myAdapter;
    ListView listview;
    int i;
    messageOpenHelper mHelper;
    MySQLiteOpenHelper myHelper;
    FriendRegisterOpenHelper fHelper;
    SQLiteDatabase f_DB = null;
    SQLiteDatabase m_DB = null;
    Cursor cursor;
    Context context;
    final int[] selectedItem = {0};
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);
        setTitle("메시지 작성");

        send = findViewById(R.id.send);
        cancel = findViewById(R.id.cancel);
        content = findViewById(R.id.content);
        to_id = findViewById(R.id.to_id);
        search = findViewById(R.id.search);
        title = findViewById(R.id.title);
        text_count = findViewById(R.id.text_count);

        Intent intent = getIntent();
        login_id = intent.getStringExtra("login_id");
        user_name = intent.getStringExtra("user_name");
        to_id.setText(intent.getStringExtra("to_id"));

        if(mHelper == null){
            mHelper = new messageOpenHelper(writeMessage.this, "message", null, 1);
        }


        //글자수 카운트
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                //텍스트 변화 있을 때 호출됨
                text_count.setText(s.length() + "/200자");
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //입력 끝났을 때 호출됨
                text_count.setText(s.length() + "/200자");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 전에 호출됨
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(myHelper == null){
                        myHelper = new MySQLiteOpenHelper(writeMessage.this, "member", null, 1);
                    }
                    if(fHelper == null){
                        fHelper = new FriendRegisterOpenHelper(writeMessage.this, "friend",null,1);
                    }

                    myAdapter =new ArrayAdapter(writeMessage.this, android.R.layout.select_dialog_singlechoice);

                    String f_id = to_id.getText().toString();
                    //m_DB = myHelper.getWritableDatabase();
                    f_DB = fHelper.getWritableDatabase();
                    if(f_id.equals("")){

//                        String sql = "select user_id from member";
//                        cursor = m_DB.rawQuery(sql,null);

                        String sql = "select friend_id from friend where user_id ='"+ login_id +"'";
                        cursor = f_DB.rawQuery(sql,null);
                        if(cursor.getCount() >1){
                            items = new String[cursor.getCount()];
                        }
                        else if(cursor.getCount() == 0){
                            Toast.makeText(getApplicationContext(),"친구를 등록해주세요 !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        while(cursor.moveToNext()){
                            myAdapter.add(cursor.getString(cursor.getColumnIndex("friend_id")));
                        }
//                        listView.setAdapter(myAdapter);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(writeMessage.this);
                        dialog.setTitle("아이디를 선택하세요")
                                .setAdapter(myAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        user_id = (String)myAdapter.getItem(i);
                                        to_id.setText(user_id);
                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        to_id.setText(user_id);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"취소되었습니다. ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                ;
                        dialog.create();
                        dialog.show();
                    }
                    else{
                        String sql = "select friend_id from friend where friend_id ='" + f_id + "'";
                        cursor = m_DB.rawQuery(sql,null);
                        if(cursor.getCount()==0){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(writeMessage.this);
                            dialog.setMessage("검색하신 아이디가 없습니다!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    to_id.setText("");
                                }
                            });
                            AlertDialog Ddialog = dialog.create();
                            Ddialog.show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"검색하신 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    m_DB.close();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"검색 실패!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(mHelper == null){
                        mHelper = new messageOpenHelper(writeMessage.this, "message", null, 1);
                    }

                    String in_content = content.getText().toString();
                    String in_title = title.getText().toString();
                    String friend_id = to_id.getText().toString();

                    m_DB = mHelper.getWritableDatabase();
                    String select = "select number from message";
                    cursor = m_DB.rawQuery(select,null);
                    if(cursor.getCount() == 0){
                        i=1;
                    } else{
                        while (cursor.moveToNext()){
                            i = cursor.getInt(cursor.getColumnIndex("number"));
                            i++;
                        }
                    }


                    String sql ="INSERT INTO message VALUES('" + i + "','" + friend_id + "','" + login_id + "','" + in_title + "','"+ in_content+ "')";
                    m_DB.execSQL(sql);
                    m_DB.close();

                    Intent intent = new Intent(getApplicationContext(), message.class);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),"쪽지 전송 완료!", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"쪽지 전송 실패 !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), message.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), message.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }
}
