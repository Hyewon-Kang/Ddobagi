package com.example.maincode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class friendRegister extends AppCompatActivity {
    ImageView phonebook, idRegister;
    ListView listView;
    String login_id, sql, f_id, user_name, id;
    FriendRegisterOpenHelper fHelper;
    SQLiteDatabase f_DB = null;
    Cursor cursor;

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_register);
        setTitle("친구를 등록해주세요!");

        Intent login = getIntent();
        login_id = login.getStringExtra("login_id");
        user_name = login.getStringExtra("user_name");

        listView = this.findViewById(R.id.listView);

        ArrayList<String> items = new ArrayList<>();
        items.clear();


        //NullPointException 해결
        if(fHelper == null){
            fHelper = new FriendRegisterOpenHelper(friendRegister.this, "friend", null,1);
        }

        try{
            f_DB = fHelper.getWritableDatabase();
            sql = "select friend_id from friend where user_id='" + login_id +"'";
            cursor = f_DB.rawQuery(sql, null);
            if(cursor.getCount() == 0){
                Toast.makeText(getApplicationContext(),"친구를 추가해주세요",Toast.LENGTH_LONG).show();
            }
            else{
                while(cursor.moveToNext()){
                    f_id = cursor.getString(cursor.getColumnIndex("friend_id"));
                    items.add(f_id);
                }
            }

            f_DB.close();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "친구목록 로딩에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }



        phonebook = findViewById(R.id.phonebook);
        phonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), numberRegister.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });

        idRegister = findViewById(R.id.idRegister);
        idRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), idSearch.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        });


        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    id = (String)listView.getItemAtPosition(i);

                    AlertDialog.Builder builder = new AlertDialog.Builder(friendRegister.this);
                    builder.setTitle("쪽지 보내기").setMessage(id+"님께 쪽지를 보내시겠습니까?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), writeMessage.class);
                            intent.putExtra("login_id", login_id);
                            intent.putExtra("user_name",user_name);
                            intent.putExtra("to_id",id);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"쪽지 보내기 실패 !",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_item, null);
            }

            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.user);
            TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(items.get(position));

            return v;

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
