package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;

/**
 * Created by PEF-2 on 30/05/2016.
 */
public class StatusDBHelper extends DataBaseHelper {
    Context mContext;
    final String TABLENAME = "Status";

    SQLiteDatabase statusDbObject;
    ContentValues statusContentValues;


    public StatusDBHelper(Context context) {
        super(context);
        mContext = context;
        statusContentValues = new ContentValues();
    }


    public void insertInitialData(String key, String value, String desc) {

        try {
            statusDbObject = getWritableDatabase();

            statusContentValues.put("key", key);
            statusContentValues.put("value", value);
            statusContentValues.put("description",desc);

            statusDbObject.replace("Status", null, statusContentValues);
            statusDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // CRL Entry for Created by
    public boolean Update(String keyName, String value) {
        try {
            statusDbObject = getWritableDatabase();
            Cursor cursor = statusDbObject.rawQuery("UPDATE " + TABLENAME + " SET value = ? where key = ? ", new String[]{value, keyName});
            cursor.moveToFirst();

            if (cursor.getCount() >= 0)
                return true;
            else return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getValue(String key) {
        try {
            statusDbObject = getWritableDatabase();
            Cursor cursor = statusDbObject.rawQuery("Select value from " + TABLENAME + " where key = '"+key+"'",null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("value"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean addToStatusTable(String key, int value, String desc) {
        try {
            statusDbObject = getWritableDatabase();
            // Toast.makeText(grpContext,"newGroupCount"+newGroupCount,Toast.LENGTH_LONG).show();
            Cursor cursor = statusDbObject.rawQuery("Insert into " + TABLENAME + " values(?,?,?)", new String[]{key, String.valueOf(value), desc});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                return true;
            } else return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
