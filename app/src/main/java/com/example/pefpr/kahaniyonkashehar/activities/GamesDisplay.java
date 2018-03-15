package com.example.pefpr.kahaniyonkashehar.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.GamesAdapter;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.contentplayer.TextToSpeechCustom;
import com.example.pefpr.kahaniyonkashehar.contentplayer.WebViewActivity;
import com.example.pefpr.kahaniyonkashehar.interfaces.GameClicked;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.ModalGames;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.pefpr.kahaniyonkashehar.KksApplication.getLanguage;


public class GamesDisplay extends BaseActivity implements GameClicked {

    private RecyclerView recyclerView;
    private GamesAdapter gamesAdapter;
    private List<ModalGames> gamesViewList, parentGameViewList;
    String[] levels;
    TextView tv_todaysletters,tv_pg_title;
    ModalGames modalGames;
    JSONArray gameJsonArray, currentLevel;
    List<ModalGames> newItems;
    ArrayList<ModalGames> pos = new ArrayList<>();
    public static String aajKeLetters;
    List<String> letterslist;
    String gameLevel,studentID;
    SdCardPath ex_path;
    String sdCardPathString;
    public static String gameDispLang="NA";
    TextToSpeechCustom tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_select);
        getSupportActionBar().hide();
        studentID = getIntent().getStringExtra("StudentID");
        ex_path = new SdCardPath(GamesDisplay.this);
        sdCardPathString = ex_path.getSdCardPath();
        tts = new TextToSpeechCustom(this, 0.6f);


        String appLang = getLanguage();

        if ( appLang.equalsIgnoreCase("Punjabi")) {
            gameDispLang = "pun";
        } else if (appLang.equalsIgnoreCase("Odiya")) {
            gameDispLang = "odiya";
        } else if (appLang.equalsIgnoreCase("Gujarati")) {
            gameDispLang = "guj";
        }else
            gameDispLang = "NA";


        recyclerView = (RecyclerView) findViewById(R.id.attendnce_recycler_view);
        tv_todaysletters = findViewById(R.id.tv_todaysletters);
        tv_pg_title = findViewById(R.id.tv_pg_title);
        tv_pg_title.setText("Games");
        tv_pg_title.setTextColor(Color.parseColor("#fdca00"));

        tv_todaysletters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTodayLettersDialog();
            }
        });

        gamesViewList = new ArrayList<>();
        parentGameViewList = new ArrayList<>();

        startTodayLettersDialog();

        modalGames = fetchJsonData("Games");
        prepareGames();

        gamesAdapter = new GamesAdapter(this, gamesViewList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(15), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gamesAdapter);
        gamesAdapter.notifyDataSetChanged();
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

    private void startTodayLettersDialog() {

        JSONArray letters = fetchStory("AajKeAksher");
        final Dialog dialog = new Dialog(GamesDisplay.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.todays_letters_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Spinner dialogSpinner = dialog.findViewById(R.id.cd_sp_todaysletters);
        Button dialogOK = dialog.findViewById(R.id.cd_btn_ok);
        TextView cd_tv_title = dialog.findViewById(R.id.cd_tv_title);

        if (GamesDisplay.gameDispLang.equalsIgnoreCase("pun")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/raavi_punjabi.ttf");
            cd_tv_title.setTypeface(custom_font, Typeface.BOLD);
        } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("odiya")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/lohit_oriya.ttf");
            cd_tv_title.setTypeface(custom_font, Typeface.BOLD);
        } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("guj")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/muktavaani_gujarati.ttf");
            cd_tv_title.setTypeface(custom_font, Typeface.BOLD);
        }


        String myLabel="अक्षर चुनों";

        JSONArray NativeLabels = fetchStory("NativeLabels");
        try {
            for (int i = 0; i < NativeLabels.length(); i++) {
                if(NativeLabels.getJSONObject(i).getString("nodeTitle").equalsIgnoreCase("choose_letters"))
                    myLabel = NativeLabels.getJSONObject(i).getString("nodeData");
            }
        }catch (Exception e){e.printStackTrace();}

        cd_tv_title.setText(""+myLabel);


        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });


        letterslist = new ArrayList<String>();
        try {
            for (int i = 0; i < letters.length(); i++)
                letterslist.add(letters.getJSONObject(i).getString("resourceDesc"));
        }catch (Exception e){ e.printStackTrace(); }


        ArrayAdapter dataAdapter = new ArrayAdapter<String>(GamesDisplay.this, R.layout.letter_spinner_layout, letterslist);
        dialogSpinner.setAdapter(dataAdapter);

        Collections.shuffle(letterslist);

        dialogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                aajKeLetters = letterslist.get(position);
                if (GamesDisplay.gameDispLang.equalsIgnoreCase("pun")) {
                    Typeface custom_font = Typeface.createFromAsset(GamesDisplay.this.getAssets(), "fonts/raavi_punjabi.ttf");
                    tv_todaysletters.setTypeface(custom_font, Typeface.BOLD);
                } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("odiya")) {
                    Typeface custom_font = Typeface.createFromAsset(GamesDisplay.this.getAssets(), "fonts/lohit_oriya.ttf");
                    tv_todaysletters.setTypeface(custom_font, Typeface.BOLD);
                } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("guj")) {
                    Typeface custom_font = Typeface.createFromAsset(GamesDisplay.this.getAssets(), "fonts/muktavaani_gujarati.ttf");
                    tv_todaysletters.setTypeface(custom_font, Typeface.BOLD);
                }
                tv_todaysletters.setText(aajKeLetters);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void prepareGames() {
        try {
            gamesViewList.addAll(modalGames.getNodelist());
            pos.add(modalGames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGameClicked(int position) {

        try {
            Log.d("onCardClick", "onGameClicked: " + position);
            if (gamesViewList.get(position).getNodeType().equalsIgnoreCase("Resource")) {
                String gameID = gamesViewList.get(position).getResourceId();
                String currentGameName = gamesViewList.get(position).getNodeTitle();
                gameLevel = gamesViewList.get(position).getResourceDesc();
                File file = new File(sdCardPathString+ "Games/" + gamesViewList.get(position).getResourcePath());
                Uri path = Uri.fromFile(file);

                Intent intent = new Intent(GamesDisplay.this, WebViewActivity.class);
                intent.putExtra("path", path.toString());
                intent.putExtra("resId", gameID);
                intent.putExtra("gameLevel", gameLevel);
                intent.putExtra("currentGameName", currentGameName);
                startActivity(intent);

            } else {
                pos.add(gamesViewList.get(position));
                newItems = gamesViewList.get(position).getNodelist();
                gamesViewList.clear();
                gamesViewList.addAll(newItems);
                gamesAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (pos.size() > 1) {
            Log.d("posSize", "onBackPressed: " + pos.size());
            int tempPos = pos.size() - 2;
            newItems = pos.get(tempPos).getNodelist();
            pos.subList(tempPos + 1, pos.size()).clear();
            ;
            Log.d("posSize", "onBackPressed2: " + pos.size());
            gamesViewList.clear();
            gamesViewList.addAll(newItems);
            gamesAdapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(this, StoryOrGame.class);
            intent.putExtra("StudentID", studentID);
            intent.putExtra("StudentName", new StudentDBHelper(this).getStudentName(studentID));
            startActivity(intent);
            this.finish();
        }
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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public ModalGames fetchJsonData(String jasonName) {
        ModalGames returnModalGames = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString+"JsonFiles/"+jasonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObj = new JSONObject(new String(buffer));
            Gson gson = new Gson();
            returnModalGames = gson.fromJson(jsonObj.toString(), ModalGames.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnModalGames;
    }

}