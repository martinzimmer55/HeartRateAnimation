package com.appxone.heartrateanimationapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;
import com.appxone.heartrateanimationapp.Utils.FontNames;

import org.json.JSONObject;

import java.util.HashMap;

public class NotificationsBracelet extends MyActivity {
    private static String url_crear_comando = "http://pillsandcare.esy.es/pillsconnect/write_command.php";
    private Spinner spinner;
    String slot = "0";
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_bracelet);
        setHeader("Notifications to Bracelet");
        getSupportActionBar().setElevation(0.0f);

        spinner = (Spinner)findViewById(R.id.spinner_slot);
        //Creamos el adaptador
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.slot,android.R.layout.simple_spinner_item);
        //Añadimos el layout para el menú
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }

   public void moverAPosicion(View v){
       spinner = (Spinner)findViewById(R.id.spinner_slot);
       slot = spinner.getSelectedItem().toString();
       new Posicionar().execute();
   }
    class Posicionar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> paramsComandoPosicionar = new HashMap<String, String>();
            paramsComandoPosicionar.put("cmd", "A");
            String argumentos = "0" + slot;
            paramsComandoPosicionar.put("args", argumentos);
            paramsComandoPosicionar.put("pcid", "1");
            try {
                JSONObject jsonWriteCommand = jParser.makeHttpRequest(url_crear_comando, "GET", paramsComandoPosicionar);
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

    public void abrirCompuertaConPulsera (View v) {
        new AbrirConPulsera().execute();
    }
    class AbrirConPulsera extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> abrirCompuerta = new HashMap<String, String>();
            abrirCompuerta.put("cmd", "B");
            abrirCompuerta.put("pcid", "1");
            try {
                JSONObject jsonAbrirSlot = jParser.makeHttpRequest(url_crear_comando, "GET", abrirCompuerta);
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

    public void cerrarCompuerta(View v){
        new Cerrar().execute();
    }

    class Cerrar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> cerrarCompuerta = new HashMap<String, String>();
            cerrarCompuerta.put("cmd", "C");
            cerrarCompuerta.put("pcid", "1");
            try {
                JSONObject jsonCerrar = jParser.makeHttpRequest(url_crear_comando, "GET", cerrarCompuerta);
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

    public void irPosicionCero (View v){
        new PosicionCero().execute();
    }

    class PosicionCero extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> posicionar = new HashMap<String, String>();
            posicionar.put("cmd", "D");
            posicionar.put("pcid", "1");
            try {
                JSONObject jsonPosicionCero = jParser.makeHttpRequest(url_crear_comando, "GET", posicionar);
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

    public void registrarHuella (View v) {
        new Registrar().execute();
    }

    class Registrar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> registrar = new HashMap<String, String>();
            registrar.put("cmd", "F");
            registrar.put("pcid", "1");
            try {
                JSONObject jsonRegistrar = jParser.makeHttpRequest(url_crear_comando, "GET", registrar);
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

    public void borrarHuellas(View v){
        new Borrar().execute();
    }

    class Borrar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> borrar = new HashMap<String, String>();
            borrar.put("cmd", "G");
            borrar.put("pcid", "1");
            try {
                JSONObject jsonBorrar = jParser.makeHttpRequest(url_crear_comando, "GET", borrar);
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

    public void abrirCompuertaSinPulsera(View v){
        new AbrirSinPulsera().execute();
    }
    class AbrirSinPulsera extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            HashMap<String, String> abrir = new HashMap<String, String>();
            abrir.put("cmd", "H");
            abrir.put("pcid", "1");
            try {
                JSONObject jsonBorrar = jParser.makeHttpRequest(url_crear_comando, "GET", abrir);
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


    public void back(View v) {
        Intent intent = new Intent(NotificationsBracelet.this, Home.class);
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
}
