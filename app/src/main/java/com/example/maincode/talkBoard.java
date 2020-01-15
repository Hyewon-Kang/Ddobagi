package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class talkBoard extends AppCompatActivity {
    ImageView edit;
    String login_id, user_name, title_text, content, sql, b_login_id;
    int i = 0;
    talkBoardOpenHelper bHelper;
    SQLiteDatabase b_DB = null;
    Cursor cursor = null;
    ListView listView;
    List<String> items;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        setTitle("주변 친구 찾기");

        /* 위젯과 멤버변수 참조 획득 */
        listView = (ListView)findViewById(R.id.listView);
        edit = findViewById(R.id.edit);

        list_Update();

        //DB NullPointException
        if(bHelper == null){
            bHelper = new talkBoardOpenHelper(talkBoard.this, "tboardList", null,1);
        }

        Intent board =  getIntent();
        login_id = board.getStringExtra("login_id");
        user_name = board.getStringExtra("user_name");

        list_Update();

        try{
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), write.class);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "페이지 이동 실패", Toast.LENGTH_SHORT).show();
        }

        try{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String text = listAdapter.getItem(i);
                    String title_text = text.substring(0, text.length()-10);

                    Intent intent = new Intent(getApplicationContext(), write_ck.class);
                    intent.putExtra("title_text",title_text);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "실패,,,!",Toast.LENGTH_SHORT).show();
        }


    }   //end onCreate


    void list_Update(){
        try{

            items = new ArrayList<String>();
            items.clear();
            listAdapter = new ArrayAdapter<String>(talkBoard.this, android.R.layout.simple_list_item_1, items);

            String list_in_id, list_in_title, reply_count;

            if(bHelper == null){
                bHelper = new talkBoardOpenHelper(talkBoard.this, "tboardList", null,1);
            }

            b_DB = bHelper.getWritableDatabase();
            String select_sql = "select user_id, title_text, reply_count from tboardList";
            cursor = b_DB.rawQuery(select_sql,null);
            if(cursor.getCount() ==0){
                Toast.makeText(getApplicationContext(),"게시물을 등록해주세요 ! ", Toast.LENGTH_SHORT).show();
            }
            else{
                while(cursor.moveToNext()){
                    list_in_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    list_in_title = cursor.getString(cursor.getColumnIndex("title_text"));
                    reply_count = cursor.getString(cursor.getColumnIndex("reply_count"));

                    list_in_title += "       [" + reply_count + "]";

                    items.add(list_in_title);
                }
            }
            b_DB.close();

            /* 리스트뷰에 어댑터 등록 */
            listView.setAdapter(listAdapter);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "List Update Fail", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View v){

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }


}