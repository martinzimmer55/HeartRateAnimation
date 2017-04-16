package com.appxone.heartrateanimationapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class BraceletEmergency extends MyActivity {

    public int terminar = 0;
    JSONParser jParser = new JSONParser();
    private static String url_get_alerts = "http://pillsandcare.esy.es/pillsconnect/get_alerts.php";
    private static String url_set_alert_ack = "http://pillsandcare.esy.es/pillsconnect/setAlertAck.php";
    private static String url_get_persona = "http://pillsandcare.esy.es/pillsconnect/get_persona.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ALERTAS = "alertas";
    private static final String TAG_ID = "id";
    private static final String TAG_PERSONA = "persona";
    private static final String TAG_LUGAR = "lugar";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_PERSONAS = "personas";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TELEFONO = "telefono";

    JSONArray arrayAlertas = null;
    JSONArray arrayPersonas = null;

    private String idAlerta = "";
    private String persona = "";
    private String nombre = "";
    private String fecha = "";
    private String lugar = "";
    private String telefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_emergency);
        setHeader("Recibir Alertas");

        getSupportActionBar().setElevation(0.0f);

        new LeerAlertas().execute();
    }

    public void reset(View v) {

    }

    public void back(View v) {
        Intent intent = new Intent(BraceletEmergency.this, BraceletDemo.class);
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

    class LeerAlertas extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            //int contador = 0;
            int success = 0;
            //ahora voy a empezar a leer en la tabla de alertas hasta encontrar una
            do {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("pcid", "1");
                JSONObject jsonMarcas = jParser.makeHttpRequest(url_get_alerts, "GET", params);
                try {
                    success = jsonMarcas.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        //si encontre una marca saco los datos
                        arrayAlertas = jsonMarcas.getJSONArray(TAG_ALERTAS);
                        int largo = arrayAlertas.length();
                        JSONObject a = arrayAlertas.getJSONObject(largo-1);
                        idAlerta = a.getString(TAG_ID);
                        persona = a.getString(TAG_PERSONA);
                        lugar = a.getString(TAG_LUGAR);
                        fecha = a.getString(TAG_FECHA);
                        //luego de sacar los datos la marco como leida (ack=1)
                        HashMap<String, String> paramAck = new HashMap<String, String>();
                        paramAck.put("id", idAlerta);
                        JSONObject jsonAck = jParser.makeHttpRequest(url_set_alert_ack, "GET", paramAck);
                        //busco la persona para completar los datos
                        HashMap<String, String> paramPersona = new HashMap<String, String>();
                        paramPersona.put("id", persona);
                        JSONObject jsonPersona = jParser.makeHttpRequest(url_get_persona, "GET", paramPersona);
                        arrayPersonas = jsonPersona.getJSONArray(TAG_PERSONAS);
                        JSONObject p = arrayPersonas.getJSONObject(0);
                        nombre = p.getString(TAG_NOMBRE);
                        telefono = p.getString(TAG_TELEFONO);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //contador++;
            } while ((success == 0) && (terminar != 1));
            //while ((success == 0) && (contador < 30) && (terminar != 1));
            return null;
        }

        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (nombre.compareTo("") != 0) {
                        Intent intent = new Intent(BraceletEmergency.this, EmergenciaPopUp.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("fecha", fecha);
                        intent.putExtra("lugar", lugar);
                        startActivity(intent);
                    }
                }
            });
        }
    }


}
