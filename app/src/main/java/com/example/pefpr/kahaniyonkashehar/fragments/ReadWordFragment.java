package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.LevelDecider;
import com.example.pefpr.kahaniyonkashehar.activities.StoriesDisplay;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadWordFragment extends Fragment implements
        RecognitionListener {

    @BindView(R.id.btn_read_word_mic)
    Button btnMic;
    @BindView(R.id.btn_read_word_next)
    Button btnNext;
    @BindView(R.id.btn_read_word_stop)
    Button btnStop;

    @BindView(R.id.tv_read_word_question)
    TextView tvQuesion;
    @BindView(R.id.tv_read_word_answer)
    TextView tvAnswer;


    MediaRecorder mediaRecorder;
    String selectedLanguage = "hi-IN";
    private SpeechRecognizer speech = null;
    public static Intent intent;
    int[] integerArray = new int[9];
    int quesCounter = 0, correctCount = 0, consecutiveCount = 0;

    JSONArray allSories;
    JSONArray questions;
    String wordQuestion;
    Float soundFileWordEnd = 0f, soundFileWordStart = 0f;

    SdCardPath ex_path;
    String sdCardPathString;


    public ReadWordFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_read_word, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ex_path = new SdCardPath(getActivity());
        sdCardPathString = ex_path.getSdCardPath();

        tvQuesion.setText("");
        tvAnswer.setText("");
        allSories = fetchStory("Words");

        String nList;

        try {
            for (int i = 0; i <= allSories.length(); i++) {
                String storyId = allSories.getJSONObject(i).getString("nodeId");
                Log.d("NodeID", "LevelDecider: "+LevelDecider.storyId+", Story Id : "+storyId);
                if(storyId.equalsIgnoreCase(LevelDecider.storyId)) {
                    questions = allSories.getJSONObject(i).getJSONArray("nodelist");
                    break;
                }
            }
        }catch (Exception e){e.printStackTrace();}

        Log.d("temp", "onViewCreated: "+questions);
        integerArray = getUniqueRandomNumber(0, questions.length(), 9);

        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        getQuestion(quesCounter);

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswer.setText("");
                speech.startListening(intent);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAnswer.setText("");
                speech.stopListening();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesCounter++;
                if (checkOnNext(tvQuesion.getText().toString(), tvAnswer.getText().toString())) {
                    correctCount++;
                    consecutiveCount++;
                } else {
                    consecutiveCount = 0;
                }
                tvQuesion.setText("");
                tvAnswer.setText("");
                tvAnswer.setBackground(getResources().getDrawable(R.drawable.edittext_corner));

                if (quesCounter == integerArray.length || consecutiveCount == 3) {
                    if (consecutiveCount == 3)
                        LevelDecider.percentageForWordRead = 100f;
                    else
                        LevelDecider.percentageForWordRead = (((float) correctCount / 9) * 100);

//                    Toast.makeText(getActivity(), "Level percentage:"+LevelDecider.percentageForWordRead, Toast.LENGTH_SHORT).show();
                    PD_Utility.showFragment(getActivity(), new ImageIdentificationFragment(), R.id.ll_leveldecider,
                            null, ImageIdentificationFragment.class.getSimpleName());
                }else {
                    getQuestion(quesCounter);
                }
            }
        });
    }

    private static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    public JSONArray fetchStory(String jasonName) {
        JSONArray returnStoryNavigate = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString+"JsonFiles/"+jasonName + ".json");
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


    public void getQuestion(int currQues) {
        String questionStoryId, questionsList;

        try {
            wordQuestion = questions.getJSONObject(integerArray[currQues]).getString("resourceDesc");
            soundFileWordStart = Float.valueOf(questions.getJSONObject(integerArray[currQues]).getString("resourceFrom"));
            soundFileWordEnd = Float.valueOf(questions.getJSONObject(integerArray[currQues]).getString("resourceDuration"));

            tvQuesion.setText(wordQuestion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

        System.out.println(" onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String tex = "";
/*
        for (int i =0; i<matches.size(); i++){
            if(matches.get(i)) {
                tex = matches.get(i) + " ";
                break;
            }
            else
*/
        tex = matches.get(0);

//        }

        checkAnswer(tex);

    }

    private void checkAnswer(String tex) {
        tvAnswer.setText(tex);
        if (tex.equalsIgnoreCase(wordQuestion))
            tvAnswer.setBackground(getResources().getDrawable(R.drawable.edittext_corner_correct));
        else
            tvAnswer.setBackground(getResources().getDrawable(R.drawable.edittext_corner_wrong));

    }

    private boolean checkOnNext(String que, String ans) {
        return que.equalsIgnoreCase(ans);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

}
