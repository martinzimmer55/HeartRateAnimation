package com.appxone.heartrateanimationapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;

import java.util.ArrayList;

public class Result extends MyActivity {
    HeartRate_Adapter adapter;
    ListView list;
    ArrayList<Model_HeartRate> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setHeader("Result");
        list = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayList.add(new Model_HeartRate("15th Feb, 2017, 02:12", "72 BMP"));
        arrayList.add(new Model_HeartRate("16th Feb, 2017, 06:24", "80 BMP"));
        arrayList.add(new Model_HeartRate("17th Feb, 2017, 09:59", "89 BMP"));
        arrayList.add(new Model_HeartRate("18th Feb, 2017, 01:32", "65 BMP"));
        arrayList.add(new Model_HeartRate("19th Feb, 2017, 12:45", "69 BMP"));
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
//        title.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_BEBAS));
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
