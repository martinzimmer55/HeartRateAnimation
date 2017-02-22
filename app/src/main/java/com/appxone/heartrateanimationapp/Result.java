package com.appxone.heartrateanimationapp;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import java.util.ArrayList;
import java.util.Collections;

public class Result extends MyActivity {
    HeartRate_Adapter adapter;
    ListView list;
    ArrayList<Model_HeartRate> arrayList;
    TextView heart_rate_value;
    CopyOfDataBaseManager db;
    String lastHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setHeader("Result");
        getSupportActionBar().setElevation(0.0f);
        db = new CopyOfDataBaseManager(getApplicationContext());
        list = (ListView) findViewById(R.id.listView);
        heart_rate_value = (TextView) findViewById(R.id.heart_rate_value);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);

        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        heart_rate_value.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_BOLD_COND));

        arrayList = new ArrayList<>();
        getDataFromDB();
        heart_rate_value.setText(lastHeartRate);
        adapter = new HeartRate_Adapter(Result.this, arrayList);
        list.setAdapter(adapter);
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

    public void getDataFromDB() {
        String q = "SELECT * FROM records";
        Cursor c = db.selectQuery(q);
        // for traversing
        arrayList.clear();
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("id"));
                    String heartrate = c.getString(c.getColumnIndex("heartrate"));
                    String datetime = c.getString(c.getColumnIndex("datetime"));
                    arrayList.add(new Model_HeartRate(id, datetime, heartrate));
                    lastHeartRate = heartrate;
                } while (c.moveToNext());
            }

        }
        Collections.reverse(arrayList);
    }

    @Override
    public void onTaskComplete(String result, String key) {

    }
}
