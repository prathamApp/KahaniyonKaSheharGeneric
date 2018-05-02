package com.example.pefpr.kahaniyonkashehar.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.LevelDecider;
import com.example.pefpr.kahaniyonkashehar.animations.MyBounceInterpolator;
import com.example.pefpr.kahaniyonkashehar.contentplayer.SdCardPath;
import com.example.pefpr.kahaniyonkashehar.contentplayer.TextToSpeechCustom;
import com.example.pefpr.kahaniyonkashehar.contentplayer.WebViewActivity;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.ScoreDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pefpr.kahaniyonkashehar.activities.LevelDecider.addStoryScore;
import static com.example.pefpr.kahaniyonkashehar.activities.LevelDecider.percentageForStory;

public class StoryFragment extends Fragment {

    public static MediaPlayer mp;
    @BindView(R.id.myflowlayout)
    FlowLayout flowLayout;
    @BindView(R.id.main_container)
    LinearLayout main_container;
    @BindView(R.id.card_view)
    CardView card_view;
    @BindView(R.id.btn_nextpage)
    Button btn_nextpage;
    @BindView(R.id.btn_previouspage)
    Button btn_previouspage;
    @BindView(R.id.story_title)
    TextView story_title;
    @BindView(R.id.iv_question)
    ImageView iv_question;
    @BindView(R.id.tv_pageno)
    TextView tv_pageno;


    String language = "Hindi";
    static int currentPage;
    int totalPages = 0, correctAnswerCount, pageNo = 1, quesNo = 0, quesPgNo = 0;
    JSONArray pageArray, questions;
    TextToSpeechCustom textToSpeechCustom;
    List<String> wordsDurationList = new ArrayList<String>();
    List<String> pageEndList = new ArrayList<String>();
    List<String> pageStartList = new ArrayList<String>();
    List<String> pageImageList = new ArrayList<String>();
    int questionCounter = 0, answerCounter = 0, randomQuestionNumber;
    public static WeakHandler handler, soundStopHandler, colorChangeHandler;
    String storyData, storyAudio, storyBg, storyId, wordQuestion,resQuesId;
    boolean nextFlag = false, mediaPauseFlag = false, storyFinishFlag = false, readingFinishFlag = false;
    Float pageDuration = 0f, stopPlayBack = 0f, startPlayBack = 0f;
    Float soundFileWordEnd = 0f, soundFileWordStart = 0f;
    JSONArray questionsArray = null, questionPagesArray = null;
    int[] randomQuestionsArray, randomPageQuestionsArray;

    SdCardPath ex_path;
    String sdCardPathString, storyFragLang;

    int correctAnswers = 0;

