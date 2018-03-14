package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreDBHelper extends DataBaseHelper {
    final String TABLENAME = "Scores";
    SQLiteDatabase scoreDbObject;
    ContentValues scoreContentValues;

    Context context;

    public ScoreDBHelper(Context context) {
        super(context);
        this.context = context;
        scoreDbObject = this.getWritableDatabase();
        scoreContentValues = new ContentValues();
    }


    public int GetPlayedResourcesCount() {
        try {
            Cursor cursor = scoreDbObject.rawQuery("select distinct ResourceID from " + TABLENAME + "", null);
            int cursorCount = cursor.getCount();
            return cursorCount;
        } catch (Exception ex) {
            return 0;
        }

    }

    public String GetPlayedResources() {
        String resourceList = "";
        try {
            Cursor cursor = scoreDbObject.rawQuery("select distinct ResourceID from " + TABLENAME + "", null);
            int cursorCount = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < cursorCount; i++) {
                resourceList += cursor.getString(cursor.getColumnIndex("ResourceID")) + ",";
                cursor.moveToNext();
            }
            return resourceList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resourceList;
    }


   /* public List<ScoreList> GetTotalUsage() {
        try {
            scoreDbObject = getWritableDatabase();
            List<ScoreList> list = new ArrayList<ScoreList>();
            {
                Cursor cursor = scoreDbObject.rawQuery("SELECT StartDateTime,EndDateTime FROM Scores", null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {

                    list.add(new ScoreList(cursor.getString(cursor.getColumnIndex("StartDateTime")), cursor.getString(cursor.getColumnIndex("EndDateTime"))));

                    cursor.moveToNext();
                }
                scoreDbObject.close();

            }
            return list;

        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetTotalUsage");
            return null;
        }
    }*/


    public boolean Add(Score score) {
        try {
            scoreDbObject = this.getWritableDatabase();
            _PopulateContentValues(score);
            long resultCount = scoreDbObject.insert(TABLENAME, null, scoreContentValues);
            scoreDbObject.close();
            if (resultCount == -1)
                return false;
            else
                return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            // scoreDbObject = getWritableDatabase();
            long resultCount = scoreDbObject.delete(TABLENAME, null, null);
            scoreDbObject.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean Update(Score score) {
        return false;
    }

    public boolean Delete(int scoreID) {
        return true;
    }

    public Score Get(int scoreID) {
        Score score = new Score();
        return score;
    }

    public List<Score> GetAll() {
        try {
            Cursor cursor = scoreDbObject.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            return null;
        }
    }

    private void _PopulateContentValues(Score score) {

        scoreContentValues.put("SessionID", score.getSessionID());
        scoreContentValues.put("StudentID", score.getStudentID());
        scoreContentValues.put("DeviceID", score.getDeviceID());
        scoreContentValues.put("QuestionID", score.getQuestionId());
        scoreContentValues.put("ResourceID", score.getResourceID());
        scoreContentValues.put("ScoredMarks", score.getScoredMarks());
        scoreContentValues.put("TotalMarks", score.getTotalMarks());
        scoreContentValues.put("StartDateTime", score.getStartDateTime());
        scoreContentValues.put("EndDateTime", score.getEndDateTime());
        scoreContentValues.put("Level", score.getLevel());
    }

    private List<Score> _PopulateListFromCursor(Cursor cursor) {
        try {
            List<Score> scoreList = new ArrayList<Score>();
            Score score;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                score = new Score();
                score.setStudentID(cursor.getString(cursor.getColumnIndex("StudentID")));
                score.setSessionID(cursor.getString(cursor.getColumnIndex("SessionID")));
                score.setDeviceID(cursor.getString(cursor.getColumnIndex("DeviceID")));
                score.setResourceID(cursor.getString(cursor.getColumnIndex("ResourceID")));
                score.setQuestionId(cursor.getInt(cursor.getColumnIndex("QuestionID")));
                score.setScoredMarks(cursor.getInt(cursor.getColumnIndex("ScoredMarks")));
                score.setTotalMarks(cursor.getInt(cursor.getColumnIndex("TotalMarks")));
                score.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
                score.setStartDateTime(cursor.getString(cursor.getColumnIndex("StartDateTime")));
                score.setEndDateTime(cursor.getString(cursor.getColumnIndex("EndDateTime")));

                scoreList.add(score);
                cursor.moveToNext();
            }
            cursor.close();
            scoreDbObject.close();
            return scoreList;
        } catch (Exception ex) {
            return null;
        }
    }

    /*
    *
    * */

    public List<Score> GetAllGroupScore() {
        try {
            Cursor cursor;
            scoreDbObject = getWritableDatabase();
//            cursor = scoreDbObject.rawQuery("select ResourceID, SUM(ScoredMarks) as ScoredMarks, SUM(TotalMarks) as TotalMarks from Scores where GroupID in ("+groupID+") and Level != 99", null);
            cursor = scoreDbObject.rawQuery("select distinct GroupID from Scores where Level != 99", null);

            return PopulateScoreListFromCursor(cursor);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Score> PopulateScoreListFromCursor(Cursor cursor) {
        try {
            List<Score> ScoresList = new ArrayList<Score>();
            Score score;
            cursor.moveToFirst();
            int count = cursor.getCount();

            while (cursor.isAfterLast() == false) {

                score = new Score();

                score.setStudentID(cursor.getString((cursor.getColumnIndex("StudentID"))));

                ScoresList.add(score);

                cursor.moveToNext();
            }
            cursor.close();
            return ScoresList;
        } catch (Exception ex) {
            return null;
        }
    }


    public List<Score> GetScoreForReports(String GroupID, String resIDs, String dp_FromDateText, String dp_ToDateText) {
        try {
            Cursor cursor;
            scoreDbObject = getWritableDatabase();
            cursor = scoreDbObject.rawQuery("select SUM(ScoredMarks) as ScoredMarks, SUM(TotalMarks) as TotalMarks from Scores where (StartDateTime and EndDateTime between '" + dp_FromDateText + "' and '" + dp_ToDateText + "') and GroupID = '" + GroupID + "' and ResourceID in (" + resIDs + ")", null);
            return PopulateGroupScoresFromCursor(cursor);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Score> GetAkaScoreForReports(String GroupID, String resIDs) {
        try {
            Cursor cursor;
            scoreDbObject = getWritableDatabase();
            cursor = scoreDbObject.rawQuery("select SUM(ScoredMarks) as ScoredMarks, SUM(TotalMarks) as TotalMarks from Scores where GroupID = '" + GroupID + "' and ResourceID in (" + resIDs + ")", null);
            return PopulateGroupScoresFromCursor(cursor);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Score> PopulateGroupScoresFromCursor(Cursor cursor) {
        try {
            List<Score> ScoresList = new ArrayList<Score>();
            Score score;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {

                score = new Score();

                score.ScoredMarks = cursor.getInt((cursor.getColumnIndex("ScoredMarks")));
                score.TotalMarks = cursor.getInt(cursor.getColumnIndex("TotalMarks"));

                ScoresList.add(score);
                cursor.moveToNext();
            }
            cursor.close();
            return ScoresList;
        } catch (Exception ex) {
            return null;
        }
    }

    /*
    *
    * */


}
