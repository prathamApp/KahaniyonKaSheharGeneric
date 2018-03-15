package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.contentplayer.TextToSpeechCustom;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.util.BackgroundManagement;
import com.example.pefpr.kahaniyonkashehar.util.SDCardUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoryOrGame extends AppCompatActivity {

    String StudentName = "";
    TextToSpeechCustom tts;
    @BindView(R.id.tv_welcomeStudent)
    TextView welcomeStudent;
    BackgroundManagement backgroundManagement;
    //    @BindView(R.id.btn_story)
//    ImageButton btn_story;
//    @BindView(R.id.btn_game)
//    ImageButton btn_game;
//    @BindView(R.id.btn_sdcard)
//    ImageButton btn_sdcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_or_game);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        backgroundManagement = new BackgroundManagement(this);
//        startService(new Intent(this, AppExitService.class));

        tts = new TextToSpeechCustom(this, 0.6f);

        Intent i = getIntent();
        StudentName = i.getStringExtra("StudentName");
        if (StudentName != null) {
            final String ttsString = "Welcome " + StudentName + "!!!";
            welcomeStudent.setText(ttsString);
        }
    }

    @OnClick(R.id.btn_story)
    public void displayStories() {
        Intent intent = new Intent(StoryOrGame.this,StoriesDisplay.class);
        intent.putExtra("StudentID",getIntent().getStringExtra("StudentID"));
        startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.btn_game)
    public void displayGames() {
        Intent intent = new Intent(StoryOrGame.this, GamesDisplay.class);
        intent.putExtra("StudentID",getIntent().getStringExtra("StudentID"));
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(this);
        SessionDBHelper sessionDBHelper = new SessionDBHelper(this);

        String curSession = statusDBHelper.getValue("CurrentSession");
        sessionDBHelper.UpdateToDate(""+curSession, KksApplication.getCurrentDateTime());
        BackupDatabase.backup(this);
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundManagement.ActivityOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundManagement.ActivityResumed();
    }
}
