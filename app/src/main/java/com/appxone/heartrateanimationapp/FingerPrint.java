package com.appxone.heartrateanimationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FingerPrint extends MyActivity {
    TextView name, address, age, datetime, loc;
    ImageView scan;

    //**************************************************
    JSONParser jParser = new JSONParser();
    JSONArray arrayMarcas = null;
    JSONArray arrayPersonas = null;
    private String marca = "";
    private String persona = "";
    private String lugar = "";
    private String fecha = "";
    private String nombre = "";
    private String direccion = "";
    private String edad = "";
    private int terminar = 0;

    private static String url_write_command = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    private static String url_get_marca = "http://pillsandcare.esy.es/pillsconnect/get_marcas.php";
    private static String url_set_marca_ack = "http://pillsandcare.esy.es/pillsconnect/setMarcaAck.php";
    private static String url_get_persona = "http://pillsandcare.esy.es/pillsconnect/get_persona.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MARCAS = "marcas";
    private static final String TAG_ID = "id";
    private static final String TAG_PERSONA = "persona";
    private static final String TAG_LUGAR = "lugar";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_PERSONAS = "personas";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_EDAD = "edad";
    //**************************************************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        setHeader("Lectura de huella digital");
        getSupportActionBar().setElevation(0.0f);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);
        TextView t3 = (TextView) findViewById(R.id.t3);
        TextView t4 = (TextView) findViewById(R.id.t4);
        TextView t5 = (TextView) findViewById(R.id.t5);
        TextView t6 = (TextView) findViewById(R.id.t6);
        TextView t7 = (TextView) findViewById(R.id.t7);
        TextView t8 = (TextView) findViewById(R.id.t8);
        scan = (ImageView) findViewById(R.id.scan);
        ImageView btnReset = (ImageView) findViewById(R.id.reset);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        age = (TextView) findViewById(R.id.age);
        datetime = (TextView) findViewById(R.id.datetime);
        loc = (TextView) findViewById(R.id.loc);
        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t3.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t4.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t5.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t6.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        t7.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        name.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        address.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        age.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        datetime.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));
        loc.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_ROMAN));

//        Setting users image and text
        //setImageAndTexts("Steven Smith", "Block A, Gulistan e Joha", "25 years", "20/02/2017 04:30pm", "Street University Road", R.drawable.fingerprint_demo_icon);
        t1.setText("Realizando consulta");
        t2.setText("Estableciendo comunicación con el dispositivo para realizar la consulta. Por favor espere unos segundos...");
        t3.setText("Datos de la persona:");
        t4.setText("Nombre:");
        name.setText("");
        t5.setText("Dirección:");
        address.setText("");
        t6.setText("Edad:");
        age.setText("");
        t7.setText("Fecha y hora:");
        datetime.setText("");
        t8.setText("Ubicación:");
        loc.setText("");
        new CargarHuella().execute();
    }

    public void setImageAndTexts(String nameS, String addressS, String ageS, String datetimeS, String locS, Integer image) {
        name.setText(nameS);
        address.setText(addressS);
        age.setText(ageS);
        datetime.setText(datetimeS);
        loc.setText(locS);
        scan.setImageResource(image);
    }

    public void reset(View v) {
        Intent intent = new Intent(FingerPrint.this, FingerPrint.class);
        terminar = 1;
        finish();
        startActivity(intent);
    }

    public void back(View v) {
        terminar = 1;
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

    @Override
    public void onTaskComplete(String result, String key) {

    }


    class CargarHuella extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            //escribo el comando para el lector en la tabla de comandos
            HashMap<String, String> paramsComando = new HashMap<String, String>();
            paramsComando.put("cmd", "E");
            paramsComando.put("args", "");
            paramsComando.put("pcid", "1");
            JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_write_command, "POST", paramsComando);
            int contador = 0;
            int success = 0;
            //ahora voy a empezar a leer en la tabla de marcas a ver si encuentro una
            do {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("pcid", "1");
                JSONObject jsonMarcas = jParser.makeHttpRequest(url_get_marca, "POST", params);
                try {
                    success = jsonMarcas.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        //si encontre una marca saco los datos
                        arrayMarcas = jsonMarcas.getJSONArray(TAG_MARCAS);
                        int largo = arrayMarcas.length();
                        JSONObject m = arrayMarcas.getJSONObject(largo-1);
                        marca = m.getString(TAG_ID);
                        persona = m.getString(TAG_PERSONA);
                        lugar = m.getString(TAG_LUGAR);
                        fecha = m.getString(TAG_FECHA);
                        //luego de sacar los datos la marco como leida (ack=1)
                        HashMap<String, String> paramAck = new HashMap<String, String>();
                        paramAck.put("id", marca);
                        JSONObject jsonAck = jParser.makeHttpRequest(url_set_marca_ack, "GET", paramAck);
                        //busco la persona para completar los datos
                        HashMap<String, String> paramPersona = new HashMap<String, String>();
                        paramPersona.put("id", persona);
                        JSONObject jsonPersona = jParser.makeHttpRequest(url_get_persona, "GET", paramPersona);
                        arrayPersonas = jsonPersona.getJSONArray(TAG_PERSONAS);
                        JSONObject p = arrayPersonas.getJSONObject(0);
                        nombre = p.getString(TAG_NOMBRE);
                        direccion = p.getString(TAG_DIRECCION);
                        edad = p.getString(TAG_EDAD);
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
                    if (nombre.compareTo("") != 0) {
                        //mostrar datos
                        //setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.fingerprint_demo_icon);
                        switch (persona) {
                            case "1": setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.rodrigo);
                                break;
                            case "2": setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.robert);
                                break;
                            case "3": setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.lucia);
                                break;
                            case "4": setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.martin);
                                break;
                            case "5": setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.flor);
                                break;
                            default: setImageAndTexts(nombre, direccion, edad, fecha, lugar, R.drawable.flor);
                                break;
                        }
                    } else {
                        AlertDialog alerta = new AlertDialog.Builder(FingerPrint.this).create();
                        alerta.setTitle("Error al recopilar datos");
                        alerta.setMessage("Error de comunicacion con el lector, por favor pruebe de nuevo en unos segundos. ");
                        alerta.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FingerPrint.this, Home.class);
                                finish();
                                startActivity(intent);
                            }
                        });
                        alerta.show();
                        //txtLectura.setText("Error en la comunicacion con la pulsera!");
                    }

                }
            });
        }
    }




}

