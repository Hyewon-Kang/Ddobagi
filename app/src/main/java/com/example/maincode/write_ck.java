package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class write_ck extends AppCompatActivity {
    TextView title_text_ck, write_user_id;
    EditText content_ck, reply;
    String user_id, user_name, title_text, sql, login_id, text, id, content, reply_content;
    int i, x;
    private static ListView mListView;
    Button rewrite, delete, reply_add;
    talkBoardOpenHelper bHelper;
    SQLiteDatabase b_DB = null;
    Cursor cursor = null;
    replyOpenHelper rHelper;
    SQLiteDatabase r_DB = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_ck);
        setTitle("주변 친구 찾기");

        title_text_ck = findViewById(R.id.title_text_ck);
        content_ck = findViewById(R.id.content_ck);
        write_user_id = findViewById(R.id.write_user_id);
        rewrite = findViewById(R.id.rewrite);
        delete = findViewById(R.id.delete);
        reply = findViewById(R.id.reply);
        reply_add = findViewById(R.id.reply_add);
        mListView = findViewById(R.id.listView);

        i = 0;

        Intent intent = getIntent();
        title_text = intent.getStringExtra("title_text");
        login_id = intent.getStringExtra("login_id");
        user_name = intent.getStringExtra("user_name");

        //DB NullPointException
        if (bHelper == null) {
            bHelper = new talkBoardOpenHelper(write_ck.this, "tboardList", null, 1);
        }
        if(rHelper == null){
            rHelper = new replyOpenHelper(write_ck.this, "replyList", null,1);
        }

        content_Update();
        reply_Update();

        String str = write_user_id.getText().toString();
        id = str.substring(9);

        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();


        if(id.equals(login_id)){
            rewrite.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        else{
            rewrite.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }

        //수정 이벤트
        rewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(i == 0){
                        content_ck.setEnabled(true);
                        i++;
                    }
                    else{
                        b_DB = bHelper.getWritableDatabase();
                        sql = "UPDATE tboardList SET content ='" + content_ck.getText().toString() +"' where title_text ='" +
                                title_text +"'";
                        b_DB.execSQL(sql);

                        content_Update();

                        i = 0;
                        content_ck.setEnabled(false);

                        Toast.makeText(getApplicationContext(),"수정 완료 !", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"수정 실패 !", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //삭제 이벤트
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    b_DB = bHelper.getWritableDatabase();
                    String delete_sql = "DELETE FROM tboardList where title_text='"+ title_text +"'";
                    b_DB.execSQL(delete_sql);
                    b_DB.close();

                    Intent intent = new Intent(getApplicationContext(), talkBoard.class);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),"삭제 완료 !", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "삭제 실패 !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reply_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String content = reply.getText().toString();

                    if(content.length() == 0){
                        Toast.makeText(getApplicationContext(),"댓글을 입력해주세요 !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    r_DB = rHelper.getWritableDatabase();
                    String select_sql ="select number from replyList";
                    cursor = r_DB.rawQuery(select_sql, null);

                    //number값 갖고옴
                    while(cursor.moveToNext()){
                        i = cursor.getInt(cursor.getColumnIndex("number"));
                    }
                    i++;

                    String insert_sql = "INSERT INTO replyList VALUES('" + i +
                            "','" + login_id + "','" + title_text + "','" + content + "')";
                    r_DB.execSQL(insert_sql);

                    String select_count = "select title_text from replyList where title_text ='" + title_text +"'";
                    x = cursor.getCount() +1;
                    r_DB.close();



                    b_DB = bHelper.getWritableDatabase();
                    String update_sql = "UPDATE tboardList SET reply_count ='" + x + "' where title_text='" + title_text + "'";
                    b_DB.execSQL(update_sql);
                    b_DB.close();

                    Toast.makeText(getApplicationContext(),"댓글 등록 완료 !", Toast.LENGTH_SHORT).show();

                    reply.setText("");
                    reply_Update();

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "댓글 등록 실패 ! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void content_Update(){
        try{
            //DB NullPointException
            if (bHelper == null) {
                bHelper = new talkBoardOpenHelper(write_ck.this, "tboardList", null, 1);
            }

            b_DB = bHelper.getWritableDatabase();
            String re_sql = "select user_id, title_text, content from tboardList where title_text='" + title_text + "'";
            cursor = b_DB.rawQuery(re_sql, null);

            while (cursor.moveToNext()) {
                id = cursor.getString(cursor.getColumnIndex("user_id"));
                text = cursor.getString(cursor.getColumnIndex("title_text"));
                content = cursor.getString(cursor.getColumnIndex("content"));
            }

            title_text_ck.setText(text);
            content_ck.setText(content);
            write_user_id.setText("작성자 ID : "+id);

            b_DB.close();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "내용 로딩 실패 !", Toast.LENGTH_SHORT).show();
        }
    }

    public void reply_Update(){
        if(rHelper == null){
            rHelper = new replyOpenHelper(write_ck.this, "replyList", null,1);
        }

        try{

            MyAdapter mMyAdapter = new MyAdapter();

            r_DB = rHelper.getWritableDatabase();
            String sql ="select user_id, content from replyList where title_text='" + title_text +"'";
            cursor = r_DB.rawQuery(sql,null);

            if(cursor.getCount() == 0){

            }else{
                while (cursor.moveToNext()){
                    String r_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    reply_content = cursor.getString(cursor.getColumnIndex("content"));


                    mMyAdapter.addItem(null, r_id, reply_content);
                }
            }
            r_DB.close();
            mListView.setAdapter(mMyAdapter);
            setListViewHeightBasedOnChildren(mListView);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"댓글 업데이트 실패 !", Toast.LENGTH_SHORT).show();
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        MyAdapter listAdapter = (MyAdapter) mListView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), talkBoard.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }
}