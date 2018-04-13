package com.example.pefpr.kahaniyonkashehar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.StoriesAdapter;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.interfaces.StoryClicked;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.StoriesView;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.pefpr.kahaniyonkashehar.KksApplication.getLanguage;


public class StoriesDisplay extends BaseActivity implements StoryClicked {

    boolean flag = false;
    private RecyclerView recyclerView;
    private StoriesAdapter storiesAdapter;
    private List<StoriesView> StoriesViewList;
    TextView tv_todaysletters, tv_pg_title;
    SdCardPath ex_path;
    String sdCardPathString, studentID;
    public static String storiesDispLang;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_select);
        getSupportActionBar().hide();

        studentID = getIntent().getStringExtra("StudentID");


        recyclerView = (RecyclerView) findViewById(R.id.attendnce_recycler_view);
        ex_path = new SdCardPath(StoriesDisplay.this);
        sdCardPathString = ex_path.getSdCardPath();

        tv_todaysletters = findViewById(R.id.tv_todaysletters);
        tv_pg_title = findViewById(R.id.tv_pg_title);
        tv_pg_title.setText("Stories");
        tv_pg_title.setTextColor(Color.parseColor("#fdca00"));

        StoriesViewList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(this, StoriesViewList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(15), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storiesAdapter);

        String appLang = getLanguage();

        if ( appLang.equalsIgnoreCase("Punjabi")) {
            storiesDispLang = "pun";
        } else if (appLang.equalsIgnoreCase("Odiya")) {
            storiesDispLang = "odiya";
        } else if (appLang.equalsIgnoreCase("Gujarati")) {
            storiesDispLang = "guj";
        }else
            storiesDispLang = "NA";


        prepareStories();

    }

    private void prepareStories() {
        JSONArray storiesJA = fetchStory("Stories");

        try {
            for (int i = 0; i < storiesJA.length(); i++) {
                StoriesView storiesView = new StoriesView();
                String sName, sId, sThumbnail, StoryData;
                sName = storiesView.storyName = storiesJA.getJSONObject(i).getString("nodeTitle");
                sId = storiesView.storyId = storiesJA.getJSONObject(i).getString("resourceId");
                sThumbnail = storiesView.storyThumbnail = sdCardPathString + "/StoryData/" + storiesJA.getJSONObject(i).getString("nodeImage");
                StoryData = storiesJA.getJSONObject(i).getString("nodelist");
                StoriesView storyListView = new StoriesView("" + sName, "" + sId, sThumbnail, StoryData);
                StoriesViewList.add(storyListView);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray fetchStory(String jasonName) {
        JSONArray returnStoryNavigate = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString + "JsonFiles/" + jasonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObj = new JSONObject(jsonStr);
            returnStoryNavigate = jsonObj.getJSONArray("nodelist");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStoryNavigate;
    }


    @Override
    public void onStoryClicked(int position, String id, String storyData, String storyId) {
        this.finish();
        Intent mainNew = new Intent(StoriesDisplay.this, LevelDecider.class);
        mainNew.putExtra("storyData", storyData);
        mainNew.putExtra("storyId", storyId);
        mainNew.putExtra("StudentID", studentID);
        startActivity(mainNew);
    }

    @Override
    public void onBackPressed() {

        if (new LevelDBHelper(this).GetStudentLevelByStdID(studentID) == null) {
            StatusDBHelper statusDBHelper = new StatusDBHelper(this);
            SessionDBHelper sessionDBHelper = new SessionDBHelper(this);

            String curSession = statusDBHelper.getValue("CurrentSession");
            sessionDBHelper.UpdateToDate(""+curSession, KksApplication.getCurrentDateTime());
            BackupDatabase.backup(this);
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
        else{
            Intent intent = new Intent(this, StoryOrGame.class);
            intent.putExtra("StudentID", studentID);
            intent.putExtra("StudentName", new StudentDBHelper(this).getStudentName(studentID));
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + avatar) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + avatar) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


}
