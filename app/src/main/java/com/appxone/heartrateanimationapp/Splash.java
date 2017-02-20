package com.appxone.heartrateanimationapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;

public class Splash extends MyActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMyActivity(Home.class);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onTaskComplete(String result, String key) {

    }
}
