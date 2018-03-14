package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.KksSession;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;

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
    }

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

    public void getStudentUsageData(){
        sessionDbObject = getWritableDatabase();
        Cursor cursor = sessionDbObject.rawQuery("select FirstName,diff from Student x INNER JOIN(select StudentID,sum(seconds) as diff from(select b.StudentID, (strftime('%s',(substr(toDate, 7, 4)||'-'||substr(toDate, 4,2)||'-'||substr(toDate, 1,2)||' '||substr(toDate,11) )) - strftime('%s',(substr(fromDate, 7, 4)||'-'||substr(fromDate, 4,2)||'-'||substr(fromDate, 1,2)||' '||substr(fromDate,11)))) as seconds from Session a left outer join Attendance b on a.SessionID = b.SessionID) group by StudentID) y on x.StudentID = y.StudentID order by diff desc", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Log.d("UsageData :::::", "FirstName: "+cursor.getString(cursor.getColumnIndex("FirstName"))+
                    "  diff: "+cursor.getString(cursor.getColumnIndex("diff"))
            );
            cursor.moveToNext();
        }
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
