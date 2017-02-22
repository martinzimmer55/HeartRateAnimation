package com.appxone.heartrateanimationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.appxone.heartrateanimationapp.FrameUtils.MyActivity;

import java.io.IOException;

public class Splash extends MyActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        proceedtoActivity();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (isRecordAudioPermissionGranted()) {
//                proceedtoActivity();
//            }
//
//
//        } else if (Build.VERSION.SDK_INT < 23) {
//            proceedtoActivity();
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[requestCode] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Permission: ", "Permission: " + permissions[requestCode] + "was " + grantResults[requestCode]);
            proceedtoActivity();
            //resume tasks needing this permission
        } else {
            toast("Please grant all permissions to proceed");
            isRecordAudioPermissionGranted();
        }
    }

    public boolean isRecordAudioPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission is granted", "Permission is granted");
                return true;
            } else {

                Log.v("Permission is revoked", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission is granted", "Permission is granted");
            return true;
        }

    }

    public void proceedtoActivity() {
        CopyOfDataBaseManager db = new CopyOfDataBaseManager(getApplicationContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMyActivity(Home.class);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onTaskComplete(String result, String key) {

    }
}
