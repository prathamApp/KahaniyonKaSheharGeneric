package com.example.pefpr.kahaniyonkashehar.modalDBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;

import java.util.ArrayList;
import java.util.List;


public class StudentDBHelper extends DataBaseHelper {
    final String TABLENAME = "Student";
    Context c;
    SQLiteDatabase studentDbObject;
    ContentValues studentContentValues;


    public StudentDBHelper(Context context) {
        super(context);
        c = context;
        studentDbObject = getWritableDatabase();
        studentContentValues = new ContentValues();
    }


    // Get Students Based on ChildID & StudentUniqueID 
    public List<Student> GetAllStudentsByChildIDnStudentUniqID(String ChildID, String StudentUID) {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("select * from " + TABLENAME + " where StudentUID = ? AND StudentID = ?", new String[]{ChildID, StudentUID});
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // set Flag to false
    public void SetFlagFalse(String studentID) {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("update " + TABLENAME + " set NewFlag=0 where StudentID = ? ", new String[]{studentID});
            cursor.moveToFirst();
            studentDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Student GetStudentDataByStdID(String studentID) {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("select * from " + TABLENAME + " where StudentID = ? ", new String[]{studentID});
            if (cursor.getCount() > 0)
                return _PopulateObjectFromCursor(cursor);
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void replaceData(Student obj) {

        try {
            studentDbObject = getWritableDatabase();

            studentContentValues.put("StudentID", obj.getStudentID());
            studentContentValues.put("StudentUID", obj.getStudentUID());
            studentContentValues.put("FirstName", obj.getFirstName());
            studentContentValues.put("MiddleName", obj.getMiddleName());
            studentContentValues.put("LastName", obj.getLastName());
            studentContentValues.put("Age", obj.getAge());
            studentContentValues.put("regDate", obj.getRegDate());
            studentContentValues.put("Gender", obj.getGender());
            studentContentValues.put("villageName", obj.getVillageName());
            studentContentValues.put("NewFlag", obj.getNewFlag());
            studentContentValues.put("DeviceId ", obj.getDeviceId());

            studentDbObject.replace("Student", null, studentContentValues);

            studentDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public void insertData(Student obj) {

        try {
            studentDbObject = getWritableDatabase();
            _PopulateContentValues(obj);
            studentDbObject.replace("Student", null, studentContentValues);
            studentDbObject.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getStudentName(String studentId) {
        String studentName = "";
        try {
            Cursor cursor = studentDbObject.rawQuery("select FirstName from " + TABLENAME + " where StudentID ='"+studentId+"'", null);
            cursor.moveToFirst();
            studentName = cursor.getString(cursor.getColumnIndex("FirstName"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return studentName;
    }

    public boolean Add(Student student, SQLiteDatabase studentDbObject1) {
        try {
            _PopulateContentValues(student);
            long resultCount = studentDbObject1.insert(TABLENAME, null, studentContentValues);
            studentDbObject1.close();
            return true;
            //return resultCount;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Update(Student student) {
        try {
            studentDbObject = getWritableDatabase();
            _PopulateContentValues(student);
            long resultCount = studentDbObject.update(TABLENAME, studentContentValues, "StudentID = ?", new String[]{(student.StudentID)});
            studentDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Delete(String studentID) {
        try {
            studentDbObject = getWritableDatabase();
            long resultCount = studentDbObject.delete(TABLENAME, "StudentID = ?", new String[]{studentID});
            studentDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            studentDbObject = getWritableDatabase();
            long resultCount = studentDbObject.delete(TABLENAME, null, null);
            studentDbObject.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Student Get(String studentID) {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("select * from " + TABLENAME + " where StudentID='" + studentID + "'", null);
            return _PopulateObjectFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Student> GetAll() {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Student> GetAllNewStudents() {
        try {
            studentDbObject = getWritableDatabase();
            Cursor cursor = studentDbObject.rawQuery("select * from " + TABLENAME + " where NewFlag = 1", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void _PopulateContentValues(Student obj) {
        studentContentValues = new ContentValues();
        studentContentValues.put("StudentID", obj.getStudentID());
        studentContentValues.put("StudentUID", obj.getStudentUID());
        studentContentValues.put("FirstName", obj.getFirstName());
        studentContentValues.put("MiddleName", obj.getMiddleName());
        studentContentValues.put("LastName", obj.getLastName());
        studentContentValues.put("Age", obj.getAge());
        studentContentValues.put("Gender", obj.getGender());
        studentContentValues.put("villageName", obj.getVillageName());
        studentContentValues.put("NewFlag", obj.getNewFlag());
        studentContentValues.put("DeviceId ", obj.getDeviceId());
        studentContentValues.put("regDate", obj.getRegDate());
    }

    private Student _PopulateObjectFromCursor(Cursor cursor) {
        try {
            studentDbObject = getWritableDatabase();
            Student student = new Student();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                student.setStudentID(cursor.getString(cursor.getColumnIndex("StudentID")));
                student.setStudentUID(cursor.getString(cursor.getColumnIndex("StudentUID")));
                student.setFirstName(cursor.getString(cursor.getColumnIndex("FirstName")));
                student.setMiddleName(cursor.getString(cursor.getColumnIndex("MiddleName")));
                student.setLastName(cursor.getString((cursor.getColumnIndex("LastName"))));
                student.setVillageName(cursor.getString((cursor.getColumnIndex("villageName"))));
                student.setGender(cursor.getString((cursor.getColumnIndex("Gender"))));
                student.setAge(cursor.getInt(cursor.getColumnIndex("Age")));
                student.setDeviceId(cursor.getString((cursor.getColumnIndex("DeviceId"))));
                student.setNewFlag(cursor.getInt(cursor.getColumnIndex("NewFlag")));
                student.setRegDate(cursor.getString((cursor.getColumnIndex("regDate"))));
                cursor.moveToNext();
            }
            cursor.close();
            studentDbObject.close();
            return student;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Student> _PopulateListFromCursor(Cursor cursor) {
        try {
            studentDbObject = getWritableDatabase();
            List<Student> students = new ArrayList<Student>();
            Student student;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                student = new Student();

                student.setStudentID(cursor.getString(cursor.getColumnIndex("StudentID")));
                student.setStudentUID(cursor.getString(cursor.getColumnIndex("StudentUID")));
                student.setFirstName(cursor.getString(cursor.getColumnIndex("FirstName")));
                student.setMiddleName(cursor.getString(cursor.getColumnIndex("MiddleName")));
                student.setLastName(cursor.getString((cursor.getColumnIndex("LastName"))));
                student.setVillageName(cursor.getString((cursor.getColumnIndex("villageName"))));
                student.setGender(cursor.getString((cursor.getColumnIndex("Gender"))));
                student.setAge(cursor.getInt(cursor.getColumnIndex("Age")));
                student.setDeviceId(cursor.getString((cursor.getColumnIndex("DeviceId"))));
                student.setNewFlag(cursor.getInt(cursor.getColumnIndex("NewFlag")));
                student.setRegDate(cursor.getString((cursor.getColumnIndex("regDate"))));
                students.add(student);
                cursor.moveToNext();
            }
            cursor.close();
            studentDbObject.close();
            return students;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}