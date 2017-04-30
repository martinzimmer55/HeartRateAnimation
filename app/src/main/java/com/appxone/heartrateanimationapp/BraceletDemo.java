package com.appxone.heartrateanimationapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONObject;

import java.util.HashMap;

public class BraceletDemo extends MyActivity {

    private static String url_crear_comando = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_demo);
        setHeader("Bracelet Demo");

        getSupportActionBar().setElevation(0.0f);
        TextView hearrate = (TextView) findViewById(R.id.hearrate);
        TextView notification = (TextView) findViewById(R.id.notification);
        TextView bracelet = (TextView) findViewById(R.id.bracelet);

        hearrate.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        notification.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        bracelet.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
    }

    public void heartrate(View v) {
        startMyActivity(MeasuringHeartRate.class);
    }

    public void notifications(View v) {
        startMyActivity(NotificationsBracelet.class);
    }

    public void bracelet(View v) {
        startMyActivity(BraceletEmergency.class);
    }

    public void openEmergencySlot(View v) {
        //new PosicionarSlotEmergencia().execute();
        //new EsperarAperturaPorPulsera().execute();
    }


    public void back(View v) {
        Intent intent = new Intent(BraceletDemo.this, Home.class);
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

    class PosicionarSlotEmergencia extends AsyncTask<String, String, String> {
        //primero tengo que posicionar en el slot que se precisa y abrir la compuerta
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> posicionarSlot = new HashMap<String, String>();
            posicionarSlot.put("cmd", "A");
            posicionarSlot.put("args", "18");
            posicionarSlot.put("pcid", "1");
            HashMap<String, String> abrirSlot = new HashMap<String, String>();
            abrirSlot.put("cmd", "B");
            abrirSlot.put("pcid", "1");
            try {
                JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", posicionarSlot);
            }
            catch (Exception e) {
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

    class EsperarAperturaPorPulsera extends AsyncTask<String, String, String> {
        //primero tengo que posicionar en el slot que se precisa y abrir la compuerta
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
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
