package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.LevelDecider;
import com.example.pefpr.kahaniyonkashehar.animations.MyBounceInterpolator;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.contentplayer.TextToSpeechCustom;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Level;
import com.example.pefpr.kahaniyonkashehar.phasedseekbar.PhasedListener;
import com.example.pefpr.kahaniyonkashehar.phasedseekbar.PhasedSeekBar;
import com.example.pefpr.kahaniyonkashehar.phasedseekbar.SimplePhasedAdapter;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pefpr.kahaniyonkashehar.activities.LevelDecider.addStoryScore;

public class ImageIdentificationFragment extends Fragment {

    @BindView(R.id.iv_image1)
    ImageView iv_image1;
    @BindView(R.id.iv_image2)
    ImageView iv_image2;
    @BindView(R.id.iv_image3)
    ImageView iv_image3;
    @BindView(R.id.iv_image4)
    ImageView iv_image4;
    @BindView(R.id.btn_repeat_question_i)
    ImageView btn_repeat_question_i;
    @BindView(R.id.psb_star)
    PhasedSeekBar psbStar;

    JSONArray questionData;
    int[] integerArray = new int[4];
    ArrayList<String> resDescriptionArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    WeakHandler handler;
    int readQuestionNo, questionCount = 0, correctCount = 0, consecutiveCount = 0;
    String ttsQuestion,resQuesId;
    TextToSpeechCustom playTTS;
    MediaPlayer mp;
    LevelDBHelper levelDBHelper;
    Boolean nextFlag = false;
    float speechRate = 1.0f;
    SdCardPath ex_path;
    String sdCardPathString,language;

    public ImageIdentificationFragment() {
    }

