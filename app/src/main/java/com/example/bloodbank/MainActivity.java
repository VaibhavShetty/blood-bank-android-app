package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity{
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this,HomePageActivity.class));
                }
//                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 4000);
    }


}
