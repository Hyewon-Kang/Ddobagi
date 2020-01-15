package com.example.maincode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String login_id, user_name;
    Integer ck=0;
    TextView user_id;
    Button login, send_sms, check_location, test_btn;
    String[] permission_list = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Go home safely :)");

        //권한 확인
        checkPermission();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setImageResource(R.drawable.alarm);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //추가해도 소용x....
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //nav_header_main에 있는 요소들 찾아옴
        View headerView = navigationView.getHeaderView(0);
        login = (Button) headerView.findViewById(R.id.btn_login);
        user_id = (TextView) headerView.findViewById(R.id.userid);
        check_location = findViewById(R.id.check_location);
        test_btn = findViewById(R.id.test_btn);

        Intent ck_intent = getIntent();
        login_id = ck_intent.getStringExtra("login_id");
        user_name = ck_intent.getStringExtra("user_name");

        if(login_id==null){
            user_id.setText("로그인해주세요");
            login.setText("로그인");
            ck=0;
        } else{
            user_id.setText(user_name+"님");
            login.setText("로그아웃");
            ck=1;
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ck == 0) {
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                }
                else if(ck==1){
                    login_id=null;
                    ck=0;
                    user_id.setText("로그인해주세요");
                    login.setText("로그인");
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_id == null) {
                    Toast.makeText(MainActivity.this, "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(login_id != null){
                    Intent intent = new Intent(getApplicationContext(),streetlamp.class);
                    intent.putExtra("login_id",login_id);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);
                }
            }
        });

        try{
            check_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Map.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "지도를 띄우는 중입니다 !", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"지도 띄우기 실패!",Toast.LENGTH_SHORT).show();
        }
    }


    //'친구 찾기' 버튼 작동
    public void showBoard(View view){
        if(login_id == null){
            Toast.makeText(MainActivity.this, "로그인 해주세요 !", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "주변 친구를 찾으러 이동 중입니다 <3", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, talkBoard.class);
            intent.putExtra("login_id",login_id);
            intent.putExtra("user_name",user_name);
            startActivity(intent);
        }
    }

    //'위치 확인' 버튼 & 카카오 지도와 연결하는 부분
    public void showMap(android.view.View view) {
        Toast.makeText(this, "지도를 띄우는 중입니다 !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, Map.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_message){
            if (login_id ==null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(getApplicationContext(),message.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_manage) {
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(getApplicationContext(),record.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }

        } else if(id == R.id.nav_board){
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(getApplicationContext(),talkBoard.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }
        else if(id == R.id.nav_streetlamp){
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(getApplicationContext(), streetlamp.class);
                intent.putExtra("login_id", login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_share) {
            //채팅 (챗봇 ?)
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_xbpeAT/chat"));
            startActivity(i);

        } else if (id == R.id.nav_friend) {
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(getApplicationContext(),friendRegister.class);
                intent.putExtra("login_id",login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }else if(id == R.id.nav_parent){
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(getApplicationContext(), parentRegister.class);
                intent.putExtra("login_id", login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }else if(id == R.id.nav_send1) {
            //정보 수정
            if(login_id == null){
                Toast.makeText(getApplicationContext(), "로그인해주세요.", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(getApplicationContext(), check.class);
                intent.putExtra("login_id", login_id);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission: permission_list){
            int ck = checkCallingOrSelfPermission(permission);
            if(ck == PackageManager.PERMISSION_DENIED){
                requestPermissions(permission_list,0);
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0){
            for(int i=0; i<grantResults.length; i++){
                //허용됐다면
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){     }
                else {
                    Toast.makeText(getApplicationContext(), "앱 권한을 설정하세요.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
