package com.appxone.heartrateanimationapp.FrameUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class MyActivity extends AppCompatActivity {

    Context context;

    // For GPS
    LocationManager lm;
    Location location;

    boolean isUserLoggedIn = false;
    boolean isUserFromFb = false;

    protected boolean allowSynch = true;

    private CommonRequest commonRequest;
    private CommonRequest_Payload commonRequest_Payload;
    static SharedPreferences myPrefSpot;
    static SharedPreferences.Editor myEditSpot;

    // Loggin Store
    public static final String KEY_LOGIN_EMAIL = "login_email";
    public static final String KEY_LOGIN_PASSWORD = "login_password";
    public static final String KEY_LOGIN_URL = "login_url";
    public static final String KEY_LOGIN_STATUS = "login_status";
    public static final String KEY_LANGUAGE = "language_selected";
    public static final String KEY_CHECKBOX = "chk";

    protected static final int NAME_LENGTH = 3;
    protected static final int PASSWORD_LENGTH = 8;

    String currentLanguage = "";
    private boolean FB_LOG_KEYHASH = true;

    public MyActivity() {
        super();
        context = this;
    }

    protected void hitUrl(String key, String url) {
        hitUrl(key, url, "");
    }

    protected void hitUrl(String key, String url, String progressTitle) {
        commonRequest = new CommonRequest(context);
        commonRequest.setActivity(this);
        commonRequest.setUrl(url);
        commonRequest.setKey(key);
        commonRequest.setProgressTitle(progressTitle);
        commonRequest.execute("");
    }

    protected void hitUrl_Payload(String key, String url, String payload) {
        hitUrl_Payload(key, url, payload, "");
    }

    protected void hitUrl_Payload(final String key, final String url,
                                  final String payload, final String progressTitle) {

        String[] params = new String[]{url, payload};

        commonRequest_Payload = new CommonRequest_Payload(context);
        commonRequest_Payload.setActivity(MyActivity.this);
        commonRequest_Payload.setKey(key);
        commonRequest_Payload.setProgressTitle(progressTitle);
        commonRequest_Payload.execute(params);
    }

    public static void writeString2(Context ctx, String key, String value) {
        SharedPreferences myPrefSpot = ctx.getSharedPreferences(
                StringUtils.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor myEditSpot = myPrefSpot.edit();
        myEditSpot.putString(key, value);
        myEditSpot.commit();
    }

    public static void writeInt(String key, int value) {
        myEditSpot.putInt(key, value);
        myEditSpot.commit();
    }

    public static void writeBoolean(String key, Boolean value) {
        myEditSpot.putBoolean(key, value);
        myEditSpot.commit();
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getString(String key) {
        return myPrefSpot.getString(key, "");
    }

    public static String getString2(Context ctx, String key) {
        SharedPreferences myPrefSpot = ctx.getSharedPreferences(
                StringUtils.PREFS_NAME, MODE_PRIVATE);
        return myPrefSpot.getString(key, "");
    }

    public static int getInt(String key) {
        return myPrefSpot.getInt(key, 0);
    }

    public static Boolean getBoolean(String key) {
        return myPrefSpot.getBoolean(key, false);
    }

    public abstract void onTaskComplete(String result, String key);

    public String getAmountWithCommas(String price) {
        return new DecimalFormat("#,###").format(Double.parseDouble(price));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPrefSpot = getSharedPreferences(StringUtils.PREFS_NAME, MODE_PRIVATE);
        myEditSpot = myPrefSpot.edit();

        getPrefs();
    }

    private void getPrefs() {
        // TODO Auto-generated method stub
        String isLoggedIn = getString(StringUtils.PREF_USER_TYPE);

        log("isLoggedIn", "" + isLoggedIn);

        if (isLoggedIn != null) {
            if (isLoggedIn.equalsIgnoreCase(StringUtils.USER_TYPE_EMAIL)) {
                isUserFromFb = false;
                isUserLoggedIn = true;
            } else if (isLoggedIn.equalsIgnoreCase(StringUtils.USER_TYPE_FB)) {
                isUserFromFb = true;
                isUserLoggedIn = true;
            } else {
                isUserLoggedIn = false;
            }
        } else {
            isUserLoggedIn = false;
        }
    }

    public static void writeString(String key, String value) {
        myEditSpot.putString(key, value);
        myEditSpot.commit();
    }

    @Override
    protected void onPause() {

        allowSynch = false;
        if (null != commonRequest) {
            try {
                commonRequest.cancel(true);
            } catch (Exception e) {
            }
        }
        super.onPause();
    }

    public void openLink(String url) {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
            // Try with the default browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    public int getDurationFromCredit() {
        float currentbalance;
        if (getString("currentcredit").equals(""))
            currentbalance = Float.parseFloat("0.0");
        else
            currentbalance = Float.parseFloat(getString("currentcredit"));
//        float currentbalance = Float.parseFloat("1.54");
        currentbalance = currentbalance - 1;
        int duration = (int) (currentbalance * 2);
        return duration;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    protected void confirmation(String title, String message,
                                DialogInterface.OnClickListener oKListener,
                                DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_menu_help)
                .setTitle(title).setMessage(message)
                .setPositiveButton("OK", oKListener)
                .setNegativeButton("Cancel", cancelListener).show();
    }

    protected void confirmation(String title, String message,
                                DialogInterface.OnClickListener oKListener) {
        confirmation(title, message, oKListener, null);
    }

    protected void warning(String title, String message,
                           OnClickListener onClickListener) {

        warning(title, message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    protected void warning(String title, String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).show();
    }

    protected void warning(String title, String message,
                           DialogInterface.OnClickListener oKListener) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(message).setPositiveButton("OK", oKListener).show();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void toast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NewApi")
    public boolean isEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    @SuppressLint("NewApi")
    public boolean isPhone(String phone) {

        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phone).matches();

    }

//    public void sessionExpiredLogout() {
//        writeString(PSStrings.USERID, "");
//        writeString(PSStrings.USERKEY, "");
//        writeString(PSStrings.SELECTEDGROUPID, "");
//        writeString(PSStrings.SELECTEDGROUPNAME, "");
//        writeBoolean(PSStrings.ISLOGGEDIN, false);
//        toast("Session Expired");
//        Intent i = new Intent(getApplicationContext(), Login.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        finish();
//    }

    public String UTF_encode(String value) {
        String converted = null;
        try {
            converted = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return converted;
    }

    public boolean isAge(String str_Age) {
        int age = Integer.parseInt(str_Age);
        if (age > 1 && age < 150) {
            return true;
        } else
            return false;
    }

    public boolean isName(String str_Name) {
        if (!isEmpty(str_Name)) {
            // Checking names limit
            if (str_Name.length() >= NAME_LENGTH) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isPasswordMatching(String pass1, String pass2,
                                      boolean checkPasswordLimit) {

        if (checkPasswordLimit) {
            if (pass1.length() >= PASSWORD_LENGTH
                    && pass2.length() >= PASSWORD_LENGTH) {
                return isPasswordMatching(pass1, pass2);
            } else {
                return false;
            }
        } else {
            return isPasswordMatching(pass1, pass2);
        }
    }

    public boolean isPasswordMatching(String pass1, String pass2) {
        if (!isEmpty(pass1) && !isEmpty(pass2)) {
            if (pass1.equals(pass2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setCancelable(false);
        alertDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

                // finish();

            }
        });
        // Setting Dialog Title
        alertDialog.setTitle("Location");

        // Setting Dialog Message
        alertDialog
                .setMessage("Unable find your location. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean isSDCardAvailable() {
        Boolean isSDPresent = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        return isSDPresent;
    }

    public void log(int text, boolean showLog) {
        if (showLog)
            log("" + text, showLog);
    }

    public void log(long text, boolean showLog) {
        if (showLog) log("" + text, showLog);
    }

    public void log(double text, boolean showLog) {
        if (showLog)
            log("" + text, showLog);
    }

    public void log(float text, boolean showLog) {
        if (showLog) log("" + text, showLog);
    }

    public void log(String text, boolean showLog) {
        if (showLog)
            log("" + getApplicationContext().getClass().getSimpleName(), "" + text);
    }

    public void log(int key, String text) {
        log("" + key, "" + text);
    }

    public void log(long key, int text) {
        log("" + key, "" + text);
    }

    public void log(float key, int text) {
        log("" + key, "" + text);
    }

    public void log(double key, int text) {
        log("" + key, "" + text);
    }

    public void log(String key, int text) {
        log("" + key, "" + text);
    }

    public void log(String key, long text) {
        log("" + key, "" + text);
    }

    public void log(String key, double text) {
        log("" + key, "" + text);
    }

    public void log(String key, float text) {
        log("" + key, "" + text);
    }

    public void log(String key, String text) {
        Log.e(key, text);
    }

    public void startMyActivity(Class<?> classToLoad) {
        startMyActivity(classToLoad, false);
    }

    public void startMyActivity(Class<?> classToLoad, boolean finishCurrentAct) {
        Intent i = new Intent(getApplicationContext(), classToLoad);
        startActivity(i);
        if (finishCurrentAct) {
            finish();
        }
    }

    public void startMyActivity(Class<?> classToLoad, Bundle b) {

        if (b != null) {
            startMyActivity(classToLoad, b, false);
        } else {
            startMyActivity(classToLoad, false);
        }
    }

    public void startMyActivity(Class<?> classToLoad, Bundle b,
                                boolean finishCurrentAct) {

        if (b != null) {
            Intent i = new Intent(getApplicationContext(), classToLoad);
            i.putExtras(b);
            startActivity(i);
            if (finishCurrentAct) {
                finish();
            }
        } else {
            startMyActivity(classToLoad, finishCurrentAct);
        }
    }

    public boolean isEmpty(String str) {
        boolean temp = true;

        if (TextUtils.isEmpty(str.trim())) {
            temp = true;
        } else {
            temp = false;
        }
        return temp;
    }

    public String replace(String str, String old, String newChar) {
        if (!isEmpty(old) && !isEmpty(newChar)) {
            try {
                return str.replace(old, URLEncoder.encode(newChar, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        } else
            return "";
    }

    protected boolean isUserLoggedIn() {
        // TODO Auto-generated method stub
        String isLoggedIn = getString2(context, StringUtils.PREF_IS_LOGIN);

        if (isLoggedIn.equals("") || isLoggedIn.equals(null))
            return false;
        else
            return true;
    }

    public String getFBHashKey() {
        String key = "";
        if (FB_LOG_KEYHASH) {
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                    if (AppSettings.FB_COPY_KEYHASH_TO_CLIPBOARD) {
////                        copyToClipBoard(key);
//                    }
                    log("KeyHash FB:", key);
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        }
        return key;
    }

    public void RateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public void share(String msg) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = msg;

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

//    public void dialog_info(String message) {
//        ImageView ok, showarea;
//        TextView t1, t2;
//        final Dialog dialog2 = new Dialog(MyActivity.this);
//
//        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog2.setContentView(R.layout.dialog_notsupported);
//
//        ok = (ImageView) dialog2.findViewById(R.id.ok);
//        t1 = (TextView) dialog2.findViewById(R.id.t1);
//        t2 = (TextView) dialog2.findViewById(R.id.t2);
//
//        t1.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_MEDIUM));
//        t2.setTypeface(Typeface.createFromAsset(getAssets(), FontNames.FONT_MEDIUM));
//        t2.setText(message);
//
//        ok.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog2.dismiss();
//            }
//        });
//
//        dialog2.show();
//    }

    public void toastSnack(String message, Snackbar.Callback callback) {

        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
//            if (message.startsWith("error") || message.startsWith("Error")) {
//                tv.setTextColor(Color.RED);
//            } else {
//                tv.setTextColor(Color.GREEN);
//            }
            snackbar.setCallback(callback);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAfterSnack(String message) {
        toastSnack(message, new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                finish();
            }
        });
    }

    public void toastSnack(String message) {
        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
//            if (error) {
//                tv.setTextColor(Color.RED);
//            } else {
//                tv.setTextColor(Color.GREEN);
//            }
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public boolean canToggleGPS() {
        PackageManager pacman = getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                //test if recevier is exported. if so, we can toggle GPS.
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                    return true;
                }
            }
        }

        return false; //default
    }

    public void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    public void buildAlertMessageNoGps() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MyActivity.this);
        builder.setMessage("It is necessary to enable GPS for app features...")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public Bitmap setGlow(int resourceId) {
        Bitmap bmp = null;
        try {
            int margin = 30;
            int halfMargin = margin / 2;

            int glowRadius = 15;

            int glowColor = Color.rgb(0, 0, 0);

            Bitmap src = BitmapFactory.decodeResource(getResources(),
                    resourceId);

            Bitmap alpha = src.extractAlpha();

            bmp = Bitmap.createBitmap(src.getWidth() + margin, src.getHeight()
                    + margin, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bmp);

            Paint paint = new Paint();
            paint.setColor(glowColor);

            paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
            canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);

            canvas.drawBitmap(src, halfMargin, halfMargin, null);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp;
    }

    public class FileUpload_Easy extends AsyncTask<String[], Void, String> {
        ProgressDialog dialog;
        String key = null;
        boolean resizePic = false;
        Map<String, String> stringParams = new HashMap<String, String>();
        private String FILE_PARAMETER = "file";
        @SuppressWarnings("unused")
        private String CONTENT_TYPE = "application/pdf";

        public boolean isResizePic() {
            return resizePic;
        }

        public void setResizePic(boolean resizePic) {
            this.resizePic = resizePic;
        }

        public String getFILE_PARAMETER() {
            return FILE_PARAMETER;
        }

        public void setFILE_PARAMETER(String FILE_PARAMETER) {
            this.FILE_PARAMETER = FILE_PARAMETER;
        }

        public Map<String, String> getStringParams() {
            return stringParams;
        }

        public void setStringParams(Map<String, String> stringParams) {
            this.stringParams = stringParams;
        }

        public String getCONTENT_TYPE() {
            return CONTENT_TYPE;
        }

        public void setCONTENT_TYPE(String CONTENT_TYPE) {
            this.CONTENT_TYPE = CONTENT_TYPE;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            String title = "Please wait for few seconds...";

            dialog = ProgressDialog.show(context, "", title);
        }

        @Override
        protected String doInBackground(String[]... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0][0]);
            String pathToOurFile = params[0][1];

            try {
                MultipartEntity entity = new MultipartEntity();

                if (getStringParams() != null) {
                    for (Map.Entry<String, String> entry : getStringParams().entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        entity.addPart(key, new StringBody((String) value));
                    }
                }

                if (isResizePic()) {
                    // resize the picture
                    String fileName = pathToOurFile.substring(pathToOurFile.lastIndexOf('/') + 1);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    Bitmap bm = BitmapFactory.decodeFile(pathToOurFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, fileName);

                    entity.addPart(getFILE_PARAMETER(), bab);
                } else {
                    // Donot resize the picture
                    log(pathToOurFile,true);
                    log(Environment.getExternalStorageDirectory().getAbsolutePath() + "",true);
                    entity.addPart(getFILE_PARAMETER(), new FileBody(new File(
                            pathToOurFile)));
                }

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);

                Log.e("response from Server", "" + result.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (!isEmpty(getKey()))
                    onTaskComplete(result, getKey());
            } catch (Exception e) {
                toast("Error in connection");
            }

            dialog.dismiss();
        }
    }

}
