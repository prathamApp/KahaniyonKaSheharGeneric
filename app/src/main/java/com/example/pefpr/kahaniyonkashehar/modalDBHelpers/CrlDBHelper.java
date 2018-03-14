package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Crl;

import java.util.ArrayList;
import java.util.List;


public class CrlDBHelper extends DataBaseHelper {

    final String TABLENAME = "CRL";
    Cursor cursor;
    SQLiteDatabase crlDbObject;
    ContentValues crlContentValues;

    public CrlDBHelper(Context context) {
        super(context);
        crlDbObject = getWritableDatabase();
        crlContentValues = new ContentValues();
    }

    // Fetch data if already exists
    public String getCrlIDFromUsername(String uname) {

        try {
            crlDbObject = getWritableDatabase();
            String crlid = null;

            cursor = crlDbObject.rawQuery("SELECT CRLID FROM " + TABLENAME + " WHERE UserName=?", new String[]{uname});
            Crl crlObject = new Crl();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                crlObject.CRLId = cursor.getString(cursor.getColumnIndex("CRLID"));
                crlid = crlObject.CRLId;
                cursor.moveToNext();
            }
            cursor.close();
            crlDbObject.close();

            return crlid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ExceptionOccured";
        }
    }

    public void replaceData(Crl obj) {

        try {
            crlDbObject = getWritableDatabase();

            crlContentValues.put("CRLID", obj.CRLId);
            crlContentValues.put("FirstName", obj.FirstName);
            crlContentValues.put("LastName", obj.LastName);
            crlContentValues.put("UserName", obj.UserName);
            crlContentValues.put("PassWord", obj.Password);
            crlContentValues.put("ProgramId", obj.ProgramId);
            crlContentValues.put("Mobile", obj.Mobile);
            crlContentValues.put("State", obj.State);
            crlContentValues.put("Email", obj.Email);
            crlContentValues.put("CreatedBy", obj.CreatedBy);
            crlContentValues.put("NewFlag", obj.newCrl);

            crlDbObject.replace("CRL", null, crlContentValues);
            crlDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkTableEmptyness() {

        try {
            crlDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME;
            Cursor mcursor = crlDbObject.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            mcursor.close();
            return icount > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    // Executed when Pull Data will be called
    public void updateJsonData(Crl obj) {

        try {
            crlDbObject = getWritableDatabase();

            crlContentValues.put("CRLID", obj.CRLId);
            crlContentValues.put("FirstName", obj.FirstName);
            crlContentValues.put("LastName", obj.LastName);
            crlContentValues.put("UserName", obj.UserName);
            crlContentValues.put("Password", obj.Password);
            crlContentValues.put("ProgramId", obj.ProgramId);
            crlContentValues.put("Mobile", obj.Mobile);
            crlContentValues.put("State", obj.State);
            crlContentValues.put("Email", obj.Email);
            crlContentValues.put("NewFlag", obj.newCrl);

            crlDbObject.replace("CRL", null, crlContentValues);
            crlDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insertData(Crl obj) {

        try {
            crlDbObject = getWritableDatabase();

            crlContentValues.put("CRLID", obj.CRLId);
            crlContentValues.put("FirstName", obj.FirstName);
            crlContentValues.put("LastName", obj.LastName);
            crlContentValues.put("UserName", obj.UserName);
            crlContentValues.put("PassWord", obj.Password);
            crlContentValues.put("ProgramId", obj.ProgramId);
            crlContentValues.put("Mobile", obj.Mobile);
            crlContentValues.put("State", obj.State);
            crlContentValues.put("Email", obj.Email);
            crlContentValues.put("CreatedBy", obj.CreatedBy);
            crlContentValues.put("NewFlag", obj.newCrl);

            crlDbObject.insert("CRL", null, crlContentValues);
            crlDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean crlExist(String username) {

        try {
            crlDbObject = getWritableDatabase();

            cursor = crlDbObject.rawQuery("SELECT UserName FROM " + TABLENAME + " WHERE UserName = ? ", new String[]{String.valueOf(username)});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    crlDbObject.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean CrlLogin(String uname, String pass) {

        try {
            crlDbObject = getWritableDatabase();

            cursor = crlDbObject.rawQuery("SELECT * FROM " + TABLENAME + " WHERE UserName=? AND PassWord =?", new String[]{uname, pass});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.close();
                    crlDbObject.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getCrlID(String uname, String pass) {

        try {
            crlDbObject = getWritableDatabase();
            String crlid = null;

            cursor = crlDbObject.rawQuery("SELECT CRLID FROM " + TABLENAME + " WHERE UserName=? AND PassWord =?", new String[]{uname, pass});
            Crl crlObject = new Crl();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                crlObject.CRLId = cursor.getString(cursor.getColumnIndex("CRLID"));
                crlid = crlObject.CRLId;
                cursor.moveToNext();
            }
            cursor.close();
            crlDbObject.close();
            return crlid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ExceptionOccured";
        }
    }

    public List<Crl> GetAll() {
        try {
            crlDbObject = getWritableDatabase();
            Cursor cursor = crlDbObject.rawQuery("select * from " + TABLENAME + " ", null);
            crlDbObject.close();
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Crl> GetAllNewCrl() {
        try {
            crlDbObject = getWritableDatabase();
            Cursor cursor = crlDbObject.rawQuery("select * from " + TABLENAME + " where NewFlag = 1", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Crl> _PopulateListFromCursor(Cursor cursor) {
        try {
            crlDbObject = getWritableDatabase();
            List<Crl> crl_list = new ArrayList<Crl>();
            Crl crlObject;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                crlObject = new Crl();

                crlObject.CRLId = cursor.getString(cursor.getColumnIndex("CRLID"));
                crlObject.FirstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                crlObject.LastName = cursor.getString(cursor.getColumnIndex("LastName"));
                crlObject.UserName = cursor.getString((cursor.getColumnIndex("UserName")));
                crlObject.Password = cursor.getString((cursor.getColumnIndex("PassWord")));
                crlObject.State = cursor.getString((cursor.getColumnIndex("State")));
                crlObject.ProgramId = cursor.getInt(cursor.getColumnIndex("ProgramId"));
                crlObject.Mobile = cursor.getString((cursor.getColumnIndex("Mobile")));
                crlObject.State = cursor.getString((cursor.getColumnIndex("State")));
                crlObject.Email = cursor.getString((cursor.getColumnIndex("Email")));
                crlObject.CreatedBy = cursor.getString((cursor.getColumnIndex("CreatedBy")));
                crlObject.newCrl = Boolean.valueOf(cursor.getString((cursor.getColumnIndex("NewFlag"))));

                crl_list.add(crlObject);
                cursor.moveToNext();
            }
            cursor.close();
            crlDbObject.close();
            return crl_list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
