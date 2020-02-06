package com.example.a5einitiatetracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler myHandler = new Handler();
    final Runnable r = new Runnable() {
        @Override
        public void run() {
            gotoMainActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myHandler.postDelayed(r, 1000);

    }

    private void gotoMainActivity(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
