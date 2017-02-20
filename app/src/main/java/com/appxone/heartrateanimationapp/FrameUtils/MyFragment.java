package com.appxone.heartrateanimationapp.FrameUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.appxone.heartrateanimationapp.Utils.PSStrings;

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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class MyFragment extends Fragment {

    Fragment fragment;

    Activity context;

    protected boolean isUserLoggedIn = false;
    protected boolean isUserFromFb = false;

    // For GPS
    LocationManager lm;
    Location location;

    protected boolean allowSynch = true;

    public static String currentLanguage = "";

    private CommonRequestFragment commonRequest;
    // Loggin Store
    public static final String KEY_LOGIN_URL = "login_url";
    public static final String KEY_LOGIN_STATUS = "login_status";
    public static final String KEY_LANGUAGE = "language_selected";
    public static final String KEY_CHECKBOX = "chk";

    static SharedPreferences myPrefSpot;
    static SharedPreferences.Editor myEditSpot;

    public static final String KEY_THEMETYPE = "THEMETYPE";
    public static final String KEY_APPLICATONTYPE = "APPLICATIONTYPE";
    public static final String KEY_LOGMODE = "LOGMODE";
    public static final String KEY_PACKAGENAME = "PACKAGENAME";

    // All
    public static final String KEY_USER_ID = "user_id"; // set(login) ,get (all
    public static final String KEY_USER_NAME = "username"; // webservices)
    public static final String KEY_SESSION_ID = "session_id"; // set(login) ,get
    // (allwebservices)
    public static final String KEY_GENDER_ME = "gender_me";
    public static final String KEY_GENDER_USER = "gender_user";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DISTANCE_PARAMETER = "distance_parameter";
    public static final String KEY_AGE = "age";
    public static final String KEY_MESSAGE_SETTING = "message";

    public static final String KEY_LATITUDE = "user_latitude"; // set(checking,

    public static final String KEY_LONGITUDE = "user_longitude"; // set(checkin,

    public static final String KEY_SCREEN_WIDTH = "screenWidth";
    public static final String KEY_SCREEN_HEIGHT = "screenHeight";
    public static final String KEY_DIFFERENCE_DATE_TIME = "difference_DateTime";

    // Loggin Store
    public static final String KEY_LOGIN_EMAIL = "login_email";
    public static final String KEY_LOGIN_PASSWORD = "login_password";

    private static final int MODE_PRIVATE = 0;

    public MyFragment() {
        super();

    }

    //    public void sessionExpiredLogout() {
//        writeString(PSStrings.USERID, "");
//        writeString(PSStrings.USERKEY, "");
//        writeString(PSStrings.SELECTEDGROUPID, "");
//        writeString(PSStrings.SELECTEDGROUPNAME, "");
//        writeBoolean(PSStrings.ISLOGGEDIN, false);
//        toast("Session Expired");
//        Intent i = new Intent(getActivity(), Login.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        getActivity().finish();
//    }
//    public void sessionExpiredLogout() {
//        writeString(PSStrings.USER_ID, "");
//        writeString(PSStrings.SESSION_ID, "");
//        writeBoolean(PSStrings.LOGGED, false);
//        toast("Session Expired");
//        Intent i = new Intent(getActivity(), Landing.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        getActivity().finish();
//    }

    public void hideSoftKeyboard() {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void hitUrl(String key, String url) {
        hitUrl(key, url, "");
    }

    public void hitUrl(String key, String url, String progressTitle) {
        commonRequest = new CommonRequestFragment(context);
        commonRequest.setActivity(this);
        commonRequest.setUrl(url);
        commonRequest.setKey(key);
        commonRequest.setProgressTitle(progressTitle);
        commonRequest.execute("");
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

    public abstract void onTaskComplete(String result, String key);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myPrefSpot = getActivity().getSharedPreferences(StringUtils.PREFS_NAME,
                MODE_PRIVATE);
        myEditSpot = myPrefSpot.edit();

        context = getActivity();
        // setSharedPreferences(getSharedPreferences(StringUtils.PREFS_NAME,0));
        // setCurrentLanguage(getSavedLanguage());

        getPrefs();
        super.onCreate(savedInstanceState);
    }

    public static String getKeyLoginEmail() {
        return getString(KEY_LOGIN_EMAIL);
    }

    public static String getKeyLoginPassword() {
        return getString(KEY_LOGIN_PASSWORD);
    }

    public static String getApplicationTheme() {
        return getString(KEY_THEMETYPE);
    }

    public static String getApplicationType() {
        return getString(KEY_APPLICATONTYPE);
    }

    public static String getAppPackageName() {
        return getString(KEY_PACKAGENAME);
    }

    public static String getLogMode() {
        return getString(KEY_LOGMODE);
    }

    public static void writeString(String key, String value) {
        myEditSpot.putString(key, value);
        myEditSpot.commit();
    }

    public static void writeInt(String key, int value) {
        myEditSpot.putInt(key, value);
        myEditSpot.commit();
    }

    public int getDurationFromCredit() {
        float currentbalance;
        if (getString("currentcredit").equals(""))
            currentbalance = Float.parseFloat("0.0");
        else
            currentbalance = Float.parseFloat(getString("currentcredit"));
//        float currentbalance = Float.parseFloat("1.4");
        currentbalance = currentbalance - 1;
        int duration = (int) (currentbalance * 2);
        return duration;
    }


    public static void writeBoolean(String key, Boolean value) {
        myEditSpot.putBoolean(key, value);
        myEditSpot.commit();
    }

    public static String getString(String key) {

        return myPrefSpot.getString(key, "");

    }

    public static int getInt(String key) {
        return myPrefSpot.getInt(key, 0);
    }

    public static Boolean getBoolean(String key) {
        return myPrefSpot.getBoolean(key, false);
    }

    public static String getUser_Id() {
        return getString(KEY_USER_ID);
    }

    public static String getMessageSetting() {
        return getString(KEY_MESSAGE_SETTING);
    }

    public static String getSession_Id() {
        return getString(KEY_SESSION_ID);
    }

    public static String getGender_Me() {
        return getString(KEY_GENDER_ME);
    }

    public static String getGender_User() {
        return getString(KEY_GENDER_USER);
    }

    public static String getDistance() {
        return getString(KEY_DISTANCE);
    }

    public static String getDistance_Parameter() {
        return getString(KEY_DISTANCE_PARAMETER);
    }

    public static String getLatitude() {
        return getString(KEY_LATITUDE);
    }

    public static String getLongitude() {
        return getString(KEY_LONGITUDE);
    }

    public static String getUsername() {
        return getString(KEY_USER_NAME);
    }

    public static int getScreenWidth() {
        return getInt(KEY_SCREEN_WIDTH);
    }

    public static int getScreenHeight() {
        return getInt(KEY_SCREEN_HEIGHT);
    }

    public static String getDateTime() {
        return getString(KEY_DIFFERENCE_DATE_TIME);
    }

    public static String getAge() {
        return getString(KEY_AGE);
    }

    @Override
    public void onPause() {

        allowSynch = false;
        if (null != commonRequest) {
            try {
                commonRequest.cancel(true);
            } catch (Exception e) {
            }
        }
        super.onPause();
    }

    protected void confirmation(String title, String message,
                                DialogInterface.OnClickListener oKListener,
                                DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_menu_help).setTitle(title)
                .setMessage(message).setPositiveButton("OK", oKListener)
                .setNegativeButton("Cancel", cancelListener).show();
    }

    protected void confirmation(String title, String message,
                                DialogInterface.OnClickListener oKListener) {
        confirmation(title, message, oKListener, null);
    }

    // @SuppressWarnings("unused")
    // public void goAdd() {
    //
    // Rated.log("Trying Add", "ADD");
    //
    // AdView adView = null;
    // RelativeLayout adlayout;
    // AdRequest request;
    // String publisherId = "ca-app-pub-3518149436592966/7997499134";
    // // String testingDeviceId = "359918043312594";
    //
    // adlayout = (RelativeLayout) context.findViewById(R.id.adLayout);
    // // Get Screen
    // Display display = ((WindowManager) context
    // .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    // int screenWidth = display.getWidth();
    // log("Screen Width", "" + screenWidth);
    //
    // // AdMob Size Initialisations
    // if (screenWidth > 300 && screenWidth <= 320)
    // adView = new AdView(context, AdSize.BANNER, publisherId);
    // else {
    //
    // adView = new AdView(context, AdSize.SMART_BANNER, publisherId);
    // }
    //
    // adlayout.addView(adView);
    //
    // // AdMob Request
    // request = new AdRequest();
    //
    // // only for testing Devices
    // // request.addTestDevice(AdRequest.TEST_EMULATOR);
    // // request.addTestDevice(testingDeviceId);
    //
    // // load Ad
    // adView.loadAd(request);
    // }

    protected void warning(String title, String message,
                           OnClickListener onClickListener) {
        warning(title, message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    protected void warning(String title, String message,
                           DialogInterface.OnClickListener oKListener) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(message).setPositiveButton("OK", oKListener).show();
    }

    public void toastSnack(String message, Snackbar.Callback callback) {

        try {
            Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT);
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
                context.finish();
            }
        });
    }

    public void toastSnack(String message) {
        try {
            Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT);
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

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void toast(int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void getScreenProperties() {
        // finding density
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);

        writeInt(KEY_SCREEN_WIDTH, displayMetrics.widthPixels);
        writeInt(KEY_SCREEN_HEIGHT, displayMetrics.heightPixels);
        log("screen width", "" + KEY_SCREEN_WIDTH);
        log("screen height", "" + KEY_SCREEN_HEIGHT);
    }

    //    public void getCoordiatesByGPS() {
//
//        lm = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//
//        Criteria criteria = new Criteria();
//        String bestProvider = lm.getBestProvider(criteria, false);
//        location = lm.getLastKnownLocation(bestProvider);
//
//        if (location != null) {
//
//            writeString(KEY_LATITUDE, "" + location.getLatitude());
//            writeString(KEY_LONGITUDE, "" + location.getLongitude());
//
//        } else {
//            showSettingsAlert();
//
//        }
//
//    }
    public boolean canToggleGPS() {
        PackageManager pacman = context.getPackageManager();
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
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public void buildAlertMessageNoGps() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage("It is necessary to enable GPS for app features...")
                .setCancelable(true)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
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
                        context.finish();
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    // Getting path of image
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public String getVideoPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Drawable getDrawable(Context ctx, String Name) {
        context = (Activity) ctx;
        final int imageResource = context.getResources().getIdentifier(
                "@drawable/" + Name, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        return image;
    }

    String changeOfString(String str) {
        if (str.equals("About average")) {
            return "about_average";
        } else if (str.equals("Big and Beautiful")) {

            return "big_and_beautiful";
        } else if (str.equals("Full-figured")) {
            return "full_figured";
        } else if (str.equals("Never Married")) {
            return "never_married";
        } else if (str.equals("Widow/Widower")) {
            return "widow_widower";
        } else if (str.equals("Athletic and toned")) {
            return "athletic_and_toned";
        } else if (str.equals("A few extra pounds")) {
            return "a_few_extra_pound";
        } else if (str.equals("Currently Separated")) {
            return "currently_separated";
        } else if (str.equals("Slender")) {
            return "slender";
        } else if (str.equals("Heavyset")) {
            return "heavy_set";
        } else if (str.equals("Curvy")) {
            return "curvy";
        } else if (str.equals("Divorced")) {
            return "divorced";
        }
        return str;
    }

    public static Typeface setTypeFace_Auto(Context ctx, TextView tv) {

        Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),
                "newFont.ttf");
        return custom_font;
    }

    public static String getSavedLanguage() {
        String lang = getString(KEY_LANGUAGE);
        try {
            if (lang.equals("") || lang.equals(null)) {
                writeString(KEY_LANGUAGE, StringUtils.LANGUAGE_ARABIC);
                return StringUtils.LANGUAGE_ARABIC;
            } else
                return lang;
        } catch (Exception e) {
            // TODO: handle exception
            return StringUtils.LANGUAGE_ARABIC;
        }
    }

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    public static void setCurrentLanguage(String currentLanguage) {
        MyFragment.currentLanguage = currentLanguage;
        writeString(KEY_LANGUAGE, currentLanguage);
    }

    public EditText setMyGravity1(EditText edt, String hint) {
        EditText temp = edt;

        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            temp.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            temp.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        temp.setHint(hint);

        return temp;
    }

    public void setMyGravity(EditText edt, String hint) {
        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            edt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        edt.setHint(hint);
    }

    public void setMyGravity(EditText edt, int hint) {
        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            edt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        edt.setHint(getString(hint));
    }

    public void setEditTextGravity(EditText edt, String hint) {
        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            edt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        edt.setHint(hint);
    }

    public void setEditTextGravity(EditText edt, int hint) {
        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            edt.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        edt.setHint(getString(hint));
    }

    public void setTextViewGravity(TextView tv, int text) {
        if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
            tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        } else {
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        tv.setText(getString(text));
    }

    public String getText(EditText edt) {
        String temp = "";

        temp = edt.getText().toString();
        if (!TextUtils.isEmpty(temp.trim())) {
            return temp;
        } else {
            if (getCurrentLanguage().equals(StringUtils.LANGUAGE_ARABIC)) {
                edt.setError("خالية");
            } else {
                edt.setError("Empty");
            }
            return "";
        }
    }

    public String getText(Spinner sp) {
        String temp = "";

        temp = sp.getSelectedItem().toString();
        if (!TextUtils.isEmpty(temp.trim())) {
            return temp;
        } else {
            return "";
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

    public void setText(EditText edt, String text) {
        if (!isEmpty(text)) {
            edt.setText(text);
        }
    }

    public String getSessionString() {
        return getString(StringUtils.PREF_LOGIN_SESSION);
    }

    protected void onSessionExpire(final int currentTab) {
        if (currentLanguage.equals(StringUtils.LANGUAGE_ARABIC))
            Toast.makeText(
                    getActivity(),
                    "وقد انتهت جلسة العمل الخاصة بك، الرجاء تسجيل الدخول مرة أخرى.",
                    Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getActivity(),
                    "Your session has expired, Please login again.",
                    Toast.LENGTH_LONG).show();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // Intent i = new Intent(getActivity(), LoginScreen.class);
                // i.putExtra("tab_load", currentTab);
                // startActivity(i);
                // getActivity().finish();
            }
        }, Toast.LENGTH_LONG);
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
            log("" + getActivity().getClass().getSimpleName(), "" + text);
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
        Intent i = new Intent(getActivity(), classToLoad);
        startActivity(i);
        if (finishCurrentAct) {
            getActivity().finish();
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
            Intent i = new Intent(getActivity(), classToLoad);
            i.putExtras(b);
            startActivity(i);
            if (finishCurrentAct) {
                getActivity().finish();
            }
        } else {
            startMyActivity(classToLoad, finishCurrentAct);
        }
    }

    protected boolean isUerLoggedIn() {
        // TODO Auto-generated method stub
        String isLoggedIn = getString(StringUtils.PREF_IS_LOGIN);

        if (isLoggedIn.equals("") || isLoggedIn.equals(null))
            return false;
        else
            return true;
    }

    public String UTF_encode(String value) {
        String converted = null;
        try {
            converted = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return converted;
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
                    log(pathToOurFile, true);
                    log(Environment.getExternalStorageDirectory().getAbsolutePath() + "", true);
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

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);


        }
    }
}
