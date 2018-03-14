package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Village;
import com.example.pefpr.kahaniyonkashehar.modalclasses.VillageList;

import java.util.ArrayList;
import java.util.List;

public class VillageDBHelper extends DataBaseHelper {
    final String TABLENAME = "Village";
    Context c;
    SQLiteDatabase villageDbObject;
    ContentValues villageContentValues;


    public VillageDBHelper(Context context) {
        super(context);
        c = context;
        villageContentValues = new ContentValues();
//        villageDbObject = getWritableDatabase();
    }

    public int GetVillageIDByBlock(String BlockName) {
        int val = 0;
        try {
            villageDbObject = getWritableDatabase();

            Cursor cursor = villageDbObject.rawQuery("select VillageID from " + TABLENAME + " where Block=?", new String[]{BlockName});

            if (cursor.moveToFirst()) // data?
                val = Integer.parseInt(cursor.getString(cursor.getColumnIndex("VillageID")));
            villageDbObject.close();
            return val;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return val;
    }

    public List<VillageList> GetRIVillageIDByBlock(String BlockName) {
        try {
            villageDbObject = getWritableDatabase();
            List<VillageList> list = new ArrayList<VillageList>();
            {
                Cursor cursor = villageDbObject.rawQuery("select VillageID from " + TABLENAME + " where Block ='" + BlockName + "'", null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    list.add(new VillageList(cursor.getInt(cursor.getColumnIndex("VillageID"))));

                    cursor.moveToNext();
                }
                villageDbObject.close();

            }
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean checkTableEmptyness() {

        try {
            villageDbObject = getReadableDatabase();

            String count = "SELECT count(*) FROM " + TABLENAME;
            Cursor mcursor = villageDbObject.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Executed when Pull Data will be called
    public void updateJsonData(Village villageobj) {

        try {
            villageDbObject = getWritableDatabase();

            villageContentValues.put("VillageID", villageobj.VillageID);
            villageContentValues.put("VillageCode", villageobj.VillageCode);
            villageContentValues.put("VillageName", villageobj.VillageName);
            villageContentValues.put("Block", villageobj.Block);
            villageContentValues.put("District", villageobj.District);
            villageContentValues.put("State", villageobj.State);
            villageContentValues.put("CRLID", villageobj.CRLID);

            villageDbObject.replace("Village", null, villageContentValues);

            villageDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insertData(Village villageobj) {
        try {

            villageDbObject = getWritableDatabase();
            villageContentValues.put("VillageID", villageobj.VillageID);
            villageContentValues.put("VillageCode", villageobj.VillageCode);
            villageContentValues.put("VillageName", villageobj.VillageName);
            villageContentValues.put("Block", villageobj.Block);
            villageContentValues.put("District", villageobj.District);
            villageContentValues.put("State", villageobj.State);
            villageContentValues.put("CRLID", villageobj.CRLID);

            villageDbObject.insert("Village", null, villageContentValues);//pravin edited

            villageDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int GetVillageID(String villageName) {
        int val = 0;
        try {
            villageDbObject = getWritableDatabase();

            Cursor cursor = villageDbObject.rawQuery("select VillageID from " + TABLENAME + " where VillageName=?", new String[]{villageName});

            if (cursor.moveToFirst()) // data?
                val = Integer.parseInt(cursor.getString(cursor.getColumnIndex("VillageID")));
            villageDbObject.close();
            return val;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return val;
    }

    public boolean Add(Village village, SQLiteDatabase villageDbObject1) {
        try {
            _PopulateContentValues(village);

            long resultCount = villageDbObject1.insert(TABLENAME, null, villageContentValues);
            villageDbObject1.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Update(Village village) {
        try {
            villageDbObject = getWritableDatabase();
            _PopulateContentValues(village);

            long resultCount = villageDbObject.update(TABLENAME, villageContentValues, "VillageID = ?", new String[]{((Integer) village.VillageID).toString()});
            villageDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Delete(Integer villageID) {
        try {
            villageDbObject = getWritableDatabase();
            long resultCount = villageDbObject.delete(TABLENAME, "VillageID = ?", new String[]{villageID.toString()});
            villageDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            villageDbObject = getWritableDatabase();
            long resultCount = villageDbObject.delete(TABLENAME, null, null);
            villageDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Village Get(int villageID) {
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("select * from " + TABLENAME + " where VillageID='" + villageID + "'", null);
            return _PopulateObjectFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Village> GetAll() {
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("select * from " + TABLENAME, null);
//            villageDbObject.close();
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> GetDistrict(String state) {
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("SELECT DISTINCT District FROM " + TABLENAME + " WHERE State = ? ORDER BY District ASC ", new String[]{state});

            List<String> list = new ArrayList<String>();
            list.add("--Select District--");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex("District")));
                cursor.moveToNext();
            }
            villageDbObject.close();
            return list;
        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public List<String> GetState() {
        List<String> list = new ArrayList<String>();
        list.add("--Select State--");
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("SELECT DISTINCT State FROM " + TABLENAME + " ORDER BY State ASC", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex("State")));
                cursor.moveToNext();
            }
            cursor.close();
            villageDbObject.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
    }

    public List<String> GetStatewiseBlock(String state) {
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("SELECT DISTINCT Block FROM " + TABLENAME + " WHERE State = ? ORDER BY Block ASC", new String[]{state});

            List<String> list = new ArrayList<String>();
            list.add("--Select Block--");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex("Block")));
                cursor.moveToNext();
            }
            cursor.close();
            villageDbObject.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> GetBlock(String district) {
        try {
            villageDbObject = getWritableDatabase();
            Cursor cursor = villageDbObject.rawQuery("SELECT DISTINCT Block FROM " + TABLENAME + " WHERE District = ? ORDER BY Block ASC", new String[]{district});

            List<String> list = new ArrayList<String>();
            list.add("--Select Block--");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex("Block")));
                cursor.moveToNext();
            }
            villageDbObject.close();
            return list;
        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public List<VillageList> Getvillages() {
        try {
            villageDbObject = getWritableDatabase();
            List<VillageList> list = new ArrayList<VillageList>();

            Cursor cursor = villageDbObject.rawQuery("SELECT VillageID, VillageName FROM " + TABLENAME + " ORDER BY VillageName ASC", null);
            list.add(new VillageList(0, "--Select Village--"));
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new VillageList(cursor.getInt(cursor.getColumnIndex("VillageID")), cursor.getString(cursor.getColumnIndex("VillageName"))));

                cursor.moveToNext();
            }
            cursor.close();
            villageDbObject.close();

            return list;

        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }

    // Block not present
    public List<VillageList> GetVillagesByGroup(String block) {
        try {
            villageDbObject = getWritableDatabase();
            List<VillageList> list = new ArrayList<VillageList>();
            if (block.equals("--Select Village--")) {
                list.add(new VillageList(0, "--Select Village--"));
            } else {
                Cursor cursor = villageDbObject.rawQuery("SELECT VillageID,VillageName FROM Groups WHERE Block = ? ORDER BY VillageName ASC", new String[]{block});
                list.add(new VillageList(0, "--Select Village--"));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    list.add(new VillageList(cursor.getInt(cursor.getColumnIndex("VillageID")), cursor.getString(cursor.getColumnIndex("VillageName"))));
                    cursor.moveToNext();
                }
                cursor.close();
                villageDbObject.close();
            }
            return list;

        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public List<VillageList> GetVillages(String block) {
        try {
            villageDbObject = getWritableDatabase();
            List<VillageList> list = new ArrayList<VillageList>();
            if (block.equals("--Select Village--")) {
                list.add(new VillageList(0, "--Select Village--"));
            } else {
                Cursor cursor = villageDbObject.rawQuery("SELECT VillageID,VillageName FROM " + TABLENAME + " WHERE Block = ? ORDER BY VillageName ASC", new String[]{block});
                list.add(new VillageList(0, "--Select Village--"));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    list.add(new VillageList(cursor.getInt(cursor.getColumnIndex("VillageID")), cursor.getString(cursor.getColumnIndex("VillageName"))));

                    cursor.moveToNext();
                }
                cursor.close();
                villageDbObject.close();
            }
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void _PopulateContentValues(Village village) {
        villageContentValues.put("VillageID", village.VillageID);
        villageContentValues.put("VillageCode", village.VillageCode);
        villageContentValues.put("VillageName", village.VillageName);
        villageContentValues.put("Block", village.Block);
        villageContentValues.put("District", village.District);
        villageContentValues.put("State", village.State);
        villageContentValues.put("CRLID", village.CRLID);
    }

    private Village _PopulateObjectFromCursor(Cursor cursor) {
        try {
            villageDbObject = getWritableDatabase();
            Village village = new Village();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                village.VillageID = cursor.getInt(cursor.getColumnIndex("VillageID"));
                village.VillageCode = cursor.getString(cursor.getColumnIndex("VillageCode"));
                village.VillageName = cursor.getString(cursor.getColumnIndex("VillageName"));
                village.Block = cursor.getString((cursor.getColumnIndex("Block")));
                village.District = cursor.getString((cursor.getColumnIndex("District")));
                village.State = cursor.getString((cursor.getColumnIndex("State")));
                village.CRLID = cursor.getString(cursor.getColumnIndex("CRLID"));
                cursor.moveToNext();
            }
            cursor.close();
            villageDbObject.close();
            return village;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Village> _PopulateListFromCursor(Cursor cursor) {
        try {
//            villageDbObject = getWritableDatabase();
            List<Village> _villages = new ArrayList<Village>();
            Village village = new Village();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                village.VillageID = cursor.getInt(cursor.getColumnIndex("VillageID"));
                village.VillageCode = cursor.getString(cursor.getColumnIndex("VillageCode"));
                village.VillageName = cursor.getString(cursor.getColumnIndex("VillageName"));
                village.Block = cursor.getString((cursor.getColumnIndex("Block")));
                village.District = cursor.getString((cursor.getColumnIndex("District")));
                village.State = cursor.getString((cursor.getColumnIndex("State")));
                village.CRLID = cursor.getString(cursor.getColumnIndex("CRLID"));

                _villages.add(village);
                cursor.moveToNext();
            }
            cursor.close();
//            villageDbObject.close();
            return _villages;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
