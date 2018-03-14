package com.example.pefpr.kahaniyonkashehar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;

public class TabUsage extends AppCompatActivity {

    SessionDBHelper sessionDBHelper;
    AttendanceDBHelper attendanceDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_usage);

        attendanceDBHelper = new AttendanceDBHelper(this);
        sessionDBHelper = new SessionDBHelper(this);
        sessionDBHelper.getStudentUsageData();

    }

}
