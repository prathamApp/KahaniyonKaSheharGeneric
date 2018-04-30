package com.example.pefpr.kahaniyonkashehar.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.CrlDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.ScoreDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.VillageDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Crl;
import com.example.pefpr.kahaniyonkashehar.modalclasses.KksSession;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Level;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;
import com.example.pefpr.kahaniyonkashehar.syncoperations.SyncUtility;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminConsole extends BaseActivity {

    @BindView(R.id.ll_admin_menu)
    LinearLayout ll_admin_menu;
    @BindView(R.id.ll_adminform)
    LinearLayout ll_adminform;
    @BindView(R.id.ll_admin_addnew)
    LinearLayout ll_admin_addnew;
    @BindView(R.id.btn_ac_submitunit)
    Button btn_ac_submitunit;

    @BindView(R.id.btn_add_admin)
    ImageButton btn_addAdmin;
    @BindView(R.id.btn_transfer_data)
    ImageButton btn_transfer_data;
    @BindView(R.id.btn_push_data)
    ImageButton btn_push_data;
    @BindView(R.id.btn_push_current)
    ImageButton btn_push_current;

    @BindView(R.id.sp_crlState)
    Spinner sp_crlState;
    @BindView(R.id.et_crlFirstName)
    EditText et_crlName;
    @BindView(R.id.et_crlLastName)
    EditText et_crlLastName;
    @BindView(R.id.et_crlMobNumber)
    EditText et_crlMobNumber;
    @BindView(R.id.et_crlMailId)
    EditText et_crlMailId;
    @BindView(R.id.et_crlUserName)
    EditText et_crlUserName;
    @BindView(R.id.et_crlPassword)
    EditText et_crlPassword;

    public static ProgressDialog progress;
    Typeface font;
    List<String> statelist;
    VillageDBHelper vDatabase;
    ArrayAdapter<String> dataAdapter;
    Boolean adminFlag = false;
    SdCardPath ex_path;
    String sdCardPathString, deviceID, transferFileName, pushFileName, pushAPI;
    BluetoothAdapter btAdapter;
    StudentDBHelper studentDBHelper;
    CrlDBHelper crlDBHelper;
    SessionDBHelper sessionDBHelper;
    LevelDBHelper levelDBHelper;
    ArrayList<String> path = new ArrayList<String>();
    File[] filesForBackup;
    static boolean sentFlag = false;
    boolean currentPush = false;
    int cnt = 0,allFiles=0;
    int [] fileCount;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        pushFileName = "KKSUsage-";
        pushAPI = "http://www.story.openiscool.org/api/kahaniya/pushdata";
//        pushAPI = "http://www.hlearning.openiscool.org/api/datapush/pushusage";

        ex_path = new SdCardPath(AdminConsole.this);
        sdCardPathString = ex_path.getSdCardPath();

        studentDBHelper = new StudentDBHelper(this);
        crlDBHelper = new CrlDBHelper(this);
        levelDBHelper = new LevelDBHelper(this);
        sessionDBHelper = new SessionDBHelper(this);

        deviceID = new StatusDBHelper(this).getValue("DeviceID");

        addStatesData();
        btn_addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_menu.setVisibility(View.GONE);
                ll_admin_addnew.setVisibility(View.VISIBLE);
                    adminFlag = true;
            }
        });

        btn_ac_submitunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName, lName, mNumber, mailID, uName, password, state;
                fName = et_crlName.getText().toString();
                lName = et_crlLastName.getText().toString();
                mNumber = et_crlMobNumber.getText().toString();
                mailID = et_crlMailId.getText().toString();
                uName = et_crlUserName.getText().toString();
                password = et_crlPassword.getText().toString();
                state = sp_crlState.getSelectedItem().toString();

                if ((fName.length() != 0) && (lName.length() != 0) && (mNumber.length() == 10) && (uName.length() != 0) && (password.length() != 0) && (!state.equals("-- Select State --"))) {
                    Toast.makeText(AdminConsole.this, "Submitted!!!", Toast.LENGTH_SHORT).show();
                    insertInCrlTable(fName, lName, mNumber, uName, password, state, mailID);
                    clearForm(ll_adminform);
                } else {
                    Toast.makeText(AdminConsole.this, "Enter proper details!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_push_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pushToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void pushToServer() throws IOException, ExecutionException, InterruptedException {

        cnt=0;
        allFiles=0;

        // Checking Internet Connection
        SyncUtility syncUtility = new SyncUtility(this);

        if (SyncUtility.isDataConnectionAvailable(this)) {

            Toast.makeText(AdminConsole.this, "Connected to the Internet !!!", Toast.LENGTH_SHORT).show();

            //Moving to Receive usage
            String path;
            if (currentPush) {
                path = Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/SelfUsageJsons";
            } else {
                path = Environment.getExternalStorageDirectory().toString() + "/Bluetooth";
            }

            String destFolder = Environment.getExternalStorageDirectory() + "/.KKSInternal/JsonsBackup";

            Log.d("path", "pushToServer: " + path);

            File blueToothDir = new File(path);
            if (!blueToothDir.exists() && !currentPush) {
                Toast.makeText(this, "Bluetooth folder does not exist", Toast.LENGTH_SHORT).show();
            } else {

                progress = new ProgressDialog(AdminConsole.this);
                progress.setMessage("Please Wait...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                File[] files = blueToothDir.listFiles();
                filesForBackup = blueToothDir.listFiles();

                for(int i=0; i<files.length;i++) {
                    if (files[i].getName().contains(pushFileName))
                        allFiles++;
                }
                        fileCount = new int[files.length];
                Toast.makeText(this, "Pushing data to server Please wait...", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < files.length; i++) {
                  /*  cnt++;*/
                    if (files[i].getName().contains(pushFileName)) {
                        try {
                            startPushing(convertToString(files[i]), syncUtility, i, destFolder);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                WeakHandler handler = new WeakHandler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (cnt == 0) {
                                progress.dismiss();
                                Toast.makeText(AdminConsole.this, "No files available !!!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        } else {
            Toast.makeText(AdminConsole.this, "Please Connect to the Internet !!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void startPushing(String jasonDataToPush, SyncUtility syncUtility, int fileNo, String destinationFolder) throws Exception {
        ArrayList<String> arrayListToTransfer = new ArrayList<String>();

        arrayListToTransfer.add(jasonDataToPush);

        //Log.d("metadata :::", metadata);
        Log.d("pushedJson :::", jasonDataToPush);

        new AsyncTaskRunner(syncUtility, jasonDataToPush, fileNo, destinationFolder).execute();
    }

    public String convertToString(File file) throws IOException {
        int length = (int) file.length();
        FileInputStream in = null;
        byte[] bytes = new byte[length];
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        String contents = new String(bytes);
        return contents;
    }

    public static void fileCutPaste(File toMove, String destFolder) {
        try {
            File destinationFolder = new File(destFolder);
            File destinationFile = new File(destFolder + "/" + toMove.getName());
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }
            FileInputStream fileInputStream = new FileInputStream(toMove);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);

            int bufferSize;
            byte[] bufffer = new byte[512];
            while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                fileOutputStream.write(bufffer, 0, bufferSize);
            }
            toMove.delete();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_transfer_data)
    public void transferDataViaBluetooth() {
        // Generate Json file
        createJsonforTransfer();
        //************************** integrate push data code here********************/
        progress = new ProgressDialog(AdminConsole.this);
        progress.setMessage("Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        TransferFile(transferFileName);
    }

    @OnClick(R.id.btn_push_current)
    public void pushCurrentTabData() {
        currentPush = true;
        createJsonforTransfer();
    }


    public void createJsonforTransfer() {
        //we will push logs and scores directly to the server

        ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
        List<Score> scores = scoreDBHelper.GetAll();

/*        if (scores == null) {
        } else if (scores.size() == 0) {
            // No Score No Transfer
        } else {*/
        try {

            JSONArray scoreData = new JSONArray(),
                    attendanceData = new JSONArray(),
                    studentData = new JSONArray(),
                    crlData = new JSONArray(),
                    levelData = new JSONArray(),
                    sessionData = new JSONArray();

            for (int i = 0; i < scores.size(); i++) {
                JSONObject _obj = new JSONObject();
                Score _score = scores.get(i);

                try {
                    _obj.put("SessionID", _score.SessionID);
                    // _obj.put("PlayerID",_score.PlayerID);
                    _obj.put("StudentID", _score.StudentID);
                    _obj.put("DeviceID", _score.DeviceID);
                    _obj.put("ResourceID", _score.ResourceID);
                    _obj.put("QuestionID", _score.QuestionId);
                    _obj.put("ScoredMarks", _score.ScoredMarks);
                    _obj.put("TotalMarks", _score.TotalMarks);
                    _obj.put("StartDateTime", _score.StartDateTime);
                    _obj.put("EndDateTime", _score.EndDateTime);
                    _obj.put("Level", _score.Level);
                    scoreData.put(_obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            {

                AttendanceDBHelper attendanceDBHelper1 = new AttendanceDBHelper(this);
                attendanceData = attendanceDBHelper1.getAll();

                if (attendanceData == null) {
                } else {
                    for (int i = 0; i < attendanceData.length(); i++) {
                        JSONObject jsonObject = attendanceData.getJSONObject(i);
                        jsonObject.put("SessionID", jsonObject.get("SessionID"));
                        jsonObject.put("StudentID", jsonObject.get("StudentID"));
                    }

                    //pravin
                    //For New Students data
                    List<Student> studentsList = studentDBHelper.GetAllNewStudents();
                    Log.d("student_list_size::", String.valueOf(studentDBHelper.GetAllNewStudents().size()));
                    JSONObject studentObj;
                    if (studentData != null) {
                        for (int i = 0; i < studentsList.size(); i++) {
                            studentObj = new JSONObject();
                            studentObj.put("StudentID", studentsList.get(i).StudentID);
                            studentObj.put("StudentUID", studentsList.get(i).StudentUID == null ? "" : studentsList.get(i).StudentUID);
                            studentObj.put("FirstName", studentsList.get(i).FirstName);
                            studentObj.put("MiddleName", studentsList.get(i).MiddleName);
                            studentObj.put("LastName", studentsList.get(i).LastName);
                            studentObj.put("Gender", studentsList.get(i).Gender);
                            studentObj.put("regDate", studentsList.get(i).regDate);
                            studentObj.put("Age", studentsList.get(i).Age);
                            studentObj.put("villageName", studentsList.get(i).villageName);
                            studentObj.put("newFlag", studentsList.get(i).newFlag);
                            studentObj.put("DeviceId", studentsList.get(i).DeviceId);
                            studentData.put(studentObj);
                        }
                    }

                    //pravin
                    //For New Crls data
                    List<Crl> crlsList = crlDBHelper.GetAllNewCrl();
                    JSONObject crlObj;
                    if (crlData != null) {
                        for (int i = 0; i < crlsList.size(); i++) {
                            crlObj = new JSONObject();
                            crlObj.put("CRLId", crlsList.get(i).CRLId);
                            crlObj.put("FirstName", crlsList.get(i).FirstName);
                            crlObj.put("LastName", crlsList.get(i).LastName);
                            crlObj.put("UserName", crlsList.get(i).UserName);
                            crlObj.put("Password", crlsList.get(i).Password);
                            crlObj.put("ProgramId", crlsList.get(i).ProgramId);
                            crlObj.put("Mobile", crlsList.get(i).Mobile);
                            crlObj.put("State", crlsList.get(i).State);
                            crlObj.put("Email", crlsList.get(i).Email);
                            crlObj.put("CreatedBy", crlsList.get(i).CreatedBy);
                            crlObj.put("newCrl", !crlsList.get(i).newCrl);
                            crlData.put(crlObj);
                        }
                    }

                    List<KksSession> sessionList = sessionDBHelper.GetAllSession();
                    JSONObject sessionObj;
                    if (sessionData != null) {
                        for (int i = 0; i < sessionList.size(); i++) {
                            sessionObj = new JSONObject();
                            sessionObj.put("SessionID", sessionList.get(i).SessionID);
                            sessionObj.put("fromDate", sessionList.get(i).fromDate);
                            sessionObj.put("toDate", sessionList.get(i).toDate);
                            sessionData.put(sessionObj);
                        }
                    }

                    List<Level> levelList = levelDBHelper.GetAllLevelData();
                    JSONObject levelObj;
                    if (levelData != null) {
                        for (int i = 0; i < levelList.size(); i++) {
                            levelObj = new JSONObject();
                            levelObj.put("StudentID", levelList.get(i).getStudentID());
                            levelObj.put("BaseLevel", levelList.get(i).getBaseLevel());
                            levelObj.put("CurrentLevel", levelList.get(i).getCurrentLevel());
                            levelObj.put("UpdatedDate", levelList.get(i).getUpdatedDate());
                            levelData.put(levelObj);
                        }
                    }

                    JSONObject metaDataObj = new JSONObject();
                    metaDataObj.put("ScoreCount", scores.size());
                    metaDataObj.put("AttendanceCount", attendanceData.length());
                    metaDataObj.put("NewStudentsCount", studentData.length());
                    metaDataObj.put("NewCrlsCount", crlData.length());
                    metaDataObj.put("LevelsCount", levelData.length());
                    metaDataObj.put("SessionsCount", sessionData.length());
                    metaDataObj.put("TransId", KksApplication.getUniqueID());
                    metaDataObj.put("DeviceId", deviceID);
                    metaDataObj.put("MobileNumber", "0");

                    String requestString = "{ \"metadata\": " + metaDataObj +
                            ", \"scoreData\": " + scoreData +
                            ", \"attendanceData\": " + attendanceData +
                            ", \"newStudentsData\": " + studentData +
                            ", \"newCrlsData\": " + crlData +
                            ", \"levelsData\": " + levelData +
                            ", \"sessionsData\": " + sessionData + "}";
                    transferFileName = "KKSUsage-" + KksApplication.getUniqueID().toString();
                    WriteSettings(this, requestString, transferFileName);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //      }
    }

    public void TransferFile(String filename) {
        // progress.dismiss();
        this.filename = filename;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "This device doesn't give bluetooth support.", Toast.LENGTH_LONG).show();
        } else {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 2000);
            startActivityForResult(discoveryIntent, 1);
        }
    }

    // Creating file in Transferred Usage
    public void WriteSettings(Context context, String data, String fName) {

        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try {
            String MainPath;
            if (currentPush)
                MainPath = Environment.getExternalStorageDirectory() + "/.KKSInternal/SelfUsageJsons/" + fName + ".json";
            else
                MainPath = Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + fName + ".json";
            File file = new File(MainPath);
            try {
                path.add(MainPath);
                fOut = new FileOutputStream(file);
                osw = new OutputStreamWriter(fOut);
                osw.write(data);
                osw.flush();
                osw.close();
                fOut.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentPush) {
                pushToServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Settings not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000 && requestCode == 1) {
            String packageName = "", className = "";
            Boolean found = false;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String f = Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + filename + ".json";
            File file = new File(f);
            int x = 0;
            if (file.exists()) {
                PackageManager pm = getPackageManager();
                List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
                if (appsList.size() > 0) {

                    for (ResolveInfo info : appsList) {
                        packageName = info.activityInfo.packageName;
                        if (packageName.equals("com.android.bluetooth")) {
                            className = info.activityInfo.name;
                            found = true;
                            break;// found
                        }
                    }
                    if (!found) {
                        Toast.makeText(this, "Bluetooth not in list", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Uri fileURI = FileProvider.getUriForFile(
                                    AdminConsole.this,
                                    AdminConsole.this.getApplicationContext()
                                            .getPackageName() + ".provider", file);
                            Log.d("filename::", fileURI + "");
                            intent.putExtra(Intent.EXTRA_STREAM, fileURI);
                        } else {
                            intent.setType("text/plain");
                            Log.d("filename::", Uri.fromFile(file) + "");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        }
                        intent.setClassName(packageName, className);
                        startActivityForResult(intent, 3);
                    }
                }
            }
        } else if (requestCode == 3) {
            filename = "";
            progress.dismiss();
            clearRecordsOrNot();
        } else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "File not found in UsageJsons folder", Toast.LENGTH_LONG).show();
        }
    }

    private void clearRecordsOrNot() {

        final String dialogTitle, msgQuestion, negativeMsg,positiveMsg,successToast,failedToast;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AdminConsole.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);//Theme_Material_Dialog_Alert,Theme_DeviceDefault_Dialog_Alert,Theme_DeviceDefault_Light_DarkActionBar
        } else {
            builder = new AlertDialog.Builder(AdminConsole.this);
        }
        if(currentPush){
            dialogTitle = "<font color='#2E96BB'>PUSH SUCCESSFUL ?</font>";
            msgQuestion = "CLEAR RECORDS IF SUCCESSFUL????\n\n If you click on 'PUSH SUCCESSFUL' then Data will be Deleted!!!\n If you click 'PUSH FAILED' the Data will persist";
            negativeMsg="PUSH FAILED";
            positiveMsg="PUSH SUCCESSFUL";
            successToast = "DATA CLEARED";
            failedToast = "DATA NOT CLEAR ";
        }
        else{
            dialogTitle = "<font color='#2E96BB'>SHARE SUCCESSFUL ?</font>";
            msgQuestion = "If you see 'File received successfully' message on master tab,\nClick SHARE SUCCESSFUL.\n\nWARNING : If you click SHARE SUCCESSFUL without receiving\n data on master tab, Data will be LOST !!!";
            negativeMsg="SHARE FAILED";
            positiveMsg="SHARE SUCCESSFUL";
            successToast = "File Transferred Successfully!!!";
            failedToast = "File Not Transferred !!!";
        }
        builder.setTitle(Html.fromHtml(""+dialogTitle))
                .setMessage(""+msgQuestion)
                .setNegativeButton(""+negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Toast.makeText(AdminConsole.this, ""+failedToast, Toast.LENGTH_SHORT).show();
                        //Delete
                        if(!currentPush){
                            File f = new File(Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + transferFileName+ ".json");
                            f.delete();
                        }
                    }
                })

                .setPositiveButton(""+positiveMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        new ScoreDBHelper(AdminConsole.this).DeleteAll();
                        BackupDatabase.backup(AdminConsole.this);
                        Toast.makeText(AdminConsole.this, ""+successToast, Toast.LENGTH_SHORT).show();
                        if(!currentPush){
                            File f = new File(Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + transferFileName+ ".json");
                            String destFolder = Environment.getExternalStorageDirectory() + "/.KKSInternal/JsonsBackup";
                            fileCutPaste(f,destFolder);
                        }
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }


    public void insertInCrlTable(String fName, String lName, String mNumber, String uName, String password, String state, String mailID) {
        try {
            CrlDBHelper crlDBHelper = new CrlDBHelper(this);
            Crl crl = new Crl();
            crl.setCRLId(UUID.randomUUID().toString());
            crl.setEmail(mailID);
            crl.setFirstName(fName);
            crl.setLastName(lName);
            crl.setMobile(mNumber);
            crl.setUserName(uName);
            crl.setPassword(password);
            crl.setState(state);
            crl.setProgramId(2);
            crl.newCrl = true;
            crl.CreatedBy = "";

            crlDBHelper.insertData(crl);
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStatesData() {
        List<String> statelist = new ArrayList<>();
        statelist.add("-- Select State --");
        statelist.add("Andhra Pradesh");
        statelist.add("Assam");
        statelist.add("Bengal");
        statelist.add("Gujarat");
        statelist.add("Karnataka");
        statelist.add("Madhya Pradesh");
        statelist.add("Maharashtra");
        statelist.add("Orissa");
        statelist.add("Punjab");
        statelist.add("Rajasthan");
        statelist.add("Tamil Nadu");
        statelist.add("Telangana");

/*        for (int i = 0; i < 29; i++)
            statelist.add(AllGroups.get(i).GroupName);*/

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, statelist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_crlState.setAdapter(dataAdapter);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0, true);
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }

    @Override
    public void onBackPressed() {
        if (adminFlag) {
            ll_admin_menu.setVisibility(View.VISIBLE);
            ll_admin_addnew.setVisibility(View.GONE);
            adminFlag = false;
        } else
            super.onBackPressed();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        //        ProgressDialog progressDialog;
        SyncUtility syncUtility;
        String jasonDataToPush, folderForBackup;
        int currentFileNo;


        public AsyncTaskRunner(SyncUtility syncUtility, String jasonDataToPush, int currentFileNo, String folderForBackup) {
            this.syncUtility = syncUtility;
            this.jasonDataToPush = jasonDataToPush;
            this.currentFileNo = currentFileNo;
            this.folderForBackup = folderForBackup;
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                String pushResult = syncUtility.sendData(pushAPI, jasonDataToPush);
                Log.d("pushResult", pushResult);
                if (pushResult.equalsIgnoreCase("success")) {
                    ////// incriment count
                    cnt++;
                    fileCutPaste(filesForBackup[currentFileNo], folderForBackup);


                    if(cnt == allFiles)
                        sentFlag = true;
                }

            } catch (Exception e) {
                /*cnt++;*/
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            if (cnt == allFiles) {
                if(currentPush)
                    clearRecordsOrNot();
                currentPush = false;
                progress.dismiss();

                // Show count on screen cnt
                Toast.makeText(AdminConsole.this, "Data succesfully pushed to the server !!!", Toast.LENGTH_LONG).show();
                Toast.makeText(AdminConsole.this, "Files moved in pushedUsage folder !!!", Toast.LENGTH_SHORT).show();

            } else if (!sentFlag && (cnt == allFiles) ) {
                progress.dismiss();
                Toast.makeText(AdminConsole.this, "Data NOT pushed to the server !!!", Toast.LENGTH_LONG).show();
            }
            //execution of result of Long time consuming operation
            //progressDialog.dismiss();
            //finalResult.setText(result);
            //Log.d("pushResult",result.toString());
        }


        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(this,
//                    "ProgressDialog",
//                    "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
//            finalResult.setText(text[0]);

        }
    }
}
