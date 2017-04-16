package com.appxone.heartrateanimationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MeasuringHeartRate extends MyActivity {
    ImageView imageView, initimage;
    AnimationDrawable anim;
    boolean activityFinished;
    int min = 55;
    int max = 100;
    CopyOfDataBaseManager db;
    long waitTime = 5000;   // waitTime for detecting heart value ... it is 5s =5000ms after that it will open Result screen


    //**********************************************************
    JSONParser jParser = new JSONParser();
    private String lectura = "0";
    JSONArray arrayLecturas = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LECTURAS = "lecturas";
    private static final String TAG_VALOR = "valor";
    private static final String TAG_ID = "id";

    private static String url_medir_pulso = "http://pillsandcare.esy.es/pillsconnect/get_pulse_lecture.php?pcid=1";
    private static String url_set_ack = "http://pillsandcare.esy.es/pillsconnect/setLectureAck.php?id=";
    private static String url_write_command = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    private int terminar = 0;
    //**********************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_heart_rate);
        //setHeader("Measuring Heart Rate");
        setHeader("Realizando medición de pulso");
        getSupportActionBar().setElevation(0.0f);
        db = new CopyOfDataBaseManager(getApplicationContext());
        imageView = (ImageView) findViewById(R.id.image);
        initimage = (ImageView) findViewById(R.id.initimage);
        imageView.setBackgroundResource(R.drawable.heart);
        TextView t1 = (TextView) findViewById(R.id.t1);
        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t1.setText("Estableciendo comunicación con el dispositivo para realizar medición. Por favor espere unos segundos...");
        anim = (AnimationDrawable) imageView.getBackground();
        //**********************************************************
        new CargarPulso().execute();
        //**********************************************************

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
        Intent intent = new Intent(MeasuringHeartRate.this, BraceletDemo.class);
        terminar = 1;
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

    //**********************************************************
    class CargarPulso extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //startAnim(imageView);
            initimage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            anim.start();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> paramsComando = new HashMap<String, String>();
            paramsComando.put("cmd", "READ_PULSE");
            paramsComando.put("args", "");
            paramsComando.put("pcid", "1");
            JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_write_command, "POST", paramsComando);
            int contador = 0;
            int success = 0;
            do {
                HashMap<String, String> params = new HashMap<String, String>();
                JSONObject jsonLectura = jParser.makeHttpRequest(url_medir_pulso, "GET", params);
                try {
                    success = jsonLectura.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        arrayLecturas = jsonLectura.getJSONArray(TAG_LECTURAS);
                        JSONObject l = arrayLecturas.getJSONObject(0);
                        lectura = l.getString(TAG_VALOR);
                        String idLectura = l.getString(TAG_ID);
                        JSONObject jsonAck = jParser.makeHttpRequest(url_set_ack+idLectura, "GET", params);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                contador ++;
            } while ((success == 0) && (contador < 15) && (terminar != 1));
            return null;
        }

        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                    anim.stop();
                    if (lectura != "0"){
                        //setear lectura
                        db.insert_update("INSERT INTO records (heartrate, datetime) VALUES('" + lectura + "','" + setCurrentDateTime() + "')");
                        startMyActivity(Result.class);
                    } else {
                        //tirar error en la lectura
                        AlertDialog alerta =new AlertDialog.Builder(MeasuringHeartRate.this).create();
                        alerta.setTitle("Error en la medicion");
                        alerta.setMessage("Error de comunicacion con la pulsera, por favor pruebe de nuevo en unos segundos. ");

                        alerta.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MeasuringHeartRate.this, BraceletDemo.class);
                                startActivity(intent);
                            }
                        });
                        alerta.show();
                    }
                }
            });
        }
    }
    //**********************************************************


}
