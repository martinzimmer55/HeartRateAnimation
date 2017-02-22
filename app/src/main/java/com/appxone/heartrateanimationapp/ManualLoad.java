package com.appxone.heartrateanimationapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;
import com.appxone.heartrateanimationapp.Utils.PSStrings;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ManualLoad extends MyActivity implements OnChartValueSelectedListener {
    private PieChart mChart;
    //    protected String[] mParties = new String[] {
//            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
//            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
//            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
//            "Party Y", "Party Z"
//    };
    protected String[] mParties = new String[]{
            "", "", "", "", "", "", "", "",
            "", ""
    };
    int selectedChartItem;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener timeO;
    TextView day, time, t1;
    boolean isDateSelected, isTimeSelected, isChartSelected;
    ImageView ready;
    HashMap<Float, Integer> myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_load);
        setHeader("Manual Load");
        getSupportActionBar().setElevation(0.0f);
        mChart = (PieChart) findViewById(R.id.chart);
        day = (TextView) findViewById(R.id.day);
        time = (TextView) findViewById(R.id.time);
        ready = (ImageView) findViewById(R.id.ready);
        t1 = (TextView) findViewById(R.id.t1);

        day.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        time.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterTextTypeface(mTfLight);
//        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

//         mChart.setUnit(" €");
//         mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(10, 100);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();

            }

        };
        timeO = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabelTime();

            }
        };
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        myMap = new HashMap<Float, Integer>();
        myMap.put(0.0f, 10);
        myMap.put(1.0f, 20);
        myMap.put(2.0f, 30);
        myMap.put(3.0f, 40);
        myMap.put(4.0f, 50);
        myMap.put(5.0f, 60);
        myMap.put(6.0f, 70);
        myMap.put(7.0f, 80);
        myMap.put(8.0f, 90);
        myMap.put(9.0f, 100);

    }

    public void day(View v) {
        new DatePickerDialog(ManualLoad.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void time(View v) {
        new TimePickerDialog(ManualLoad.this, timeO, myCalendar
                .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                false).show();
    }

    public void ready(View v) {
        if (isChartSelected && isTimeSelected && isDateSelected) {
//            All three values are fetched below
//            selectedChartItem
//            day.getText().toString();
//            time.getText().toString();

        }
    }

    public void back(View v) {
        finish();
    }

    private void updateLabelDate() {
        isDateSelected = true;
        String myFormat = "dd/MM/yy"; //In which you need put here
//        String myFormat = "EEEE"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        day.setText(sdf.format(myCalendar.getTime()));
        setReadyAllowed();
    }

    private void updateLabelTime() {

        isTimeSelected = true;
        String myFormat = "hh:mm aaa"; //In which you need put here
//        String myFormat = "EEEE"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        time.setText(sdf.format(myCalendar.getTime()));
        setReadyAllowed();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
            entries.add(new PieEntry((float) (10.0), mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(15f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.rgb("#3AD8BE"));
        colors.add(ColorTemplate.rgb("#3AA6DD"));
        colors.add(ColorTemplate.rgb("#465FDB"));
        colors.add(ColorTemplate.rgb("#7244D8"));
        colors.add(ColorTemplate.rgb("#C03EDB"));
        colors.add(ColorTemplate.rgb("#DB455E"));
        colors.add(ColorTemplate.rgb("#DD7440"));
        colors.add(ColorTemplate.rgb("#D1B333"));
        colors.add(ColorTemplate.rgb("#A3D83A"));
        colors.add(ColorTemplate.rgb("#5fce48"));
//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);  to set fonts
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    void setReadyAllowed() {
        if (isChartSelected && isTimeSelected && isDateSelected)
            ready.setBackgroundResource(R.drawable.manual_ready_green_btn);
        else
            ready.setBackgroundResource(R.drawable.manual_ready_grey_btn);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        isChartSelected = true;
        setReadyAllowed();
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
//        selectedChartItem = (int) (h.getX() + 1.0f);
        selectedChartItem = myMap.get(h.getX());
        log(selectedChartItem, PSStrings.showLog);

    }

    @Override
    public void onNothingSelected() {
        isChartSelected = false;
        setReadyAllowed();
    }
}
