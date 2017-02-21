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

public class MeasuringHeartRate extends MyActivity {
    ImageView imageView;
    AnimationDrawable anim;
    boolean activityFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_heart_rate);
        setHeader("Measuring Heart Rate");
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.heart);
        TextView t1 = (TextView) findViewById(R.id.t1);

        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        anim = (AnimationDrawable) imageView.getBackground();
        imageView.setVisibility(View.VISIBLE);
        anim.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.stop();
                imageView.setVisibility(View.GONE);
                if (!activityFinished) {
                    startMyActivity(Result.class);
                    finish();
                }
            }
        }, 6000);

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