    private static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    private static int[] getUniqueRandomNumber(int min, int max, int numSize) {

        int[] tempArray;
        if ((max - min) > numSize) {
            tempArray = new int[numSize];
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int i = min; i < max; i++)
                list.add(new Integer(i));

            Collections.shuffle(list);
            for (int i = 0; i < numSize; i++) {
                System.out.println("===== : " + list.get(i));
                tempArray[i] = list.get(i);
            }
            return tempArray;
        } else
            return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_image_identification for this fragment
        return inflater.inflate(R.layout.fragment_image_identification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        final Resources resources = getResources();
        playTTS = new TextToSpeechCustom(getActivity(), 1.0f);
        language = getLanguage();
        if(!(language.equalsIgnoreCase("hindi")))
            psbStar.setVisibility(View.GONE);

        ex_path = new SdCardPath(getActivity());
        sdCardPathString = ex_path.getSdCardPath();


        btn_repeat_question_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQuestion(readQuestionNo);
            }
        });

        iv_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image1.startAnimation(pop);
                checkAnswer(1);
            }
        });

        iv_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image2.startAnimation(pop);
                checkAnswer(2);
            }
        });

        iv_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image3.startAnimation(pop);
                checkAnswer(3);
            }
        });

        iv_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image4.startAnimation(pop);
                checkAnswer(4);
            }
        });

        questionData = fetchStory("ImageIdentification");
        integerArray = getUniqueRandomNumber(0, questionData.length(), 4);
        ShowImages(integerArray);

        psbStar.setAdapter(new SimplePhasedAdapter(resources, new int[]{
                R.drawable.btn_star5_selector,
                R.drawable.btn_star4_selector,
                R.drawable.btn_star3_selector,
                R.drawable.btn_star2_selector,
                R.drawable.btn_star1_selector}));

        psbStar.setListener(new PhasedListener() {
            @Override
            public void onPositionSelected(int position) {

                switch (position) {
                    case 0:
                        speechRate = 1.5f;
                        break;
                    case 1:
                        speechRate = 1.0f;
                        break;
                    case 2:
                        speechRate = 0.8f;
                        break;
                    case 3:
                        speechRate = 0.5f;
                        break;
                    case 4:
                        speechRate = 0.1f;
                        break;
                    default:
                        speechRate = 1.0f;
                }
                btn_repeat_question_i.performClick();
                Log.d("STT_Position", "onPositionSelected: " + position);
            }
        });

    }

    private void checkAnswer(int imageViewNum) {
        String imageString = resDescriptionArray.get(imageViewNum - 1);
        int scorefromQuestion;

        if (imageString.equalsIgnoreCase(ttsQuestion)) {
            playMusic("StoriesAudio/correct.mp3");
            scorefromQuestion = 10;
            consecutiveCount++;
            correctCount++;
            if (consecutiveCount >= 3) {
                nextFlag = true;
            }
        } else {
            consecutiveCount = 0;
            scorefromQuestion = 0;
            playMusic("StoriesAudio/wrong.mp3");
        }

        addStoryScore(Integer.parseInt(resQuesId), scorefromQuestion);
        handler = new WeakHandler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                integerArray = getUniqueRandomNumber(0, questionData.length(), 4);
                ShowImages(integerArray);
            }
        }, 1000);
    }

    void playMusic(String fileName) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(sdCardPathString + "/StoryData/" + fileName);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowImages(int[] integerArray) {
        try {
            questionCount++;
            if (nextFlag || questionCount == 9) {
                if (nextFlag)
                    LevelDecider.percentageForImageRecognition = 100f;
                else
                    LevelDecider.percentageForImageRecognition = (((float) correctCount / 9) * 100);

//                Toast.makeText(getActivity(), "Level Percengtage" + LevelDecider.percentageForImageRecognition, Toast.LENGTH_SHORT).show();

                updateStudentLevel();
                PD_Utility.showFragment(getActivity(), new CharacterDialogFragment(), R.id.ll_leveldecider,
                        null, CharacterDialogFragment.class.getSimpleName());
            } else {
                readQuestionNo = getRandomNumber(0, 4);
                String imagePath = sdCardPathString + "/StoryData/";

                resDescriptionArray.clear();
                resIdArray.clear();
                resImageArray.clear();
                resAudioArray.clear();

                for (int i = 0; i < 4; i++) {
                    resDescriptionArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceDesc"));
                    resImageArray.add("IdentificationImages/" + questionData.getJSONObject(integerArray[i]).getString("resourceBg"));
                    resAudioArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceAudio"));
                    resIdArray.add(questionData.getJSONObject(integerArray[i]).getString("resourceId"));
                }
                Bitmap[] bitmap = {BitmapFactory.decodeFile(imagePath + resImageArray.get(0))};
                iv_image1.setImageBitmap(bitmap[0]);
                bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(1))};
                iv_image2.setImageBitmap(bitmap[0]);
                bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(2))};
                iv_image3.setImageBitmap(bitmap[0]);
                bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(3))};
                iv_image4.setImageBitmap(bitmap[0]);

                handler = new WeakHandler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LevelDecider.questionStartDate  = ""+ KksApplication.getCurrentDateTime();
                        resQuesId = resIdArray.get(readQuestionNo);
                        readQuestion(readQuestionNo);
                    }
                }, 1500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLanguage() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());
        language = statusDBHelper.getValue("AppLang");
        return language;
    }

    private void updateStudentLevel() {//todo
        Float avg, level = 0f;
        if(language.equalsIgnoreCase("hindi"))
            avg = (LevelDecider.percentageForImageRecognition + LevelDecider.percentageForWordRead + LevelDecider.percentageForStory) / 3;
        else
            avg = (LevelDecider.percentageForImageRecognition + LevelDecider.percentageForStory) / 2;
        if (avg > 0f) {
            String levelCase = String.valueOf(avg / 10);
            String splitted[] = levelCase.split("\\.");
            switch (splitted[0]) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    level = 1.2f;
                    break;
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                    level = 2.2f;
            }
        }
        LevelDBHelper levelDBHelper = new LevelDBHelper(getActivity());
        if (levelDBHelper.CheckChildLevelExists(LevelDecider.StudentID)){
            levelDBHelper.updateStudentLevel(LevelDecider.StudentID, level,""+KksApplication.getCurrentDateTime());
        }else {
            Level levelObject = new Level();
            levelObject.setStudentID(LevelDecider.StudentID);
            levelObject.setCurrentLevel(String.valueOf(level));
            levelObject.setBaseLevel("1.2");
            levelObject.setUpdatedDate(""+KksApplication.getCurrentDateTime());
            levelDBHelper.Add(levelObject, levelDBHelper.getWritableDatabase());
        }

        BackupDatabase.backup(getActivity());
    }

    public void readQuestion(int questionToRead) {
        ttsQuestion = resDescriptionArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate);
        if(language.equalsIgnoreCase("hindi"))
            playTTS.ttsFunction(ttsQuestion, "hin", speechRate);
        else
            playMusic("StoriesAudio/"+ resAudioArray.get(questionToRead));

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