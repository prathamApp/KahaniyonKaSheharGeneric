package com.example.pefpr.kahaniyonkashehar.util;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;

/**
 * Created by Ameya on 15-Mar-18.
 */

public class BackgroundManagement extends Activity{
    Context context;
    static CountDownTimer cd;
    static Long timeout = (long) 2000 * 60;
    static Long duration = timeout;
    Boolean setTimer=false;

    public BackgroundManagement(Context context) {
        this.context = context;
    }

    public void ActivityOnPause() {

        setTimer = true;
        Log.d("APP_END", "onFinish: Startd the App: "+duration);

        cd = new CountDownTimer(duration, 1000) {
            //cd = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                duration = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                try {
                    Log.d("APP_END", "onFinish: Ended the App: "+KksApplication.getCurrentDateTime());

                    StatusDBHelper statusDBHelper = new StatusDBHelper(context);
                    SessionDBHelper sessionDBHelper = new SessionDBHelper(context);

                    String curSession = statusDBHelper.getValue("CurrentSession");
                    Log.d("APP_END", "onFinish: Current Session: "+curSession);
                    Boolean temp = sessionDBHelper.UpdateToDate("" + curSession, KksApplication.getCurrentDateTime());
                    if(temp)
                        Log.d("APP_END", "onFinish: SUCCESS");
                    BackupDatabase.backup(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void ActivityResumed() {
            setTimer = false;
            cd.cancel();
            duration = timeout;
        Log.d("APP_END", "onFinish: duration: "+duration);

    }
}