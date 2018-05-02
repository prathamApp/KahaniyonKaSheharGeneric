package com.example.pefpr.kahaniyonkashehar.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;

public class AppExitService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try {
            StatusDBHelper statusDBHelper = new StatusDBHelper(this);
            SessionDBHelper sessionDBHelper = new SessionDBHelper(this);

            String curSession = statusDBHelper.getValue("CurrentSession");
            String curStrSession = statusDBHelper.getValue("CurrentStorySession");
            sessionDBHelper.UpdateToDate(""+curSession, KksApplication.getCurrentDateTime());
            sessionDBHelper.UpdateToDate(""+curStrSession, KksApplication.getCurrentDateTime());
            BackupDatabase.backup(this);
            Log.d("AppExitService:", "onTaskRemoved: HAHAHAHAHAHAHAHAHAAHAHAAAAAA");
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}