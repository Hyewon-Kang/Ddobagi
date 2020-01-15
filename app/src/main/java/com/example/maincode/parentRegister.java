package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class parentRegister extends AppCompatActivity {

    EditText parent_name, parent_phone;
    Button parent_add;
    String login_id, select_sql, sql, p_name, p_phone, select_phone, user_name, name, phone;
    Integer i =0;
    TextView parent_title, ss_parent_name, ss_parent_number;
    ParentRegisterHelper pHelper;
    SQLiteDatabase db = null;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        setTitle("보호자를 등록 해주세요");

        parent_name = findViewById(R.id.parent_name);
        parent_phone = findViewById(R.id.parent_phone);
        parent_add = findViewById(R.id.parent_add);
        parent_title = findViewById(R.id.title_parent);
        ss_parent_name = findViewById(R.id.ss_parnet_name);
        ss_parent_number = findViewById(R.id.ss_parent_number);


        if(pHelper == null){
            pHelper = new ParentRegisterHelper(parentRegister.this, "parent", null,1);
        } //NullPointerException 해결


        Intent p_intent = getIntent();
        login_id = p_intent.getStringExtra("login_id");
        user_name = p_intent.getStringExtra("user_name");

        parent_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        parent_phone.setImeOptions(EditorInfo.IME_ACTION_DONE);

        try{
            db = pHelper.getWritableDatabase();
            String parnet_sql = "select parent_name, parent_number from parent where p_user_id ='"+login_id+"'";
            cursor = db.rawQuery(parnet_sql,null);
            while (cursor.moveToNext()) {
                p_name = cursor.getString(cursor.getColumnIndex("parent_name"));
                p_phone = cursor.getString(cursor.getColumnIndex("parent_number"));
            }

            if (p_phone != null) {
                parent_title.setVisibility(View.VISIBLE);
                ss_parent_number.setVisibility(View.VISIBLE);
                ss_parent_name.setVisibility(View.VISIBLE);

                ss_parent_name.setText("이름 : " + p_name);
                ss_parent_number.setText("번호 : " + p_phone);
                parent_add.setText("수정");
                i++;
            }
        } catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"보호자 번호를 등록해주세요 !", Toast.LENGTH_SHORT).show();
        }



        parent_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    p_name = parent_name.getText().toString();
                    p_phone = parent_phone.getText().toString();
                    if(i == 0){

                        if(p_name.length()==0){
                            Toast.makeText(parentRegister.this, "이름를 입력하세요",Toast.LENGTH_LONG).show();
                            return;
                        } else if(p_phone.length() ==0){
                            Toast.makeText(parentRegister.this, "번호를 입력하세요",Toast.LENGTH_LONG).show();
                            return;
                        }


                        db = pHelper.getWritableDatabase();

                        select_sql ="select parent_number from parent where parent_number='" + p_phone +"'";
                        cursor = db.rawQuery(select_sql, null);
                        while(cursor.moveToNext()){
                            select_phone = cursor.getString(cursor.getColumnIndex("parent_number"));
                        }

                        if(select_phone != null){
                            Toast.makeText(parentRegister.this, "이미 등록된 번호입니다.", Toast.LENGTH_LONG).show();
                            select_phone = null;
                            return;
                        }

                        sql = "insert into parent values('" + login_id + "','" + p_name + "','" + p_phone +"')";
                        db.execSQL(sql);
                        db.close();

                        parent_title.setVisibility(View.VISIBLE);
                        ss_parent_number.setVisibility(View.VISIBLE);
                        ss_parent_name.setVisibility(View.VISIBLE);

                        ss_parent_name.setText("이름 : " + p_name);
                        ss_parent_number.setText("번호 : " + p_phone);

                        parent_add.setText("수정");
                        i++;
                        Toast.makeText(getApplicationContext(), "보호자 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    }
                    else{
                        if(p_name.length()==0){
                            Toast.makeText(parentRegister.this, "수정할 이름를 입력하세요",Toast.LENGTH_LONG).show();
                            return;
                        } else if(p_phone.length() ==0){
                            Toast.makeText(parentRegister.this, "수정할 번호를 입력하세요",Toast.LENGTH_LONG).show();
                            return;
                        }


                        db = pHelper.getWritableDatabase();
                        String update_sql ="UPDATE parent SET parent_name='" + p_name +"', parent_number='" + p_phone +"' WHERE p_user_id ='" + login_id + "'";
                        cursor = db.rawQuery(update_sql,null);

                        ss_parent_name.setText("이름 : " + p_name);
                        ss_parent_number.setText("번호 : " + p_phone);

                        db.close();

                        Toast.makeText(getApplicationContext(),"수정이 완료되었습니다 !", Toast.LENGTH_SHORT).show();



                    }


                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "번호 등록에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        parent_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    parent_add.performClick();
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("login_id", login_id);
        mainIntent.putExtra("user_name", user_name);
        startActivity(mainIntent);
    }

}
