package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.interfaces.PermissionResult;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.services.AppExitService;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;
import com.example.pefpr.kahaniyonkashehar.util.PermissionUtils;
import com.example.pefpr.kahaniyonkashehar.util.SDCardUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements PermissionResult {

    @BindView(R.id.start_app)
    TextView startApp;
    StatusDBHelper statusDBHelper;
    public static final int RequestPermissionCode = 1;
    int pressCount = 0;
    String final_sd_path, sdCardPathString;

    //android debug
//    debugImplementation 'com.amitshekhar.android:debug-db:1.0.3'


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, SplashActivity.this);
        getSupportActionBar().hide();
        startService(new Intent(this, AppExitService.class));

        String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA,
//                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_RECORD_AUDIO,
/*                PermissionUtils.Manifest_BLUETOOTH,
                PermissionUtils.Manifest_BLUETOOTH_ADMIN,*/
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
//                PermissionUtils.Manifest_ACCESS_FINE_LOCATION
        };

        if (!isPermissionsGranted(SplashActivity.this, permissionArray)) {
            askCompactPermissions(permissionArray, this);
        }

        startApp.setTextColor(Color.WHITE);
        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(Environment.getExternalStorageDirectory().toString() + "/.KKSInternal");
                if (!file.exists())
                    file.mkdir();

                file = new File(Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/UsageJsons");
                if (!file.exists())
                    file.mkdir();

                file = new File(Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/SelfUsageJsons");
                if (!file.exists())
                    file.mkdir();

                file = new File(Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/JsonsBackup");
                if (!file.exists())
                    file.mkdir();

                file = new File(Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/Recordings");
                if (!file.exists())
                    file.mkdir();

                BackupDatabase.backup(SplashActivity.this);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            startUpProcess();
        }

    }

    public String fetchStory(String jasonName) {
        String appLang = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString + "/JsonFiles/" + jasonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObj = new JSONObject(new String(buffer));
            appLang = jsonObj.get("nodeTitle").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appLang;
    }


    @Override
    public void onBackPressed() {
        if (pressCount >= 1) {
            this.finish();
        } else {
            Toast.makeText(this, "Exit App", Toast.LENGTH_SHORT).show();
            pressCount++;
        }
    }

    public void intiateDatabase() {
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
//            myDbHelper.openDataBaseAndModify();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new Error("Unable to create database");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void startUpProcess() {

        try {
            intiateDatabase();

            statusDBHelper = new StatusDBHelper(SplashActivity.this);
            String extSdPath = statusDBHelper.getValue("SdCardPath");
            Log.d("extSdPath", "permissionGranted: " + extSdPath);

            String inserted = statusDBHelper.getValue("insertedStudents");

            ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(SplashActivity.this);
            for (String path : sdcard_path) {
                final_sd_path = path;
                if (new File(final_sd_path + "/.KKSGames").exists()) {
                    statusDBHelper.Update("SdCardPath", path);
                    sdCardPathString = path + "/.KKSGames";
                    if (!inserted.equalsIgnoreCase("y"))
                        new DataBaseHelper(this).insertDataFromStudentJSON();
                    break;
                }
            }

            String kksLang = fetchStory("Stories");
            Log.d("LanguageJson", "onCreate: " + kksLang);
            statusDBHelper.Update("AppLang", kksLang);
            BackupDatabase.backup(SplashActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void permissionGranted() {
        Log.d("permission", "permissionGranted: ");
        startUpProcess();
    }

    @Override
    public void permissionDenied() {

    }

    @Override
    public void permissionForeverDenied() {

    }
}