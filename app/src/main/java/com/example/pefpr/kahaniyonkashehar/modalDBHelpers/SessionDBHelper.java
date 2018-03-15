package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.KksSession;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pravin on 30/05/2016.
 */

public class SessionDBHelper extends DataBaseHelper {
    Context mContext;
    final String TABLENAME = "Session";

    SQLiteDatabase sessionDbObject;
    ContentValues sessionContentValues;


    public SessionDBHelper(Context context) {
        super(context);
        mContext = context;
        sessionDbObject = getWritableDatabase();
        sessionContentValues = new ContentValues();
    }

    public boolean UpdateToDate(String SessionID, String toDate) {
        String restrue = "f";
        try {
            Cursor cursor= sessionDbObject.rawQuery("select fromDate from " + TABLENAME + " where SessionID = ? ", new String[]{SessionID});
            cursor.moveToFirst();

            String fromDate = cursor.getString(cursor.getColumnIndex("fromDate"));
            Log.d("fromDate", "UpdateToDate: "+fromDate);

            if (fromDate.equalsIgnoreCase("na")) {
                cursor = sessionDbObject.rawQuery("UPDATE " + TABLENAME + " SET toDate = ? where SessionID = ? ", new String[]{toDate, SessionID});
                cursor.moveToFirst();
                if (cursor.getCount() >= 0)
                    restrue = "t";
                else
                    restrue = "f";
            }
            if (restrue.equalsIgnoreCase("t"))
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

/*    public boolean checkUpdatedDate(String SessionID, String toDate) {
        try {
            Cursor cursor = sessionDbObject.rawQuery("UPDATE " + TABLENAME + " SET toDate = ? where SessionID = ? ", new String[]{toDate, SessionID});
            cursor.moveToFirst();

            if (cursor.getCount() >= 0)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }*/

    public boolean addToSessionTable(String SessionID, String fromDate, String toDate) {
        try {
            Cursor cursor = sessionDbObject.rawQuery("Insert into " + TABLENAME + " values(?,?,?)", new String[]{SessionID, fromDate, toDate});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                return true;
            } else return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<KksSession> GetAllSession() {
        try {
            sessionDbObject = getWritableDatabase();
            Cursor cursor = sessionDbObject.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<JSONObject> getStudentUsageData() {
        sessionDbObject = getWritableDatabase();
        Cursor cursor = sessionDbObject.rawQuery("select FirstName,time from Student x INNER JOIN(select StudentID,sum(seconds) as time from(select b.StudentID, (strftime('%s',(substr(toDate, 7, 4)||'-'||substr(toDate, 4,2)||'-'||substr(toDate, 1,2)||' '||substr(toDate,11) )) - strftime('%s',(substr(fromDate, 7, 4)||'-'||substr(fromDate, 4,2)||'-'||substr(fromDate, 1,2)||' '||substr(fromDate,11)))) as seconds from Session a left outer join Attendance b on a.SessionID = b.SessionID) group by StudentID) y on x.StudentID = y.StudentID order by time desc", null);
        cursor.moveToFirst();
        ArrayList<JSONObject> allUsageData = new ArrayList<JSONObject>();
        JSONObject studentData = null;

        try {
            while (!cursor.isAfterLast()) {
                studentData = new JSONObject();
                studentData.put("FirstName", cursor.getString(cursor.getColumnIndex("FirstName")));
                studentData.put("time", cursor.getString(cursor.getColumnIndex("time")));
                allUsageData.add(studentData);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allUsageData;
    }

    private List<KksSession> _PopulateListFromCursor(Cursor cursor) {
        try {
            List<KksSession> sessionsList = new ArrayList<KksSession>();
            KksSession kksSession;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                kksSession = new KksSession();
                kksSession.setSessionID(cursor.getString(cursor.getColumnIndex("SessionID")));
                kksSession.setFromDate(cursor.getString(cursor.getColumnIndex("fromDate")));
                kksSession.setToDate(cursor.getString(cursor.getColumnIndex("toDate")));

                sessionsList.add(kksSession);
                cursor.moveToNext();
            }
            cursor.close();
            sessionDbObject.close();
            return sessionsList;
        } catch (Exception ex) {
            return null;
        }
    }


}
