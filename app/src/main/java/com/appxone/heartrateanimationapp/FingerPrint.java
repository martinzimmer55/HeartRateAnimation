package com.appxone.heartrateanimationapp;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

public class FingerPrint extends MyActivity {
    TextView name, address, age, datetime, loc;
    ImageView scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        setHeader("Fingerprint Demo");
        getSupportActionBar().setElevation(0.0f);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);
        TextView t3 = (TextView) findViewById(R.id.t3);
        TextView t4 = (TextView) findViewById(R.id.t4);
        TextView t5 = (TextView) findViewById(R.id.t5);
        TextView t6 = (TextView) findViewById(R.id.t6);
        TextView t7 = (TextView) findViewById(R.id.t7);
        scan = (ImageView) findViewById(R.id.scan);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        age = (TextView) findViewById(R.id.age);
        datetime = (TextView) findViewById(R.id.datetime);
        loc = (TextView) findViewById(R.id.loc);
        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t3.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t4.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t5.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t6.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t7.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        name.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        address.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        age.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        datetime.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        loc.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));

//        Setting users image and text
        setImageAndTexts("Steven Smith", "Block A, Gulistan e Joha", "25 years", "20/02/2017 04:30pm", "Street University Road", R.drawable.fingerprint_demo_icon);

    }

    public void setImageAndTexts(String nameS, String addressS, String ageS, String datetimeS, String locS, Integer image) {
        name.setText(nameS);
        address.setText(addressS);
        age.setText(ageS);
        datetime.setText(datetimeS);
        loc.setText(locS);
        scan.setImageResource(image);

    }

    public void reset(View v) {

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
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
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
}

