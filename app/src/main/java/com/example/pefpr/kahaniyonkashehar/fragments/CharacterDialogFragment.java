package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.AttendenceDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.LevelDecider;
import com.example.pefpr.kahaniyonkashehar.activities.StoriesDisplay;
import com.example.pefpr.kahaniyonkashehar.animations.GifView;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Level;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterDialogFragment extends Fragment {

    @BindView(R.id.iv_character_dialog_gif)
    GifView iv_gif;
    @BindView(R.id.tv_character_dialog_question)
    TextView tv_Question;
    @BindView(R.id.tv_character_dialog_choice1)
    TextView tv_Choice1;
    @BindView(R.id.tv_character_dialog_choice2)
    TextView tv_Choice2;
    Level level;
    LevelDBHelper levelDBHelper;
    String appLang;
    SdCardPath ex_path;
    String sdCardPathString;

    public CharacterDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_dialog, container, false);
    }

    private String getLanguage() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());
        String language = statusDBHelper.getValue("AppLang");
        return language;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        levelDBHelper = new LevelDBHelper(getActivity());

        ex_path = new SdCardPath(getActivity());
        sdCardPathString = ex_path.getSdCardPath();


        appLang = getLanguage();

        if ( appLang.equals("Punjabi")) {
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raavi_punjabi.ttf");
            tv_Question.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice1.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice2.setTypeface(custom_font, Typeface.BOLD);
        } else if (appLang.equals("Odiya")) {
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lohit_oriya.ttf");
            tv_Question.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice1.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice2.setTypeface(custom_font, Typeface.BOLD);
        } else if (appLang.equals("Gujarati")) {
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muktavaani_gujarati.ttf");
            tv_Question.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice1.setTypeface(custom_font, Typeface.BOLD);
            tv_Choice2.setTypeface(custom_font, Typeface.BOLD);
        }

        iv_gif.setGifResource(R.drawable.bittu_show);

        String str_Question = "अब, और क्या करना चाहोगे?";
        String str_Story = "फिर से कहानी पढ़ोगे ?";
        String str_Game = "खेल खेलोगे?";

        JSONArray NativeLabels = fetchStory("NativeLabels");
        try {
            for (int i = 0; i < NativeLabels.length(); i++) {
                if(NativeLabels.getJSONObject(i).getString("nodeTitle").equalsIgnoreCase("FragmentQuestion"))
                    str_Question = NativeLabels.getJSONObject(i).getString("nodeData");
                if(NativeLabels.getJSONObject(i).getString("nodeTitle").equalsIgnoreCase("choice1_story"))
                    str_Story = NativeLabels.getJSONObject(i).getString("nodeData");
                if(NativeLabels.getJSONObject(i).getString("nodeTitle").equalsIgnoreCase("choice2_game"))
                    str_Game = NativeLabels.getJSONObject(i).getString("nodeData");
            }
        }catch (Exception e){e.printStackTrace();}


        tv_Question.setText(""+str_Question);
        tv_Choice1.setText(""+str_Story);
        tv_Choice2.setText(""+str_Game);

        tv_Question.setTextSize(40);
        tv_Choice1.setTextSize(35);
        tv_Choice2.setTextSize(35);

        tv_Choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PD_Utility.showFragment(getActivity(), new ImageIdentificationFragment(), R.id.ll_leveldecider,
//                        null, ImageIdentificationFragment.class.getSimpleName());
                getActivity().finish();
                Intent storiesDisp = new Intent(getActivity(), StoriesDisplay.class);
                storiesDisp.putExtra("StudentID",LevelDecider.StudentID);
                startActivity(storiesDisp);
            }
        });

        tv_Choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gamesDisp = new Intent(getActivity(), GamesDisplay.class);
                gamesDisp.putExtra("StudentID",LevelDecider.StudentID);
                startActivity(gamesDisp);
            }
        });
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



}