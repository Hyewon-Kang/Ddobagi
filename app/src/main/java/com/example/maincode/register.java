package com.example.maincode;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    Button register, bt_check;
    EditText user_id, user_pw, user_pass_check, user_name, user_email, user_addr, user_tel;
    String u_id;
    int ck;
    RadioGroup user_sex;
    RadioButton rgid;
    MySQLiteOpenHelper myHelper;
    SQLiteDatabase sqlDB = null;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입하세요!");


        register = findViewById(R.id.register_add);
        bt_check = findViewById(R.id.bt_check);
        user_id = (EditText)findViewById(R.id.user_id);
        user_pw = (EditText)findViewById(R.id.user_pass);
        user_pass_check = (EditText)findViewById(R.id.user_pass_ck);
        user_name = (EditText)findViewById(R.id.user_name);
        user_email = (EditText)findViewById(R.id.user_email);
        user_addr = (EditText)findViewById(R.id.user_addr);
        user_tel = (EditText)findViewById(R.id.user_tel);
        user_sex = findViewById(R.id.user_sex);

        user_id.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_pw.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_pass_check.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_addr.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_tel.setImeOptions(EditorInfo.IME_ACTION_DONE);


        ck=0;

        if(myHelper == null){
            myHelper = new MySQLiteOpenHelper(register.this, "member", null,1);
        } //NullPointerException 해결

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = user_id.getText().toString();
                try{
                    sqlDB = myHelper.getWritableDatabase();
                    String sql = "select user_id from member where user_id='" +
                            id +"'";
                    cursor = sqlDB.rawQuery(sql,null);
                    while (cursor.moveToNext()){
                        u_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    }
                    if (id.equals(u_id)){
                        Toast.makeText(register.this, "중복된 ID입니다.", Toast.LENGTH_LONG).show();
                    } else if(!id.equals(u_id)){
                        Toast.makeText(getApplicationContext(), "사용할 수 있는 ID입니다.", Toast.LENGTH_LONG).show();
                        ck++;
                    }
                    sqlDB.close();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "중복확인에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String id = user_id.getText().toString();
                    String pw = user_pw.getText().toString();
                    String pw_ck = user_pass_check.getText().toString();
                    String name = user_name.getText().toString();
                    String email = user_email.getText().toString();
                    String addr = user_addr.getText().toString();
                    String tel = user_tel.getText().toString();
                    rgid = findViewById(user_sex.getCheckedRadioButtonId());
                    String sex = rgid.getText().toString();

                    sqlDB = myHelper.getWritableDatabase();

                    if(ck==0){
                        Toast.makeText(register.this, "중복확인을 해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    } else if(pw.equals(id)){
                        Toast.makeText(register.this, "아이디와 같은 비밀번호는 사용할 수 없습니다. ",Toast.LENGTH_LONG).show();
                        return;
                    } else if(!pw.equals(pw_ck)){
                        Toast.makeText(register.this, "비밀번호가 일치하지 않습니다. ",Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(email.equals("") || addr.equals("") || tel.equals("")){
                        Toast.makeText(register.this, "빈 항목이 있습니다.",Toast.LENGTH_LONG).show();
                        return;
                    } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(register.this, "올바른 이메일 형식으로 입력해 주세요.",Toast.LENGTH_LONG).show();
                        return;
                    } else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", tel)) {
                        Toast.makeText(register.this, "올바른 휴대폰 번호 형식으로 입력해 주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String sql = "INSERT INTO member VALUES('" + id +"','" + pw + "','"
                            + name + "','" + sex + "','"+ email + "','" + addr + "','" + tel +"')";
                    sqlDB.execSQL(sql);
                    sqlDB.close();

                    Intent registerIntent = new Intent(getApplicationContext(), login.class);
                    startActivity(registerIntent);

                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        user_tel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    register.performClick();
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
    }


}