    public StoryFragment() {
        textToSpeechCustom = new TextToSpeechCustom(getActivity(), 0.4f);
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
        storyData = getArguments().getString("storyData", "");
        storyId = getArguments().getString("storyId", "");
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        textToSpeechCustom = new TextToSpeechCustom(getActivity(), 0.4f);
        ex_path = new SdCardPath(getActivity());
        currentPage = 0;
        sdCardPathString = ex_path.getSdCardPath();

        String appLang = getLanguage();

        if (appLang.equalsIgnoreCase("Punjabi")) {
            storyFragLang = "pun";
        } else if (appLang.equalsIgnoreCase("Odiya")) {
            storyFragLang = "odiya";
        } else if (appLang.equalsIgnoreCase("Gujarati")) {
            storyFragLang = "guj";
        } else
            storyFragLang = "NA";

        storyData = getArguments().getString("storyData");
        storyId = getArguments().getString("storyId");
        iv_question.setVisibility(View.INVISIBLE);
        tv_pageno.setText("" + (currentPage + 1));

        iv_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playQuestionAudio();
            }
        });


        questions = fetchStory("Questions");
        generateRandomQuestions();

        try {
            pageArray = new JSONArray(storyData);
            language = getLanguage();
            getWordsOfStoryOfPage(language);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "totalPages: NextBtn: " + totalPages + "  currentPage: " + currentPage);

                Animation leftSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left);
                leftSwipe.setInterpolator(new AccelerateDecelerateInterpolator());

                if ((currentPage < totalPages - 1) && (!storyFinishFlag)) {

                    currentPage++;
                    pageNo++;
                    tv_pageno.setText("" + (currentPage + 1));

                    try {
                        if (mp.isPlaying())
                            mp.stop();
                        handler.removeCallbacksAndMessages(null);
                        soundStopHandler.removeCallbacksAndMessages(null);
                        card_view.startAnimation(leftSwipe);
                        handler.removeCallbacksAndMessages(null);
                        soundStopHandler.removeCallbacksAndMessages(null);
                        setTextAndPlayRecordings(currentPage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("click", "NextBtn - totalPages: " + totalPages + "  currentPage: " + currentPage);
                } else {
                    storyFinishFlag = true;
                    if (currentPage < totalPages - 1) {
                        currentPage++;
                        pageNo++;
                        tv_pageno.setText("" + (currentPage + 1));
                        card_view.startAnimation(leftSwipe);
                        setTextAndPlayRecordings(currentPage);
                    }
                }
            }
        });

        btn_previouspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "totalPages: PreviousBtn: " + totalPages + "  currentPage: " + currentPage);

                Animation rightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right);
                rightSwipe.setInterpolator(new AccelerateDecelerateInterpolator());

                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }

                    if ((currentPage > 0) && (!storyFinishFlag)) {
                        if (mp.isPlaying())
                            mp.stop();
                        currentPage--;
                        pageNo--;
                        tv_pageno.setText("" + (currentPage + 1));
                        handler.removeCallbacksAndMessages(null);
                        soundStopHandler.removeCallbacksAndMessages(null);
                        setTextAndPlayRecordings(currentPage);
                        card_view.startAnimation(rightSwipe);
                    } else {
                        if (currentPage > 0) {
                            currentPage--;
                            pageNo--;
                            tv_pageno.setText("" + (currentPage + 1));
                            card_view.startAnimation(rightSwipe);
                            setTextAndPlayRecordings(currentPage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void generateRandomQuestions() {
        String questionStoryId;
        try {
            for (int i = 0; i < questions.length(); i++) {
                questionStoryId = questions.getJSONObject(i).getString("resourceId");
                if (questionStoryId.equalsIgnoreCase(storyId)) {
                    questionPagesArray = questions.getJSONObject(i).getJSONArray("nodelist");
                    break;
                }
            }

            Log.d("QuestionsPages", "generateRandomQuestions: " + questionPagesArray.length());
            randomPageQuestionsArray = new int[questionPagesArray.length()];
            for (int i = 0; i < questionPagesArray.length(); i++)
                randomPageQuestionsArray[i] = i;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shuffleArray(int[] newQuestionsArray) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            rnd = ThreadLocalRandom.current();
        }
        for (int i = newQuestionsArray.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = newQuestionsArray[index];
            newQuestionsArray[index] = newQuestionsArray[i];
            newQuestionsArray[i] = a;
        }
        randomQuestionsArray = newQuestionsArray;

        for (int i = 0; i < randomQuestionsArray.length; i++)
            Log.d("randomArray", "shuffleArray: " + randomQuestionsArray[i]);
    }

    private void getWordsOfStoryOfPage(String language) {
        try {
            JSONObject selectedStoryObject = pageArray.getJSONObject(currentPage);
            totalPages = pageArray.length();
            Log.d("totalPages", "getWordsOfStoryOfPage: " + totalPages);
            String storyTitle = selectedStoryObject.getString("nodeTitle");
            storyAudio = selectedStoryObject.getString("resourceAudio");

            if (storyFragLang.equalsIgnoreCase("pun")) {
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raavi_punjabi.ttf");
                story_title.setTypeface(custom_font, Typeface.NORMAL);
            } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("odiya")) {
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lohit_oriya.ttf");
                story_title.setTypeface(custom_font, Typeface.NORMAL);
            } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("guj")) {
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muktavaani_gujarati.ttf");
                story_title.setTypeface(custom_font, Typeface.NORMAL);
            }

            story_title.setText("" + storyTitle);
            tv_pageno.setText("" + (currentPage + 1));
            setTextAndPlayRecordings(currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTextAndPlayRecordings(final int currentPage) {
        JSONObject page = null;
        List<String> linesStringList = new ArrayList<String>();
        wordsDurationList.clear();
        flowLayout.removeAllViews();

        try {
            page = pageArray.getJSONObject(currentPage);
            //TODO change to nodeImage
            storyBg = page.getString("nodeImage");
            File f = new File(sdCardPathString + "StoryData/" + storyBg);
            if (f.exists()) {
                Bitmap bmImg = BitmapFactory.decodeStream(new FileInputStream(sdCardPathString + "/StoryData/" + storyBg));
                BitmapDrawable background = new BitmapDrawable(bmImg);
                main_container.setBackgroundDrawable(background);
            }

            JSONArray lines = page.getJSONArray("nodelist");
            for (int j = 0; j < lines.length(); j++) {
                linesStringList.add(lines.getJSONObject(j).getString("resourceDesc"));
                wordsDurationList.add(lines.getJSONObject(j).getString("resourceDuration"));
                stopPlayBack = Float.valueOf(lines.getJSONObject(j).getString("resourceTo"));
            }
            startPlayBack = Float.valueOf(lines.getJSONObject(0).getString("resourceFrom"));
            pageStartList.add(String.valueOf(startPlayBack));
            pageEndList.add(String.valueOf(stopPlayBack));
            Log.d("duration", "startPlayBack: " + startPlayBack + "  stopPlayBack: " + stopPlayBack);

            for (int i = 0; i < linesStringList.size(); i++) {
                final TextView myTextView = new TextView(getActivity());

                if (storyFragLang.equalsIgnoreCase("pun")) {
                    Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/raavi_punjabi.ttf");
                    myTextView.setTypeface(custom_font, Typeface.BOLD);
                } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("odiya")) {
                    Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lohit_oriya.ttf");
                    myTextView.setTypeface(custom_font, Typeface.BOLD);
                } else if (GamesDisplay.gameDispLang.equalsIgnoreCase("guj")) {
                    Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muktavaani_gujarati.ttf");
                    myTextView.setTypeface(custom_font, Typeface.BOLD);
                }

                myTextView.setText(linesStringList.get(i));
                myTextView.setId(i);
                myTextView.setTextSize(45);
                myTextView.setTextColor(Color.BLACK);
                myTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (readingFinishFlag) {
                            String clickedText = (String) myTextView.getText();
                            Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                            pop.setInterpolator(interpolator);
                            myTextView.startAnimation(pop);

                            checkAnswer(clickedText, myTextView);
                            if (readingFinishFlag && questionCounter < 9 && !nextFlag) {
                                handler = new WeakHandler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getQuestion(quesNo);
                                    }
                                }, 2500);
                            } else if (nextFlag || questionCounter == 9) {
                                if (nextFlag)
                                    percentageForStory = 100f;
                                else {
                                    percentageForStory = (((float) correctAnswers / 9) * 100);
                                }
//                                Toast.makeText(getActivity(), "Level Percentage" + percentageForStory, Toast.LENGTH_SHORT).show();
                                handler = new WeakHandler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (mp.isPlaying())
                                                mp.stop();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        handler.removeCallbacksAndMessages(this);
                                        soundStopHandler.removeCallbacksAndMessages(this);

                                        if (language.equalsIgnoreCase("hindi")) {
                                            PD_Utility.showFragment(getActivity(), new ReadWordFragment(), R.id.ll_leveldecider,
                                                    null, ReadWordFragment.class.getSimpleName());
                                        } else {
                                            PD_Utility.showFragment(getActivity(), new ImageIdentificationFragment(), R.id.ll_leveldecider,
                                                    null, ImageIdentificationFragment.class.getSimpleName());
                                        }
                                    }
                                }, 3000);
                            }
                        }
                    }
                });
                flowLayout.addView(myTextView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!storyFinishFlag) {

            handler = new WeakHandler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        mp = new MediaPlayer();
                        mp.setDataSource(sdCardPathString + "StoryData/" + storyAudio);
                        mp.prepare();
                        mp.seekTo((int) (startPlayBack * 1000));
                        mp.start();
                        soundStopHandler = new WeakHandler();
                        soundStopHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mp.stop();
                            }
                        }, (long) (stopPlayBack * 1000));
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startStoryReading(0);
                }
            }, 2000);
        }
    }

    public void checkAnswer(final String clickedText, final TextView clickedTextView) {
        Log.d("wordddd", "checkAnswer:" + wordQuestion + "----- clickedText:" + clickedText);
        questionCounter++;
        int scorefromQuestion;
        if (wordQuestion.equalsIgnoreCase(clickedText)) {
            playMusic("StoriesAudio/correct.mp3");
            scorefromQuestion = 10;
            clickedTextView.setTextColor(Color.GREEN);
            correctAnswers++;
            correctAnswerCount++;
            if (correctAnswerCount >= 3) {
                nextFlag = true;
            }
        } else {
            playMusic("StoriesAudio/wrong.mp3");
            scorefromQuestion = 0;
            clickedTextView.setTextColor(Color.RED);
            colorChangeHandler = new WeakHandler();
            colorChangeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mp.isPlaying())
                        mp.stop();
                    textToSpeechCustom.ttsFunction("" + clickedText, "hin", 0.6f);
                    clickedTextView.setTextColor(Color.BLACK);
                }
            }, 500);
            correctAnswerCount = 0;
        }
        addStoryScore(Integer.parseInt(resQuesId), scorefromQuestion);
        colorChangeHandler = new WeakHandler();
        colorChangeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickedTextView.setTextColor(Color.BLACK);
            }
        }, 1000);
    }

    private String getLanguage() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());
        language = statusDBHelper.getValue("AppLang");
        return language;
    }

    public JSONArray fetchStory(String jasonName) {
        JSONArray returnStoryNavigate = null;
        try {
            InputStream is = new FileInputStream(sdCardPathString + "JsonFiles/" + jasonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObj = new JSONObject(new String(buffer));
            returnStoryNavigate = jsonObj.getJSONArray("nodelist");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStoryNavigate;
    }

    @SuppressLint("ResourceAsColor")
    public void startStoryReading(final int index) {
//        textToSpeechCustom.ttsFunction("चलो शब्द पहचानो", language);
        float wordDuration = 1;
        handler = new WeakHandler();
        colorChangeHandler = new WeakHandler();

        if (index < wordsDurationList.size()) {
            wordDuration = Float.parseFloat(wordsDurationList.get(index));
            final TextView myView = (TextView) flowLayout.getChildAt(index);
            myView.setBackgroundColor(Color.YELLOW);
            myView.setTextColor(Color.RED);
            colorChangeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myView.setBackgroundColor(Color.TRANSPARENT);
                    myView.setTextColor(Color.BLACK);
                }
            }, 500);
            if (index == wordsDurationList.size() - 1) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp.stop();
                    }
                }, 500);
                mediaPauseFlag = true;
            } else if (index == wordsDurationList.size() - 2) {
                //todo Scroll
            }
        } else
            wordDuration = 1;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < flowLayout.getChildCount()) {
//                    int a = index + 1;
//                    Log.d("ddddd", "run: " + a);
                    startStoryReading(index + 1);
                } else {
                    mp.stop();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < wordsDurationList.size(); i++) {
                                TextView myView = (TextView) flowLayout.getChildAt(i);
                                myView.setBackgroundColor(Color.TRANSPARENT);
                                myView.setTextColor(Color.BLACK);
                            }
                            btn_nextpage.performClick();
                            if (storyFinishFlag) {
                                readingFinishFlag = true;
                                iv_question.setVisibility(View.VISIBLE);
                                StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());
                                SessionDBHelper sessionDBHelper = new SessionDBHelper(getActivity());
                                String curStrSession = statusDBHelper.getValue("CurrentStorySession");
                                sessionDBHelper.UpdateToDate(""+curStrSession, KksApplication.getCurrentDateTime());
                                BackupDatabase.backup(getActivity());
                                getQuestion(quesNo);
                            }
                        }
                    }, (long) (1500));
                }
            }
        }, (long) ((wordDuration * 1000) - 10));

    }

    public void getQuestion(int quesno) {
        try {
            int[] quesarr;
            shuffleArray(randomPageQuestionsArray);
            quesPgNo = randomPageQuestionsArray[0];
            Log.d("quesPgNo", "getQuestion: " + quesPgNo);
            questionsArray = questionPagesArray.getJSONObject(randomPageQuestionsArray[0]).getJSONArray("nodelist");

            Random randomNum = new Random();
            Log.d("quesLen", "QuestionsLength: " + questionsArray.length());
            quesarr = new int[questionsArray.length()];
            for (int i = 0; i < questionsArray.length(); i++)
                quesarr[i] = i;
            shuffleArray(quesarr);

//            int currPg = Integer.parseInt(""+tv_pageno.getText());
            setTextAndPlayRecordings(quesPgNo);
            currentPage = quesPgNo;
            tv_pageno.setText("" + (quesPgNo + 1));
            wordQuestion = questionsArray.getJSONObject(quesarr[0]).getString("resourceDesc");
            soundFileWordStart = Float.valueOf(questionsArray.getJSONObject(quesarr[0]).getString("resourceFrom"));
            soundFileWordEnd = Float.valueOf(questionsArray.getJSONObject(quesarr[0]).getString("resourceDuration"));
            resQuesId = questionsArray.getJSONObject(quesarr[0]).getString("resourceId");

            Log.d("wordddd", "getQuestion : " + wordQuestion );
            Log.d("wordddd", "resQuesIdc : " + resQuesId );
            quesNo++;
            LevelDecider.questionStartDate  = ""+KksApplication.getCurrentDateTime();
            playQuestionAudio();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playQuestionAudio() {
        try {
            if (language.equalsIgnoreCase("hindi")) {
                if (quesNo == 1) {
                    textToSpeechCustom.ttsFunction("कहानी में इन् शब्दों को ढूंढो!", "hin", 0.6f);
                    Thread.sleep(4000);
                    textToSpeechCustom.ttsFunction(wordQuestion, "hin", 0.7f);
                } else {
                    textToSpeechCustom.ttsFunction(wordQuestion, "hin", 0.6f);
                }
            } else {
                if (mp != null || mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
                if (quesNo == 1) {
                    mp = new MediaPlayer();
                    mp.setDataSource(sdCardPathString + "StoryData/StoriesAudio/wordsfromstories.mp3");
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            try {
                                Thread.sleep((long) (800));
                                Log.d("AudioQues", "Start : " + soundFileWordStart +
                                        "   End: " + soundFileWordEnd);
                                mp = new MediaPlayer();
                                mp.setDataSource(sdCardPathString + "StoryData/" + storyAudio);
                                mp.prepare();
                                mp.seekTo((int) (soundFileWordStart * 1000));
                                mp.start();
                                Thread.sleep((long) (soundFileWordEnd * 1000));
                                mp.stop();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Log.d("AudioQues", "Start : " + soundFileWordStart +
                            "   End: " + soundFileWordEnd);
                    mp = new MediaPlayer();
                    mp.setDataSource(sdCardPathString + "StoryData/" + storyAudio);
                    mp.prepare();
                    mp.seekTo((int) (soundFileWordStart * 1000));
                    mp.start();
                    Thread.sleep((long) (soundFileWordEnd * 1000));
                    mp.stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void playMusic(String fileName) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(sdCardPathString + "StoryData/" + fileName);
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
}
