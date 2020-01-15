package com.example.maincode;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class record extends AppCompatActivity {
    private final static String TAG = "record";
    private final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice recorder";
    MediaRecorder recorder;
    String fname, login_id, user_name, sql, song_path;
    MediaPlayer player;
    boolean isRecording = false;
    boolean isPlaying = false;
    Button recordStart, recordPlaying;
    Chronometer timer;
    Integer i, currentPosition, x;
    recorderNumber rNumber;
    SQLiteDatabase r_db = null;
    Cursor cursor;
    ListView listView;
    List<String> items;
    ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("녹음하기");

        Intent login = getIntent();
        login_id = login.getStringExtra("login_id");
        user_name = login.getStringExtra("user_name");

        timer = findViewById(R.id.timer);
        listView = this.findViewById(R.id.recordList);

        x=1;

        recorder = new MediaRecorder();

        if(rNumber == null){
            rNumber = new recorderNumber(record.this, "recorderNumber", null,1);
        }

        recorderList();

        r_db = rNumber.getWritableDatabase();
        String select_sql ="select number from recorderNumber";
        cursor = r_db.rawQuery(select_sql, null);
        while(cursor.moveToNext()){
            i = cursor.getInt(cursor.getColumnIndex("number"));
        }
        r_db.close();


        recordStart = findViewById(R.id.recordStart);
        recordStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isRecording == false) {
                        initAudioRecorder();
                        timer.start();
                        recordStart.setText("녹음 중지");
                    }
                    else if(isRecording == true) {

                        stopRecorder();

                        timer.stop();
                        timer.setBase(SystemClock.elapsedRealtime());
                        i++;

                        r_db = rNumber.getWritableDatabase();
                        String update_q = "update recorderNumber set number ='" + i + "'";
                        r_db.execSQL(update_q);
                        r_db.close();

                        recorderList();

                        isRecording = false;
                        recordStart.setText("녹음 시작");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String file_name = listAdapter.getItem(i).toString();
                song_path = path + "/" + file_name;
                try{
                    if(x==1){
                        playSong(song_path);
                        x++;
                    }
                    else{
                        stopSong();
                        x=1;
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"재생 실패",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void initAudioRecorder(){
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            fname = path + "/record"+i+".m4a";
            Log.d(TAG, "file path is " + path);
            recorder.setOutputFile(fname);

            isRecording = true;
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void stopRecorder(){
        try{
            if(recorder != null){
                recorder.stop();
                recorder.release();
                recorder = null;
                isRecording = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void recorderList(){
        try {
            items = new ArrayList<String>();
            items.clear();

            listAdapter = new ArrayAdapter<String>(record.this,
                    android.R.layout.simple_list_item_1, items);

            File f = new File(path);
            File[] files = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase(Locale.US).endsWith(".m4a"); //확장자
                }
            });

            for(int i=0; i< files.length; i++){
                items.add(files[i].getName());
            }

            listView.setAdapter(listAdapter);

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    void playSong(String songPath){
        try{
            player = new MediaPlayer();
            player.reset();
            player.setDataSource(songPath);
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 : " + songPath, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "재생안됨", Toast.LENGTH_SHORT).show();
        }
    }

    void stopSong(){
        try{
            player.stop();
            player.release();
            player = null;

            currentPosition =0;
        } catch (Exception e){
            e.printStackTrace();
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
