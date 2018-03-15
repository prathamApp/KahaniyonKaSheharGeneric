package com.example.pefpr.kahaniyonkashehar;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Ameya on 05-Jan-18.
 */

public class KksApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    static KksApplication kksApplication;
    private static final DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return dateTimeFormat.format(cal.getTime());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        kksApplication = this;
    }

    public static String getLanguage() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(kksApplication);
        String language = statusDBHelper.getValue("AppLang");
        return language;
    }

    public static UUID getUniqueID() {
        return UUID.randomUUID();
    }

    public static KksApplication getInstance() {
        return kksApplication;
    }


}
