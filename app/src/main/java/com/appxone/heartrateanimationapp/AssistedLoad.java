package com.appxone.heartrateanimationapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

public class AssistedLoad extends MyActivity {
    String[] treatment_array;
    Spinner spinner_treatment;
    TextView incharge, spec1, spec2, spec3, startdate, enddate, treatfollow;
    ImageView start;
    LinearLayout treatment_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assisted_load);
        setHeader("Assisted Load");
        spinner_treatment = (Spinner) findViewById(R.id.spinner_treatment);
        start = (ImageView) findViewById(R.id.start);
        treatment_data = (LinearLayout) findViewById(R.id.treatment_data);
        treatment_array = getResources().getStringArray(R.array.treatment);
        ArrayAdapter_Territory adap = new ArrayAdapter_Territory(getApplicationContext(), treatment_array);
        spinner_treatment.setAdapter(adap);

        TextView t1 = (TextView) findViewById(R.id.t1);
        final TextView t3 = (TextView) findViewById(R.id.t3);
        TextView t4 = (TextView) findViewById(R.id.t4);
        TextView t5 = (TextView) findViewById(R.id.t5);
        TextView t6 = (TextView) findViewById(R.id.t6);
        TextView t7 = (TextView) findViewById(R.id.t7);
        TextView t8 = (TextView) findViewById(R.id.t8);
        TextView t9 = (TextView) findViewById(R.id.t9);
        TextView t10 = (TextView) findViewById(R.id.t10);

        incharge = (TextView) findViewById(R.id.incharge);
        spec1 = (TextView) findViewById(R.id.spec1);
        spec2 = (TextView) findViewById(R.id.spec2);
        spec3 = (TextView) findViewById(R.id.spec3);
        startdate = (TextView) findViewById(R.id.startdate);
        enddate = (TextView) findViewById(R.id.enddate);
        treatfollow = (TextView) findViewById(R.id.treatfollow);


        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t3.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t4.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t5.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t6.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t7.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t8.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t9.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t10.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));

        incharge.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        spec1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        spec2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        spec3.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        startdate.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        enddate.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        treatfollow.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        spinner_treatment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (treatment_array[i].equalsIgnoreCase("select treatment")) {
                    start.setBackgroundResource(R.drawable.assisted_start_grey);
                    treatment_data.setVisibility(View.INVISIBLE);
                    t3.setVisibility(View.INVISIBLE);
                } else {
                    start.setBackgroundResource(R.drawable.assisted_start_green);
                    t3.setVisibility(View.VISIBLE);
                    treatment_data.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void start(View v) {

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

    private class ArrayAdapter_Territory extends BaseAdapter {

        private LayoutInflater mInflater;
        String[] array;

        public ArrayAdapter_Territory(Context context, String[] array) {
            // TODO Auto-generated constructor stub
            mInflater = LayoutInflater.from(context);
            this.array = array;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(R.layout.dropdownselected, null);
                holder = new ListContent();

                holder.contact_text = (TextView) v.findViewById(R.id.tv_dropDownMenu);

                v.setTag(holder);
            } else {

                holder = (ListContent) v.getTag();
            }

            holder.contact_text.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
            holder.contact_text.setTextSize(15.0f);
            holder.contact_text.setText("" + array[position]);

            return v;
        }

    }


    static class ListContent {

        TextView contact_text;

    }

    @Override
    public void onTaskComplete(String result, String key) {

    }
}
