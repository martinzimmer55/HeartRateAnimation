package com.appxone.heartrateanimationapp;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MeasuringHeartRate extends MyActivity {
    ImageView imageView, initimage;
    AnimationDrawable anim;
    boolean activityFinished;
    int min = 55;
    int max = 100;
    CopyOfDataBaseManager db;
    long waitTime = 5000;   // waitTime for detecting heart value ... it is 5s =5000ms after that it will open Result screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_heart_rate);
        setHeader("Measuring Heart Rate");
        getSupportActionBar().setElevation(0.0f);
        db = new CopyOfDataBaseManager(getApplicationContext());
        imageView = (ImageView) findViewById(R.id.image);
        initimage = (ImageView) findViewById(R.id.initimage);
        imageView.setBackgroundResource(R.drawable.heart);
        TextView t1 = (TextView) findViewById(R.id.t1);
        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        anim = (AnimationDrawable) imageView.getBackground();


    }


    public void startAnim(View v) {
        initimage.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        anim.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.stop();
//                imageView.setVisibility(View.GONE);
                if (!activityFinished) {
                    Random r = new Random();
                    int heartrate = r.nextInt(max - min + 1) + min;
//                    As I here inserting random value of Heart rate ... so you need to insert your actual heart rate from device
                    db.insert_update("INSERT INTO records (heartrate, datetime) VALUES('" + String.valueOf(heartrate) + "','" + setCurrentDateTime() + "')");
                    startMyActivity(Result.class);
                    finish();
                }
            }

        }, waitTime);

    }

    public String setCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        return sdf.format(c.getTime());
    }

    public void back(View v) {
        finish();
    }

    public void setHeader(String header_title) {
        ActionBar mActionBar;
        TextView title;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_red, null);

        title = (TextView) mCustomView.findViewById(R.id.textTitle);
        ImageView backButton = (ImageView) mCustomView.findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        title.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        title.setText(header_title);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));
        //to display custom layout with same BG color
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        getSupportActionBar().setCustomView(mCustomView, layout);
    }

    @Override
    public void onTaskComplete(String result, String key) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        activityFinished = true;
    }
}
