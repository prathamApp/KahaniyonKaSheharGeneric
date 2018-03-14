package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.fragments.StoryFragment;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import static com.example.pefpr.kahaniyonkashehar.fragments.StoryFragment.mp;

public class LevelDecider extends AppCompatActivity {

    public static String StudentID = "12345";
    public static float percentageForStory, percentageForWordRead, percentageForImageRecognition;
    public static String storyId;
    String storyData;
    Boolean backFlag = false, mediaFlg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_decider);
        getSupportActionBar().hide();

        Intent i = this.getIntent();
        storyData = i.getStringExtra("storyData");
        storyId = i.getStringExtra("storyId");
        StudentID = i.getStringExtra("StudentID");
        Log.d("bbbbb", "storyId " + storyId);

        startStory();
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

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {


        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }

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