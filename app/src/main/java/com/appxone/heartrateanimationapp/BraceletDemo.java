package com.appxone.heartrateanimationapp;

import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BraceletDemo extends MyActivity {

    private static String url_crear_comando = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    private static String url_get_respuesta = "http://pillsandcare.esy.es/pillsconnect/get_bracelet_response.php";
    private static int terminar = 0;
    private static boolean slotAbierto = false;
    private static final String TAG_SUCCESS = "success";
    JSONParser jParser = new JSONParser();
    //private TextView txt = (TextView)findViewById(R.id.txtvw1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_demo);
        setHeader("Demo Pulsera");

        getSupportActionBar().setElevation(0.0f);
        TextView hearrate = (TextView) findViewById(R.id.hearrate);
        TextView notification = (TextView) findViewById(R.id.notification);
        TextView bracelet = (TextView) findViewById(R.id.bracelet);

        hearrate.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        notification.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        bracelet.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
    }

    public void heartrate(View v) {
        //startMyActivity(MeasuringHeartRate.class);
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.oudmon.bandvt");
        startActivity(launchIntent);
    }

    public void notifications(View v) {
        startMyActivity(NotificationsBracelet.class);
    }

    public void bracelet(View v) {
        startMyActivity(BraceletEmergency.class);
    }

    public void openEmergencySlot(View v) {
       // TextView txt = (TextView)findViewById(R.id.txtvw1);
       // txt.setText("Espere que se posicione el slot de emergencia para abrir la compuerta con la pulsera.");
       // new PosicionarSlotEmergencia().execute();
       // new EsperarAperturaPorPulsera().execute();
        Intent intent = new Intent(BraceletDemo.this, AperturaEmergencia.class);
        //finish();
        startActivity(intent);
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
            posicionarSlot.put("args", "10");
            posicionarSlot.put("pcid", "1");
            HashMap<String, String> abrirSlot = new HashMap<String, String>();
            abrirSlot.put("cmd", "B");
            abrirSlot.put("pcid", "1");
            try {
                JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", posicionarSlot);
                JSONObject jsonAbrirSlot = jParser.makeHttpRequest(url_crear_comando, "GET", abrirSlot);
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

            int contador = 0;
            int success = 0;
            //ahora voy a empezar a leer en la tabla de respuestas a ver si leyo la pulsera
            do {
                HashMap<String, String> params = new HashMap<String, String>();
               // params.put("pcid", "1");
                JSONObject jsonResp = jParser.makeHttpRequest(url_get_respuesta, "POST", params);
                try {
                    success = jsonResp.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        //si encontre una respuesta tengo que abrir el slot
                        //HashMap<String, String> abrirPuerta = new HashMap<String, String>();
                        //abrirPuerta.put("cmd", "H");
                        //abrirPuerta.put("args", "");
                        //abrirPuerta.put("pcid", "1");
                        //JSONObject jsonAbrirPuerta = jParser.makeHttpRequest(url_crear_comando, "GET", abrirPuerta);
                        slotAbierto = true;
                        terminar = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                contador++;
            } while ((success == 0) && (contador < 15) && (terminar != 1));
            return null;
        }
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (slotAbierto == true){
                        AperturaFinalizada();
                        slotAbierto = false;
                        terminar = 0;
                    }
                    else {
                        AperturaCancelada();
                        terminar = 0;
                    }
                }
            });
        }
    }
    public void AperturaFinalizada(){
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(BraceletDemo.this);
        builder1.setTitle("Apertura de emergencia");
        builder1.setMessage("Se ha abierto el slot de emergencia, retire la medicacion y presione OK para cerrar el compartimiento.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new BraceletDemo.CerrarCompuerta().execute();
                        Intent intent = new Intent(BraceletDemo.this, Home.class);
                        dialog.cancel();
                        finish();
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void AperturaCancelada(){
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(BraceletDemo.this);
        builder1.setTitle("Apertura de emergencia");
        builder1.setMessage("Se ha cancelado la apertura de emergencia.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(BraceletDemo.this, Home.class);
                        dialog.cancel();
                        finish();
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
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

    @Override
    public void onBackPressed() {

    }

}
