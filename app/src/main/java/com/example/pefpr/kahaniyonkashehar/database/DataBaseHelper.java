package com.example.pefpr.kahaniyonkashehar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.CrlDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Crl;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Level;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by pravin on 17/1/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "//data//com.example.pefpr.kahaniyonkashehar//databases//";

    private static String DB_NAME = "KKSdb";

    public SQLiteDatabase myDataBase;

    public final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 21);
        this.myContext = context;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDB() {
        try {
            myDataBase = this.getWritableDatabase();
            myDataBase.execSQL(DatabaseInitialization.CreateStudentTable);
            myDataBase.execSQL(DatabaseInitialization.CreateLevelTable);
            myDataBase.execSQL(DatabaseInitialization.CreateAttendanceTable);
            myDataBase.execSQL(DatabaseInitialization.CreateScoreTable);
            myDataBase.execSQL(DatabaseInitialization.CreateSessionTable);
            myDataBase.execSQL(DatabaseInitialization.CreateStatusTable);
            myDataBase.execSQL(DatabaseInitialization.CreateCRLTable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (!dbExist) {
            if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamKKSTabDB.db").exists()) {
                copyDataBase();
            } else {
                try {
                    createDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            /* For checking tables */

                Cursor c = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        Log.d(":::", "Table Name=> " + c.getString(0));
                        c.moveToNext();
                    }
                }

                StatusDBHelper statusDBHelper = new StatusDBHelper(myContext);
                statusDBHelper.insertInitialData("DeviceID", Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID), Build.SERIAL);
                statusDBHelper.insertInitialData("CurrentSession", "", "");
                statusDBHelper.insertInitialData("SdCardPath", "NA", "");
                statusDBHelper.insertInitialData("AppLang", "NA", "");
                statusDBHelper.insertInitialData("insertedStudents", "N", "");

                CrlDBHelper crlDBHelper;
                crlDBHelper = new CrlDBHelper(myContext);
                try {

                    Crl crl = new Crl();
                    crl.setCRLId("admin_crl");
                    crl.setEmail("admin@admin");
                    crl.setFirstName("Admin");
                    crl.setLastName("Admin");
                    crl.setMobile("0123456789");
                    crl.setUserName("kksadmin");
                    crl.setPassword("kkspratham");
                    crl.setState("NA");
                    crl.setProgramId(2);
                    crl.newCrl = true;
                    crl.CreatedBy = "NA";
                    crlDBHelper.replaceData(crl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    /*if (!new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamTabDB.db").exists()) {
            //do nothing - database already exist
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                try {
                    createDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                //By calling this method an empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();
                try {
                    copyDataBase();
                    //POS doesn't have level table so explicitely creating
                    SQLiteDatabase dbInstance = this.getWritableDatabase();
                    dbInstance.execSQL(DatabaseInitialization.CreateLevelTable);
                    copyStudentsObtainedFromPOStoKKSforLevel();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }
        }*/

    public void insertDataFromStudentJSON() {
        SdCardPath ex_path;
        String sdCardPathString;
        StudentDBHelper studentDBHelper = new StudentDBHelper(myContext);
        StatusDBHelper statusDBHelper = new StatusDBHelper(myContext);

        ex_path = new SdCardPath(myContext);
        sdCardPathString = ex_path.getSdCardPath();

        sdCardPathString += "JsonFiles/";


        JSONArray studentsJsonArray = null;
        try {
            studentsJsonArray = new JSONArray(getStudentJson(sdCardPathString, "StudentList.json"));
            for (int i = 0; i < studentsJsonArray.length(); i++) {
                JSONObject studentsJsonObject = studentsJsonArray.getJSONObject(i);

                Student student = new Student();
                student.setStudentID(studentsJsonObject.getString("StudentId"));
                student.setStudentUID(studentsJsonObject.getString("StudentUID"));
                student.setFirstName(studentsJsonObject.getString("FirstName"));
                student.setMiddleName(studentsJsonObject.getString("MiddleName"));
                student.setLastName(studentsJsonObject.getString("LastName"));
                student.setRegDate(""+KksApplication.getCurrentDateTime());
                student.setGender(studentsJsonObject.getString("Gender"));
                student.setAge(studentsJsonObject.getInt("Age"));
                student.setDeviceId(statusDBHelper.getValue("DeviceID"));
                student.setNewFlag(0);
                student.setVillageName("");

                studentDBHelper.insertData(student);
            }
/*            List<Student> students = studentDBHelper.GetAll();
            Toast.makeText(myContext, ""+students.size(), Toast.LENGTH_SHORT).show();*/
            statusDBHelper.Update("insertedStudents", "Y");
            setLevelsOfJSONStudents();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStudentJson(String sdCardPathString, String file_name) {
        String studentData = "";
        try {
            File studentJSON = new File(sdCardPathString, file_name);
            FileInputStream stream = new FileInputStream(studentJSON);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                studentData = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentData;
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            File file = myContext.getDir("databases", Context.MODE_PRIVATE);
            String myPath = file.getAbsolutePath().replace("app_databases", "databases") + "/" + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // e.printStackTrace();
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        this.getReadableDatabase();
        File input = new File(Environment.getExternalStorageDirectory().getPath() + "/PrathamKKSTabDB.db");
        InputStream myInput = new FileInputStream(input);
        // Path to the just created empty db
        File file = myContext.getDir("databases", Context.MODE_PRIVATE);
        String myPath = file.getAbsolutePath().replace("app_databases", "databases") + "/" + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(myPath);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBaseAndModify() throws SQLException {
        try {
            //Open the database
//        String myPath = DB_PATH + DB_NAME;
            File file = myContext.getDir("databases", Context.MODE_PRIVATE);
            String myPath = file.getAbsolutePath().replace("app_databases", "databases") + "/" + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

            myDataBase.execSQL("DROP TABLE IF EXISTS AssessmentScores");
            myDataBase.execSQL("DROP TABLE IF EXISTS Attendance");
            myDataBase.execSQL("DROP TABLE IF EXISTS Logs");
            myDataBase.execSQL("DROP TABLE IF EXISTS Scores");
            myDataBase.execSQL("DROP TABLE IF EXISTS Session");
            myDataBase.execSQL("DROP TABLE IF EXISTS Status");
            myDataBase.execSQL("DROP TABLE IF EXISTS UserType");
            myDataBase.execSQL("DROP TABLE IF EXISTS Users");

            Log.d("version::", myDataBase.getVersion() + "");
            Cursor c = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    Log.d(":::", "Table Name=> " + c.getString(0));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLevelsOfJSONStudents() {
        StudentDBHelper studentDBHelper = new StudentDBHelper(myContext);
        LevelDBHelper levelDBHelper = new LevelDBHelper(myContext);
        List<Student> students = studentDBHelper.GetAll();
        if (students != null || students.size() > 0) {
            Student student;
            Level level;
            for (int i = 0; i < students.size(); i++) {
                level = new Level();
                student = students.get(i);
                level.StudentID = student.StudentID;
                level.CurrentLevel = "0.0";
                level.BaseLevel = "1.2";
                levelDBHelper.Add(level, this.getWritableDatabase());
            }
        }
    }
}
