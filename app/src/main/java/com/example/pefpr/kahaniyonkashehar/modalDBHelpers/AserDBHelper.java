package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Aser;

import java.util.ArrayList;
import java.util.List;


public class AserDBHelper extends DataBaseHelper {

    final String TABLENAME = "Aser";
    Cursor cursor;
    SQLiteDatabase aserDbObject;
    ContentValues aserContentValues;

    public AserDBHelper(Context context) {
        super(context);
        aserDbObject = getWritableDatabase();
        aserContentValues = new ContentValues();
    }

    // Check Data entry in Aser DB
    public boolean CheckChildIDExists(String uniqStdID, String ChildID, String GroupID) {
        try {
            aserDbObject = getWritableDatabase();

            cursor = aserDbObject.rawQuery("SELECT * FROM " + TABLENAME + " WHERE StudentId !=? AND ChildID=? AND GroupID =?", new String[]{uniqStdID, ChildID, GroupID});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    aserDbObject.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public int GetBaselineCount(String GroupID) {
        try {
            aserDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME + " where GroupID = ? AND TestType = 0 ";
            Cursor mcursor = aserDbObject.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0)
                return icount;
            else
                return icount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int GetEndline1Count(String GroupID) {

        try {
            String count = "SELECT count(*) FROM " + TABLENAME + " where GroupID = ? AND TestType = 1 ";
            aserDbObject = getReadableDatabase();
            Cursor mcursor = aserDbObject.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            mcursor.close();
            if (icount > 0)
                return icount;
            else
                return icount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int GetEndline2Count(String GroupID) {

        try {
            aserDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME + " where GroupID = ? AND TestType = 2 ";
            Cursor mcursor = aserDbObject.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0)
                return icount;
            else
                return icount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int GetEndline3Count(String GroupID) {

        try {
            aserDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME + " where GroupID = ? AND TestType = 3 ";
            Cursor mcursor = aserDbObject.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0)
                return icount;
            else
                return icount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int GetEndline4Count(String GroupID) {

        try {
            aserDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME + " where GroupID = ? AND TestType = 4 ";
            Cursor mcursor = aserDbObject.rawQuery(count, new String[]{GroupID});
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0)
                return icount;
            else
                return icount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // Check Data entry in Aser DB
    public boolean CheckDataExists(String StudentId, int TestType) {

        try {

            aserDbObject = getWritableDatabase();

            cursor = aserDbObject.rawQuery("SELECT * FROM " + TABLENAME + " WHERE StudentId=? AND TestType =?", new String[]{StudentId, String.valueOf(TestType)});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    aserDbObject.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // set Flag to false
    public void UpdateAserData(String ChildID, String testDate, int lang, int num, int oad, int osb, int oml, int odv, int wad, int wsb, String crtby, String crtdt, int isSelected, String studentID, int TstType) {
        try {
            aserDbObject = getWritableDatabase();
            Cursor cursor = aserDbObject.rawQuery("update " + TABLENAME + " set ChildID='" + ChildID + "', TestDate='" + testDate + "', Lang = " + lang + ", Num = " + num + ", OAdd = " + oad + ", OSub = " + osb + ", OMul = " + oml + ", ODiv = " + odv + ", WAdd = " + wad + ", WSub = " + wsb + ", CreatedBy = '" + crtby + "', CreatedDate = '" + crtdt + "', FLAG =" + isSelected + "  WHERE StudentId='" + studentID + "' AND TestType =" + TstType + "", null);
            cursor.moveToFirst();
            aserDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Getting Aser Data based on Unique Student ID passed by universal child form
    public List<Aser> GetAllByStudentID(String StudentID, int testV) {
        try {
            aserDbObject = getWritableDatabase();
            Cursor cursor = aserDbObject.rawQuery("select * from " + TABLENAME + " where StudentId = ? AND TestType = ?", new String[]{StudentID, String.valueOf(testV)});

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean checkTableEmptyness() {
        try {
            aserDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM ASER";
            Cursor mcursor = aserDbObject.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            return icount > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void replaceData(Aser obj) {
        try {

            aserDbObject = getWritableDatabase();

            aserContentValues.put("StudentId", obj.StudentId);
            aserContentValues.put("TestType", obj.TestType);
            aserContentValues.put("TestDate", obj.TestDate);
            aserContentValues.put("Lang", obj.Lang);
            aserContentValues.put("Num", obj.Num);
            aserContentValues.put("OAdd", obj.OAdd);
            aserContentValues.put("OSub", obj.OSub);
            aserContentValues.put("OMul", obj.OMul);
            aserContentValues.put("ODiv", obj.ODiv);
            aserContentValues.put("WAdd", obj.WAdd);
            aserContentValues.put("WSub", obj.WSub);
            aserContentValues.put("CreatedBy", obj.CreatedBy);
            aserContentValues.put("CreatedDate", obj.CreatedDate);
            aserContentValues.put("DeviceId", obj.DeviceId);
            aserContentValues.put("FLAG", obj.FLAG);

            aserDbObject.replace("Aser", null, aserContentValues);
            aserDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insertData(Aser obj) {

        try {
            aserDbObject = getWritableDatabase();

            aserContentValues.put("StudentId", obj.StudentId);
            aserContentValues.put("GroupID", obj.GroupID);
            aserContentValues.put("ChildID", obj.ChildID);
            aserContentValues.put("TestType", obj.TestType);
            aserContentValues.put("TestDate", obj.TestDate);
            aserContentValues.put("Lang", obj.Lang);
            aserContentValues.put("Num", obj.Num);
            aserContentValues.put("OAdd", obj.OAdd);
            aserContentValues.put("OSub", obj.OSub);
            aserContentValues.put("OMul", obj.OMul);
            aserContentValues.put("ODiv", obj.ODiv);
            aserContentValues.put("WAdd", obj.WAdd);
            aserContentValues.put("WSub", obj.WSub);
            aserContentValues.put("CreatedBy", obj.CreatedBy);
            aserContentValues.put("CreatedDate", obj.CreatedDate);
            aserContentValues.put("DeviceId", obj.DeviceId);
            aserContentValues.put("FLAG", obj.FLAG);


            aserDbObject.insert("Aser", null, aserContentValues);
            aserDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Aser> GetAll() {
        try {
            aserDbObject = getWritableDatabase();
            Cursor cursor = aserDbObject.rawQuery("select * from " + TABLENAME, null);

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Aser> GetAllNewAserGroups() {
        try {
            aserDbObject = getWritableDatabase();
            Cursor cursor = aserDbObject.rawQuery("select * from " + TABLENAME, null);

            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Aser> _PopulateListFromCursor(Cursor cursor) {
        try {
            aserDbObject = getWritableDatabase();
            List<Aser> aser_list = new ArrayList<Aser>();
            Aser aserObject;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {

                aserObject = new Aser();

                aserObject.StudentId = cursor.getString(cursor.getColumnIndex("StudentId"));
                aserObject.ChildID = cursor.getString(cursor.getColumnIndex("ChildID"));
                aserObject.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                aserObject.TestType = cursor.getInt(cursor.getColumnIndex("TestType"));
                aserObject.TestDate = cursor.getString(cursor.getColumnIndex("TestDate"));
                aserObject.Lang = cursor.getInt((cursor.getColumnIndex("Lang")));
                aserObject.Num = cursor.getInt((cursor.getColumnIndex("Num")));
                aserObject.OAdd = cursor.getInt((cursor.getColumnIndex("OAdd")));
                aserObject.OSub = cursor.getInt((cursor.getColumnIndex("OSub")));
                //Boolean.valueOf(cursor.getString((cursor.getColumnIndex("OSub"))));
                aserObject.OMul = cursor.getInt((cursor.getColumnIndex("OMul")));
                aserObject.ODiv = cursor.getInt((cursor.getColumnIndex("ODiv")));
                aserObject.WAdd = cursor.getInt((cursor.getColumnIndex("WAdd")));
                aserObject.WSub = cursor.getInt((cursor.getColumnIndex("WSub")));
                aserObject.CreatedBy = cursor.getString((cursor.getColumnIndex("CreatedBy")));
                aserObject.CreatedDate = cursor.getString((cursor.getColumnIndex("CreatedDate")));
                aserObject.DeviceId = cursor.getString((cursor.getColumnIndex("DeviceId")));
                aserObject.FLAG = cursor.getInt((cursor.getColumnIndex("FLAG")));

                aser_list.add(aserObject);
                cursor.moveToNext();
            }
            cursor.close();
            aserDbObject.close();
            return aser_list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
