package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Group;
import com.example.pefpr.kahaniyonkashehar.modalclasses.GroupList;
import com.example.pefpr.kahaniyonkashehar.modalclasses.VillageList;

import java.util.ArrayList;
import java.util.List;


public class GroupDBHelper extends DataBaseHelper {
    final String TABLENAME = "Groups";
    Context c;
    SQLiteDatabase groupDbObject;
    ContentValues groupContentValues;

    public GroupDBHelper(Context context) {
        super(context);
        c = context;
        groupDbObject = getWritableDatabase();
        groupContentValues = new ContentValues();
    }

    // Populate Groups for RI Case
    public List<GroupList> GetAllRIGroups(List<VillageList> villageId) {
        try {

            groupDbObject = getWritableDatabase();
            List<GroupList> list = new ArrayList<GroupList>();

            for (int i = 0; i < villageId.size(); i++) {
                int villID = villageId.get(i).villageId;
                {
                    Cursor cursor = groupDbObject.rawQuery("SELECT GroupID,GroupName FROM " + TABLENAME + " WHERE VillageID =" + villID + " ORDER BY GroupName ASC", null);//Ketan
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        list.add(new GroupList(cursor.getString(cursor.getColumnIndex("GroupID")), cursor.getString(cursor.getColumnIndex("GroupName"))));

                        cursor.moveToNext();
                    }


                }

            }
            groupDbObject.close();
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // set Flag to false
    public void SetFlagFalse(String GroupID) {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("update " + TABLENAME + " set NewFlag=false where GroupID = ? ", new String[]{GroupID});
            cursor.moveToFirst();
            groupDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getGroupId(String grpName) {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select GroupID from " + TABLENAME + " where GroupName = ?", new String[]{grpName});
            cursor.moveToFirst();
            groupDbObject.close();
            return cursor.getString(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ExceptionOccured";
        }
    }

    public void replaceData(Group obj) {

        try {
            groupDbObject = getWritableDatabase();

            groupContentValues.put("GroupID", obj.GroupID);
            groupContentValues.put("GroupCode", obj.GroupCode);
            groupContentValues.put("GroupName", obj.GroupName);
            groupContentValues.put("UnitNumber", obj.UnitNumber);
            groupContentValues.put("DeviceID", obj.DeviceID);
            groupContentValues.put("Responsible", obj.Responsible);
            groupContentValues.put("ResponsibleMobile", obj.ResponsibleMobile);
            groupContentValues.put("VillageID", obj.VillageID);
            groupContentValues.put("ProgramId", obj.ProgramID);
            groupContentValues.put("CreatedBy", obj.CreatedBy);
            groupContentValues.put("NewFlag", obj.newGroup);
            groupContentValues.put("VillageName", obj.VillageName);
            groupContentValues.put("SchoolName", obj.SchoolName);


            groupDbObject.replace("Groups", null, groupContentValues);

            groupDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void insertData(Group obj) {
        try {

            groupDbObject = getWritableDatabase();

            groupContentValues.put("GroupID", obj.GroupID);
            groupContentValues.put("GroupCode", obj.GroupCode);
            groupContentValues.put("GroupName", obj.GroupName);
            groupContentValues.put("UnitNumber", obj.UnitNumber);
            groupContentValues.put("DeviceID", obj.DeviceID);
            groupContentValues.put("Responsible", obj.Responsible);
            groupContentValues.put("ResponsibleMobile", obj.ResponsibleMobile);
            groupContentValues.put("VillageID", obj.VillageID);
            groupContentValues.put("ProgramId", obj.ProgramID);
            groupContentValues.put("CreatedBy", obj.CreatedBy);
            groupContentValues.put("NewFlag", obj.newGroup);
            groupContentValues.put("VillageName", obj.VillageName);
            groupContentValues.put("SchoolName", obj.SchoolName);


            groupDbObject.insert("Groups", null, groupContentValues);

            groupDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // MHM 21 June
    public List<GroupList> GetUnitwiseSchoolNVillage(String selectedUnit) {
        try {
            groupDbObject = getWritableDatabase();
            List<GroupList> list = new ArrayList<GroupList>();
            {
                //Cursor cursor = groupDbObject.rawQuery("SELECT GroupID,GroupName FROM " + TABLENAME + " WHERE VillageID = ? and NewFlag ORDER BY GroupName ASC", new String[]{String.valueOf(villageId)});
                Cursor cursor = groupDbObject.rawQuery("SELECT VillageName,SchoolName,CreatedBy FROM " + TABLENAME + " WHERE GroupID = ?", new String[]{selectedUnit});//Ketan
                //list.add(new GroupList("0", "--Select Group--"));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    list.add(new GroupList(cursor.getString(cursor.getColumnIndex("VillageName")), cursor.getString(cursor.getColumnIndex("SchoolName"))));

                    cursor.moveToNext();
                }
                groupDbObject.close();

            }
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean Add(Group group, SQLiteDatabase groupDbObject1) {
        try {
            _PopulateContentValues(group);
            long resultCount = groupDbObject1.insert(TABLENAME, null, groupContentValues);
            groupDbObject1.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Update(Group group) {
        try {
            groupDbObject = getWritableDatabase();
            _PopulateContentValues(group);
            long resultCount = groupDbObject.update(TABLENAME, groupContentValues, "GroupID = ?", new String[]{(group.GroupID).toString()});
            groupDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Delete(String groupID) {
        try {
            groupDbObject = getWritableDatabase();
            long resultCount = groupDbObject.delete(TABLENAME, "GroupID = ?", new String[]{groupID.toString()});
            groupDbObject.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            groupDbObject = getWritableDatabase();
            long resultCount = groupDbObject.delete(TABLENAME, null, null);
            groupDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Group Get(String groupID) {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select * from " + TABLENAME + " where GroupID='" + groupID + "'", null);
            return _PopulateObjectFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getGroupById(String id) {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select GroupName from " + TABLENAME + " where GroupID='" + id + "'", null);
            cursor.moveToFirst();
            groupDbObject.close();
            return cursor.getString(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ExceptionOccured";
        }
    }

    //Pravin
    public String getGroupIdByName(String name) {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select GroupID from " + TABLENAME + " where GroupName='" + name + "'", null);
            cursor.moveToFirst();
            groupDbObject.close();
            return cursor.getString(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Group> GetAll() {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<Group>();
        }
    }

    public List<Group> GetAllNewGroups() {
        try {
            groupDbObject = getWritableDatabase();
            Cursor cursor = groupDbObject.rawQuery("select * from " + TABLENAME + " where NewFlag = 1", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<GroupList> GetGroups(int villageId) {
        try {
            groupDbObject = getWritableDatabase();
            List<GroupList> list = new ArrayList<GroupList>();
            if (villageId == 0) {
                list.add(new GroupList("0", "--Select Group--"));
            } else {
                //Cursor cursor = groupDbObject.rawQuery("SELECT GroupID,GroupName FROM " + TABLENAME + " WHERE VillageID = ? and NewFlag ORDER BY GroupName ASC", new String[]{String.valueOf(villageId)});
                Cursor cursor = groupDbObject.rawQuery("SELECT GroupID,GroupName FROM " + TABLENAME + " WHERE VillageID =" + villageId + " ORDER BY GroupName ASC", null);//Ketan
                list.add(new GroupList("0", "--Select Group--"));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    list.add(new GroupList(cursor.getString(cursor.getColumnIndex("GroupID")), cursor.getString(cursor.getColumnIndex("GroupName"))));

                    cursor.moveToNext();
                }
                groupDbObject.close();

            }
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void _PopulateContentValues(Group group) {
        try {
            groupContentValues.put("GroupID", group.GroupID);
            groupContentValues.put("GroupName", group.GroupName);
            groupContentValues.put("UnitNumber", group.UnitNumber);
            groupContentValues.put("DeviceID", group.DeviceID);
            groupContentValues.put("Responsible", group.Responsible);
            groupContentValues.put("ResponsibleMobile", group.ResponsibleMobile);
            groupContentValues.put("VillageID", group.VillageID);
            groupContentValues.put("GroupCode", group.GroupCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Group _PopulateObjectFromCursor(Cursor cursor) {
        try {
            groupDbObject = getWritableDatabase();
            Group group = new Group();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                group.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                group.GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
                group.UnitNumber = cursor.getString((cursor.getColumnIndex("UnitNumber")));
                group.DeviceID = cursor.getString((cursor.getColumnIndex("DeviceID")));
                group.VillageID = cursor.getInt((cursor.getColumnIndex("VillageID")));
                group.Responsible = cursor.getString((cursor.getColumnIndex("Responsible")));
                group.ResponsibleMobile = cursor.getString(cursor.getColumnIndex("ResponsibleMobile"));
                group.GroupCode = cursor.getString((cursor.getColumnIndex("GroupCode")));
                cursor.moveToNext();
            }
            cursor.close();
            groupDbObject.close();
            return group;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Group> _PopulateListFromCursor(Cursor cursor) {
        try {
            groupDbObject = getWritableDatabase();
            List<Group> groups = new ArrayList<Group>();
            Group group;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                group = new Group();

                group.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                group.GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
                group.UnitNumber = cursor.getString((cursor.getColumnIndex("UnitNumber")));
                group.DeviceID = cursor.getString((cursor.getColumnIndex("DeviceID")));
                group.VillageID = cursor.getInt((cursor.getColumnIndex("VillageID")));
                group.GroupCode = cursor.getString((cursor.getColumnIndex("GroupCode")));
                group.ResponsibleMobile = cursor.getString(cursor.getColumnIndex("ResponsibleMobile"));
                group.Responsible = cursor.getString((cursor.getColumnIndex("Responsible")));
                group.ProgramID = cursor.getInt((cursor.getColumnIndex("ProgramId")));
                group.CreatedBy = cursor.getString((cursor.getColumnIndex("CreatedBy")));
                group.SchoolName = cursor.getString((cursor.getColumnIndex("SchoolName")));
                group.VillageName = cursor.getString((cursor.getColumnIndex("VillageName")));
                group.newGroup = Boolean.valueOf(cursor.getString((cursor.getColumnIndex("NewFlag"))));

                groups.add(group);
                cursor.moveToNext();
            }
            cursor.close();
            groupDbObject.close();
            return groups;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}