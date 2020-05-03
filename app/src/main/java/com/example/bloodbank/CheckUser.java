package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CheckUser extends AppCompatActivity {
Button up,in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        in = findViewById(R.id.in);
        up= findViewById(R.id.up);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckUser.this,LoginActivity.class);
                i.putExtra("status","signin");
                startActivity(i);
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckUser.this,LoginActivity.class);
                i.putExtra("status","signup");
                startActivity(i);
            }
        });
    }
}
