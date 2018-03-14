package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Attendance;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by PEF-2 on 23/06/2016.
 */
public class AttendanceDBHelper extends DataBaseHelper {

    Context c;
    public static String TABLENAME = "Attendance";
    Cursor cursor;
    SQLiteDatabase attendenceDbObject;
    ContentValues attendenceContentValues;

    public AttendanceDBHelper(Context c) {
        super(c);
        this.c = c;
        attendenceDbObject = getWritableDatabase();
        attendenceContentValues = new ContentValues();
    }


    public boolean add(String sessionID, String studentID) {
        try {
            attendenceDbObject = getWritableDatabase();
            Cursor cursor = attendenceDbObject.rawQuery("Insert into " + TABLENAME + " values(?,?)", new String[]{sessionID, studentID});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                attendenceDbObject.close();
                return true;
            } else {
                attendenceDbObject.close();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public JSONArray getAll() {
        JSONArray jsonArray = null;

        try {
            Cursor cursor = attendenceDbObject.rawQuery("select * from " + TABLENAME + "", null);
            cursor.moveToFirst();
            jsonArray = new JSONArray();
            while (cursor.isAfterLast() == false) {
                JSONObject obj = new JSONObject();
                obj.put("SessionID", cursor.getString(cursor.getColumnIndex("SessionID")));
                obj.put("StudentID", cursor.getString(cursor.getColumnIndex("StudentID")));
                jsonArray.put(obj);
                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonArray;
    }

    public String getCurrenStudentId(String sessionId) {
        String studentID = "";
        try {
            Cursor cursor = attendenceDbObject.rawQuery("select StudentID from " + TABLENAME + " where SessionID ='"+sessionId+"'", null);
            cursor.moveToFirst();
            studentID = cursor.getString(cursor.getColumnIndex("StudentID"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return studentID;
    }

    public boolean deleteAll() {
        try {
            long resultCount = attendenceDbObject.delete(TABLENAME, null, null);
            attendenceDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void _populateContentValues(Attendance attendance) {
        attendenceContentValues.put("SessionID", attendance.getSessionID());
        attendenceContentValues.put("StudentID", attendance.getSudentID());
    }


}
