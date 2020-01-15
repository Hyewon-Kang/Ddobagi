package com.example.maincode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class emergencysetting extends AppCompatActivity {
Button addn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencysetting);
        setTitle("Emergency Situations Settings");

        addn = findViewById(R.id.addn);
        addn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),parentRegister.class);
                startActivity(intent);
            }
        });
    }
}
