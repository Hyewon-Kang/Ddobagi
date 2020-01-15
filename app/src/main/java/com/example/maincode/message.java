package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class message extends AppCompatActivity {
    String login_id, user_name, title;
    ImageView write;
    ListView listView;
    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> item;
    messageOpenHelper mHelper;
    SQLiteDatabase m_DB = null;

    private TextView selected_item_textview;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("쪽지 보관함");

        if(mHelper == null){
            mHelper = new messageOpenHelper(message.this, "message", null, 1);
        }

        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        login_id = intent.getStringExtra("login_id");
        user_name = intent.getStringExtra("user_name");

        items = new ArrayList<HashMap<String, String>>();

        message_Update();

        write = findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),writeMessage.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = (HashMap)listView.getItemAtPosition(i);
                String title = (String)item.get("item1");

                Intent intent = new Intent(getApplicationContext(), checkMessage.class);
                intent.putExtra("login_id", login_id);
                intent.putExtra("user_name",user_name);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }

    void message_Update(){
        try{

            String user_id, title;

            if(mHelper == null){
                mHelper = new messageOpenHelper(message.this, "message", null,1);
            }

//            messageAdapter mAdapter = new messageAdapter();


            m_DB = mHelper.getReadableDatabase();
            String select_sql = "select user_id, title from message where friend_id ='" + login_id +"'"; //user_id가 보낸 사람, friend_id가 받은 사람
            Cursor cursor = m_DB.rawQuery(select_sql,null);
            if(cursor.getCount() ==0){
                Toast.makeText(getApplicationContext(),"받은 쪽지가 없습니다 ! ", Toast.LENGTH_SHORT).show();
            }
            else{
                while(cursor.moveToNext()){
                    user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    title = cursor.getString(cursor.getColumnIndex("title"));

                    item = new HashMap<String, String>();
                    item.put("item1", title);
                    item.put("item2",user_id);
                    items.add(item);
                }
            }
            SimpleAdapter mAdapter = new SimpleAdapter(getApplicationContext(),items, android.R.layout.simple_list_item_2,
                    new String[]{"item1","item2"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(mAdapter);
            m_DB.close();


        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "List Update Fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }
}
