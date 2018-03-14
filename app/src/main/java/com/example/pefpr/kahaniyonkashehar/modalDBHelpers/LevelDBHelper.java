package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Level;

import java.util.ArrayList;
import java.util.List;


public class LevelDBHelper extends DataBaseHelper {
    final String TABLENAME = "Level";
    Context c;
    SQLiteDatabase levelDbObject;
    ContentValues levelContentValues;


    public LevelDBHelper(Context context) {
        super(context);
        c = context;
        levelDbObject = getWritableDatabase();
        levelContentValues = new ContentValues();
    }

    public String GetStudentLevelByStdID(String studentID) {
        try {
            levelDbObject = getWritableDatabase();
            Cursor cursor = levelDbObject.rawQuery("select CurrentLevel from " + TABLENAME + " where StudentID = ? ", new String[]{studentID});
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("CurrentLevel"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Level> GetAllLevelData(){
        try {
            levelDbObject = getWritableDatabase();
            Cursor cursor = levelDbObject.rawQuery("SELECT * FROM " + TABLENAME, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                   return _PopulateListFromCursor(cursor);
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean CheckChildLevelExists(String stdID) {
        try {
            levelDbObject = getWritableDatabase();

            Cursor cursor = levelDbObject.rawQuery("SELECT * FROM " + TABLENAME + " WHERE StudentId =?", new String[]{stdID});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateStudentLevel(String studentID, float level) {
        try {
            String sLevel = String.valueOf(level);
            //double newLevel = (double) level;
            levelContentValues = new ContentValues();
            levelContentValues.put("CurrentLevel",sLevel);
            levelDbObject.update(TABLENAME,levelContentValues,"StudentID='"+studentID+"'",null);
//            Cursor cursor = levelDbObject.rawQuery("UPDATE " + TABLENAME + " SET CurrentLevel = '" + sLevel + "' WHERE StudentID = '" + studentID +"'",null);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Add(Level level, SQLiteDatabase writableDatabase) {
        try {
            _PopulateContentValues(level);
            long resultCount = writableDatabase.insert(TABLENAME, null, levelContentValues);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean DeleteSpecificStudent(String studentID) {
        try {
            levelDbObject = getWritableDatabase();
            long resultCount = levelDbObject.delete(TABLENAME, "StudentID = ?", new String[]{studentID});
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            levelDbObject = getWritableDatabase();
            long resultCount = levelDbObject.delete(TABLENAME, null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void _PopulateContentValues(Level level) {
        levelContentValues = new ContentValues();
        levelContentValues.put("StudentID", level.StudentID);
        levelContentValues.put("CurrentLevel", String.valueOf(level.CurrentLevel));
        levelContentValues.put("BaseLevel", String.valueOf(level.BaseLevel));
    }

    private Level _PopulateObjectFromCursor(Cursor cursor) {
        try {
            levelDbObject = getWritableDatabase();
            Level level = new Level();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                level.StudentID = cursor.getString(cursor.getColumnIndex("StudentID"));
                level.BaseLevel = cursor.getString(cursor.getColumnIndex("BaseLevel"));
                level.CurrentLevel = cursor.getString((cursor.getColumnIndex("CurrentLevel")));
                cursor.moveToNext();
            }
            cursor.close();

            return level;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Level> _PopulateListFromCursor(Cursor cursor) {
        try {
            List<Level> levels = new ArrayList<Level>();
            Level level;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                level = new Level();
                level.StudentID = cursor.getString(cursor.getColumnIndex("StudentID"));
                level.BaseLevel = cursor.getString(cursor.getColumnIndex("BaseLevel"));
                level.CurrentLevel = cursor.getString((cursor.getColumnIndex("CurrentLevel")));
                levels.add(level);
                cursor.moveToNext();
            }
            cursor.close();
            return levels;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}