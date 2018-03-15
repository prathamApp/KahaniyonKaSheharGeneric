package com.example.pefpr.kahaniyonkashehar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.TabUsageAdapter;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Usage;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class TabUsage extends BaseActivity {

    SessionDBHelper sessionDBHelper;
    AttendanceDBHelper attendanceDBHelper;
    ArrayList<JSONObject> usageData;
    ArrayList<Usage> listForAdapter;
    String hours, minutes, seconds,sdCardPathString;
    ArrayAdapter<Usage> listAdapter;
    ListView listView;
    Usage usage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_usage);
        getSupportActionBar().hide();

        SdCardPath ex_path = new SdCardPath(this);
        sdCardPathString = ex_path.getSdCardPath();
        listView = findViewById(R.id.list_view);
        sessionDBHelper = new SessionDBHelper(this);
        usageData = sessionDBHelper.getStudentUsageData();
        listForAdapter = new ArrayList<Usage>();

        try {
            for (int i = 0; i < usageData.size(); i++) {
                int currentSec = usageData.get(i).getInt("time");
                String FName = usageData.get(i).getString("FirstName");

                hours =  String.format("%02d",currentSec / 3600)+"h";
                minutes = String.format("%02d",(currentSec % 3600) / 60)+"m";
                seconds = String.format("%02d",currentSec % 60)+"s";
                String currentTimeStamp = hours+" : "+minutes+" : "+seconds;
                String imagePath = sdCardPathString+"StoryData/StoryCover/story1.jpg";
                Log.d("TabUsageActivity", "Name: "+FName+"     TimeStamp: "+currentTimeStamp);
                listForAdapter.add(new Usage(FName,currentTimeStamp));
            }

            listAdapter = new TabUsageAdapter(this,listForAdapter);
            listView.setAdapter(listAdapter);

        }catch (Exception e){e.printStackTrace();}

    }

}
