package com.appxone.heartrateanimationapp.FrameUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class MyBaseAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    Activity context;

    // For GPS
    LocationManager lm;
    Location location;

    boolean isUserLoggedIn = false;
    boolean isUserFromFb = false;

    protected boolean allowSynch = true;

    private CommonRequest_BaseAdapter commonRequest;

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

    public MyBaseAdapter(Activity ctx) {
        super();
        context = ctx;

        myPrefSpot = ctx.getSharedPreferences(StringUtils.PREFS_NAME,
                Context.MODE_PRIVATE);
        myEditSpot = myPrefSpot.edit();

        getPrefs();
    }

    public void share(String textToShare) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare + " ");
            sendIntent.setType("text/plain");
            this.context.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyBaseAdapter(Activity ctx, ArrayList<?> items) {
        // TODO Auto-generated constructor stub
        super();
        try {
            context = ctx;

            myPrefSpot = ctx.getSharedPreferences(StringUtils.PREFS_NAME,
                    Context.MODE_PRIVATE);
            myEditSpot = myPrefSpot.edit();

            getPrefs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public String getSessionID() {
//        return getString(StringUtils.PREF_USER_SESSION);
//    }

//    public void sessionExpiredLogout() {
//        writeString(PSStrings.USER_ID, "");
//        writeString(PSStrings.SESSION_ID, "");
//        writeBoolean(PSStrings.LOGGED, false);
//        toast("Session Expired");
//        Intent i = new Intent(context, Landing.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(i);
//        context.finish();
//    }

    public String getAmountWithCommas(String price) {
        return new DecimalFormat("#,###").format(Double.parseDouble(price));
    }

    public String getUserId() {
        return getString(StringUtils.PREF_USER_USER_ID);
    }

    protected void hitUrl(String key, String url) {
        hitUrl(key, url, "");
    }

    protected void hitUrl1(final String key, final String url) {
        if (isInternetConnected()) {
            // TODO Auto-generated method stub
            commonRequest = new CommonRequest_BaseAdapter(context, this);
            commonRequest.setActivity(this);
            commonRequest.setUrl(url);
            commonRequest.setKey(key);
            commonRequest.execute("");
        } else {
            toast("No Internet Connected. Please check your internet connection and try again.");
        }
    }

    protected void hitUrl(final String key, final String url,
                          final String progressTitle) {
        if (isInternetConnected()) {
            // TODO Auto-generated method stub
            commonRequest = new CommonRequest_BaseAdapter(context, this);
            commonRequest.setActivity(this);
            commonRequest.setUrl(url);
            commonRequest.setKey(key);
            commonRequest.setProgressTitle(progressTitle);
            commonRequest.execute("");
        } else {
            toast("No Internet Connected. Please check your internet connection and try again.");
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

    public static void writeString2(Context ctx, String key, String value) {
        SharedPreferences myPrefSpot = ctx.getSharedPreferences(
                StringUtils.PREFS_NAME, Context.MODE_PRIVATE);
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

    public static String getString(String key) {
        return myPrefSpot.getString(key, "");
    }

    public static String getString2(Context ctx, String key) {
        SharedPreferences myPrefSpot = ctx.getSharedPreferences(
                StringUtils.PREFS_NAME, Context.MODE_PRIVATE);
        return myPrefSpot.getString(key, "");
    }

    public static int getInt(String key) {
        return myPrefSpot.getInt(key, 0);
    }

    public static Boolean getBoolean(String key) {
        return myPrefSpot.getBoolean(key, false);
    }

    public abstract void onTaskComplete(String result, String key);

    private void getPrefs() {
        // TODO Auto-generated method stub
        isUserLoggedIn = getBoolean(StringUtils.PREF_IS_LOGIN);
    }

    public static void writeString(String key, String value) {
        myEditSpot.putString(key, value);
        myEditSpot.commit();
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

    protected void warning(String title, String message,
                           OnClickListener onClickListener) {

        warning(title, message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    protected void warning(String title, String message) {
        new AlertDialog.Builder(context)
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
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
                .setMessage(message).setPositiveButton("OK", oKListener).show();
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void toast(int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public boolean isEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isPhone(String phone) {
        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phone).matches();
    }

    public boolean isAge(String str_Age) {
        int age = Integer.parseInt(str_Age);
        if (age > 1 && age < 150) {
            return true;
        } else
            return false;
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

    public static boolean isSDCardAvailable() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);

        return isSDPresent;
    }

    public void log(int text) {
        log("" + text);
    }

    public void log(long text) {
        log("" + text);
    }

    public void log(double text) {
        log("" + text);
    }

    public void log(float text) {
        log("" + text);
    }

    public void log(String text) {
        log("" + context.getClass().getSimpleName(), "" + text);
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
        Intent i = new Intent(context, classToLoad);
        context.startActivity(i);
        if (finishCurrentAct) {
            context.finish();
        }
    }

    public void startMyActivity(Class<?> classToLoad, boolean finishCurrentAct,
                                boolean clearTop) {
        Intent i = new Intent(context, classToLoad);
        if (clearTop) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(i);
        if (finishCurrentAct) {
            context.finish();
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
            Intent i = new Intent(context, classToLoad);
            i.putExtras(b);
            context.startActivity(i);
            if (finishCurrentAct) {
                context.finish();
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
        if (!isEmpty(old)) {
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
    public static void showProgress(Context context, final boolean show,
                                    final View mProgressView, final View mFormView) {
        Log.e("show progress", "show progress " + show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try {
            if (hasHoneycombMR2()) {
                int shortAnimTime = context.getResources().getInteger(
                        android.R.integer.config_shortAnimTime);

                mProgressView.setVisibility(View.VISIBLE);
                mProgressView.animate().setDuration(shortAnimTime)
                        .alpha(show ? 1 : 0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mProgressView.setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                            }
                        });

                mFormView.setVisibility(View.VISIBLE);
                mFormView.animate().setDuration(shortAnimTime)
                        .alpha(show ? 0 : 1)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mFormView.setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                            }
                        });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply
                // show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        } catch (Exception e) {
            // TODO: handle exception
            // e.printStackTrace();
        }
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= 13;
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
}
