package com.appxone.heartrateanimationapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.ActionBar;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.R.attr.x;


public class AssistedLoadLoading extends MyActivity implements OnChartValueSelectedListener {

    private static String url_crear_comando = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    JSONParser jParser = new JSONParser();
    Integer slot = 0;
    int puestos = 0;
    String medicamento = "";
    String dosis = "";
    int tomas = 0;

    private PieChart mChart;
    protected String[] mParties = new String[]{
            "", "", "", "", "", "", "", "",
            "", ""
    };
    int selectedChartItem;
    Calendar myCalendar;
    TextView t1;
    ImageView ready;
    HashMap<Float, Integer> myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        medicamento = extras.getString("medicamento");
        dosis = extras.getString("dosis");
        tomas = extras.getInt("tomas");
        setContentView(R.layout.activity_assisted_load_loading);
        setHeader("Carga Asistida");
        getSupportActionBar().setElevation(0.0f);
        mChart = (PieChart) findViewById(R.id.chart);
        t1 = (TextView) findViewById(R.id.t1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        // disable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);
        setData(10, 100);
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.highlightValue(slot.floatValue(), 0, false);

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

        ready = (ImageView) findViewById(R.id.ready);
        setReadyAllowed();
        t1.setText("Espere a que se abra el compartimiento y coloque una pastilla de " + medicamento + " " + dosis + ". Luego presione \"Ready\" para continuar con la siguiente dosis.");

        //****************************************************************************
        //tirar comando para inicializar el pastillero, ir a la posicion 0 y abrir la compuerta
        //****************************************************************************
        //comando ir al slot cero
        HashMap<String, String> paramsComandoInicial = new HashMap<String, String>();
        paramsComandoInicial.put("cmd", "D");
        paramsComandoInicial.put("args", "");
        paramsComandoInicial.put("pcid", "1");
        try {
            JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoInicial);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //comando abrir compuerta
        HashMap<String, String> paramsComandoAbrir = new HashMap<String, String>();
        paramsComandoAbrir.put("cmd", "H");
        paramsComandoAbrir.put("args", "");
        paramsComandoAbrir.put("pcid", "1");
        try {
            JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoAbrir);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ready(View v) {
        if (puestos < tomas) {
            new Posicionar().execute();
            mChart.highlightValue(slot.floatValue() + 1, 0, false);
        }
        else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AssistedLoadLoading.this);
            builder1.setTitle("Carga asistida finalizada");
            builder1.setMessage("Ha finalizado la carga asistida");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new CerrarCompuerta().execute();
                            Intent intent = new Intent(AssistedLoadLoading.this, Home.class);
                            dialog.cancel();
                            finish();
                            startActivity(intent);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void back(View v) {
        finish();
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
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (10.0), mParties[i % mParties.length]));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(15f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
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
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    void setReadyAllowed() {
        ready.setBackgroundResource(R.drawable.manual_ready_green_btn);
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
        setReadyAllowed();
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
        selectedChartItem = myMap.get(h.getX());
        log(selectedChartItem, PSStrings.showLog);
    }

    @Override
    public void onNothingSelected() {
        setReadyAllowed();
    }

    class Posicionar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            if (puestos < tomas) {// Si todavia queda medicacion por poner
                // Ya tengo lleno el slot 0, tengo que cerrar compuerta, ir al slot 1, abrir compuerta y quedarme esperando el siguiente medicamento
                // comando cerrar compuerta
                HashMap<String, String> paramsComandoCerrar = new HashMap<String, String>();
                paramsComandoCerrar.put("cmd", "C");
                paramsComandoCerrar.put("args", "");
                paramsComandoCerrar.put("pcid", "1");
                try {
                    JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoCerrar);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                puestos ++;
                slot ++;
                //comando ir al slot siguiente
                HashMap<String, String> paramsComandoSiguiente = new HashMap<String, String>();
                paramsComandoSiguiente.put("cmd", "A");
                paramsComandoSiguiente.put("args", "0" + slot.toString());
                paramsComandoSiguiente.put("pcid", "1");
                try {
                    JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoSiguiente);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                //comando abrir compuerta
                HashMap<String, String> paramsComandoAbrir = new HashMap<String, String>();
                paramsComandoAbrir.put("cmd", "H");
                paramsComandoAbrir.put("args", "");
                paramsComandoAbrir.put("pcid", "1");
                try {
                    JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoAbrir);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }
    }

    class CerrarCompuerta extends AsyncTask<String, String, String> {
        //primero tengo que posicionar en el slot que se precisa y abrir la compuerta
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            HashMap<String, String> paramsComandoCerrarPuerta = new HashMap<String, String>();
            paramsComandoCerrarPuerta.put("cmd", "C");
            paramsComandoCerrarPuerta.put("pcid", "1");
            try {
                JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoCerrarPuerta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }
    }


}
