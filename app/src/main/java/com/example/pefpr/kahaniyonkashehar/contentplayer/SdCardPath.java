package com.example.pefpr.kahaniyonkashehar.contentplayer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;

/**
 * Created by Ameya on 25-Jan-18.
 */

public class SdCardPath {

    //    String sdCardPath,rootPath;
    static Context context;

    public SdCardPath(Context context) {
        this.context = context;
    }

    public static String getSdCardPath() {
        String sdCardPath = Environment.getExternalStorageDirectory().toString() + "/";

/*        rootPath = PreferenceManager.getDefaultSharedPreferences(KksApplication.getInstance()).getString("PATH","");
        if ((new File(rootPath+"/.KKSGames/").exists()) ) {
            sdCardPath = rootPath+"/";
        } else */

/*        if ( (new File("/storage/sdcard1/.KKSGames/").exists()) ) {
            sdCardPath = "/storage/sdcard1/";
        } else if ( (new File("/storage/usbcard1/.KKSGames/").exists()) ) {
            sdCardPath = "/storage/usbcard1/";
        } else if ((new File("/storage/sdcard0/.KKSGames/").exists()) ) {
            sdCardPath = "/storage/sdcard0/";
        } else if ((new File("/storage/emulated/0/.KKSGames/").exists()) ) {
            sdCardPath = "/storage/emulated/0/";
        }else if ( (new File("/storage/extSdCard/.KKSGames/").exists()) ) {
            sdCardPath = "/storage/extSdCard/";
        }*/

        StatusDBHelper statusDBHelper = new StatusDBHelper(context);
        sdCardPath = statusDBHelper.getValue("SdCardPath");
        Log.d("getSdCardPath", "getSdCardPath: "+sdCardPath);
        sdCardPath = sdCardPath + "/.KKSGames/";
        return sdCardPath;
    }
}
