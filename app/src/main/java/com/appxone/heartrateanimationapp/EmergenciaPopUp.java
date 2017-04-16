package com.appxone.heartrateanimationapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.FrameUtils.StringUtils;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONArray;

/**
 * Created by goliath on 19/03/2017.
 */

public class EmergenciaPopUp  extends MyActivity {
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_popup);
        setHeader("Alerta recibida");
        getSupportActionBar().setElevation(0.0f);
        TextView t2 = (TextView) findViewById(R.id.t2);
        t2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        TextView t3 = (TextView) findViewById(R.id.t3);
        t3.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        TextView t4 = (TextView) findViewById(R.id.t4);
        t4.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        TextView t5 = (TextView) findViewById(R.id.t5);
        t5.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        TextView t6 = (TextView) findViewById(R.id.t6);
        t6.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        TextView name = (TextView) findViewById(R.id.emName);
        TextView date = (TextView) findViewById(R.id.emDate);
        TextView address = (TextView) findViewById(R.id.emAddress);

        Intent intent = this.getIntent();
        String n = intent.getStringExtra("nombre");
        String f = intent.getStringExtra("fecha");
        String l = intent.getStringExtra("lugar");
        name.setText(n);
        date.setText(f);
        address.setText(l);
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(4000);
        mp = MediaPlayer.create(EmergenciaPopUp.this, R.raw.alarma);
        mp.start();
    }
    public void reset(View v) {
        Intent intent = new Intent(EmergenciaPopUp.this, BraceletEmergency.class);
        startActivity(intent);
    }

    public void back(View v) {
        Intent intent = new Intent(EmergenciaPopUp.this, BraceletDemo.class);
        finish();
        startActivity(intent);
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
