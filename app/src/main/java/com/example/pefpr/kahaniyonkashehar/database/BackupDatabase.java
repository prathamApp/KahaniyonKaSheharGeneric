package com.example.pefpr.kahaniyonkashehar.database;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by PEF-2 on 29/10/2015.
 */
public class BackupDatabase {

    public static void backup(Context mContext) {
        try {

            File sd = Environment.getExternalStorageDirectory();

            File file = mContext.getDir("databases", Context.MODE_PRIVATE);
            String currentDBPath = file.getAbsolutePath().replace("app_databases", "databases") + "/KKSdb";
            String backupDBPath = "PrathamKKSTabDB.db";
            File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            backupDB.renameTo(new File("PrathamKKSTabDB.db"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            throw new Error("Copying Failed");
        }
    }

}
