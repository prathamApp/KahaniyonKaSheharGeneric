package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.fragments.StoryFragment;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.ScoreDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import static com.example.pefpr.kahaniyonkashehar.fragments.StoryFragment.mp;

public class LevelDecider extends BaseActivity {

    public static String StudentID = "12345";
    public static float percentageForStory, percentageForWordRead, percentageForImageRecognition;
    public static String storyId;
    String storyData;
    static Context context;
    public static String questionStartDate, currentStorySession;
    Boolean backFlag = false, mediaFlg = false;
    StatusDBHelper stsDBHelper;
    SessionDBHelper sesDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_decider);
        getSupportActionBar().hide();
        context = LevelDecider.this;
        Intent i = this.getIntent();
        storyData = i.getStringExtra("storyData");
        storyId = i.getStringExtra("storyId");
        StudentID = i.getStringExtra("StudentID");
        Log.d("bbbbb", "storyId " + storyId);
        stsDBHelper = new StatusDBHelper(context);
        sesDBHelper = new SessionDBHelper(context);

        String sessionID = KksApplication.getUniqueID().toString();
        currentStorySession = storyId+"_SS_"+sessionID+"_CS_"+ stsDBHelper.getValue("CurrentSession");

        stsDBHelper.Update("CurrentStorySession", "" + currentStorySession);
        sesDBHelper.addToSessionTable(currentStorySession,KksApplication.getCurrentDateTime(),"NA");
        BackupDatabase.backup(this);
        startStory();
    }

    public static void addStoryScore(int resQuesId, int scorefromQuestion) {

        try {
            StatusDBHelper statusDBHelper = new StatusDBHelper(context);
            ScoreDBHelper scoreDBHelper = new ScoreDBHelper(context);

            Score score = new Score();

            score.setSessionID(statusDBHelper.getValue("CurrentSession"));
            score.setResourceID(storyId);
            score.setQuestionId(resQuesId);
            score.setScoredMarks(scorefromQuestion);
            score.setTotalMarks(10);
            score.setStudentID(LevelDecider.StudentID);

            score.setStartDateTime(questionStartDate);

            String deviceId = statusDBHelper.getValue("DeviceID");
            score.setDeviceID(deviceId.equals(null) ? "0000" : deviceId);
            score.setEndDateTime(KksApplication.getCurrentDateTime());
            score.setLevel(1);
            Boolean _wasSuccessful = scoreDBHelper.Add(score);

            BackupDatabase.backup(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startStory() {
        Bundle bundle = new Bundle();
        bundle.putString("storyData", storyData);
        bundle.putString("storyId", storyId);
        PD_Utility.showFragment(LevelDecider.this, new StoryFragment(), R.id.ll_leveldecider,
                bundle, StoryFragment.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaFlg) {
            startStory();
        }
        if (backFlag)
            onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (StoryFragment.handler != null)
                StoryFragment.handler.removeCallbacksAndMessages(null);
            if (StoryFragment.colorChangeHandler != null)
                StoryFragment.colorChangeHandler.removeCallbacksAndMessages(null);
            if (StoryFragment.soundStopHandler != null)
                StoryFragment.soundStopHandler.removeCallbacksAndMessages(null);
            if (StoryFragment.mp.isPlaying()) {
                StoryFragment.mp.stop();
                mediaFlg = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {


        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        StatusDBHelper statusDBHelper = new StatusDBHelper(this);
        SessionDBHelper sessionDBHelper = new SessionDBHelper(this);
        String curStrSession = statusDBHelper.getValue("CurrentStorySession");
        sessionDBHelper.UpdateToDate(""+curStrSession, KksApplication.getCurrentDateTime());
        BackupDatabase.backup(this);


        /*StoryFragment myStoryFragment = (StoryFragment) getSupportFragmentManager().findFragmentByTag(StoryFragment.class.getSimpleName());

        getSupportFragmentManager().popBackStack();
        if (myStoryFragment != null && myStoryFragment.isVisible()) {
            super.onBackPressed();
        }*/
        this.finish();
        Intent intent = new Intent(LevelDecider.this, StoriesDisplay.class);
        intent.putExtra("StudentID", getIntent().getStringExtra("StudentID"));
        startActivity(intent);
    }
}