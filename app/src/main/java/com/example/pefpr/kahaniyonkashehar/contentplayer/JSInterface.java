package com.example.pefpr.kahaniyonkashehar.contentplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.LevelDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.ScoreDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Score;

import java.util.ArrayList;

import static com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay.aajKeLetters;

public class JSInterface implements RecognitionListener {
    static Context mContext;
    SdCardPath sdCardPath;
    AudioPlayer recordAudio;
    static Boolean audioFlag = false;
    public static MediaPlayer mp;
    private SpeechRecognizer speech = null;
    TextToSpeechCustom tts;
    public String extractedText;
    WebViewActivity activity_instance;

    JSInterface(Context mContext, WebView w, TextToSpeechCustom tts, WebViewActivity activity_instance) {
        this.mContext = mContext;
        this.activity_instance = activity_instance;
        sdCardPath = new SdCardPath(mContext);
        mp = new MediaPlayer();
        this.tts = tts;
    }

    @JavascriptInterface
    public String getMediaPath(String gameFolder) {
        String path;
        path = sdCardPath.getSdCardPath() + "Games/" + gameFolder + "/";
        return path;
    }

    @JavascriptInterface
    public void startRecording(String recName) {
        try {
            recordAudio = new AudioPlayer(recName);
            recordAudio.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void stopRecording() {
        audioFlag = false;
        try {
            recordAudio.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void StudentLevelDetection(int totalScoredMarks, int totalOutOfMarks) {
        try {
            float perc = 0f, max = 2.4f, min = 1.1f;
            float childLevel = 0f, gameLevel = 0f;
            LevelDBHelper levelDBHelper = new LevelDBHelper(mContext);
            childLevel = Float.valueOf(levelDBHelper.GetStudentLevelByStdID(WebViewActivity.studentId));

            gameLevel = Float.valueOf(WebViewActivity.gameLevel);

            if (totalOutOfMarks != 0)
                perc = (((float) totalScoredMarks / totalOutOfMarks) * 100);
            if ((perc < 40.0f)) {
                if (childLevel > gameLevel) {
                    //Reduce Child level
                    if (childLevel > min) {
                        if ((perc < 20.0f) && (childLevel > 2.0f)) {
                            childLevel = Float.valueOf(String.valueOf(childLevel).split("\\.")[0]) - 0.6f;
                        } else if (perc < 20.0f) {
                            childLevel -= 0.1f;
                        } else if (perc > 20.0f && (childLevel > 2.0f)) {
                            if (String.valueOf(childLevel).split("\\.")[1].equalsIgnoreCase("1")) {
                                childLevel = Float.valueOf(String.valueOf(childLevel).split("\\.")[0]) - 0.6f;
                            } else {
                                childLevel -= 0.1f;
                            }
                        }
                        levelDBHelper.updateStudentLevel(WebViewActivity.studentId, childLevel,""+KksApplication.getCurrentDateTime());
                    }
                }
            } else if (perc > 60.0f) {
                if (childLevel < gameLevel) {
                    //Increase Child level
                    if (childLevel < max) {
                        if ((perc > 90.0f) && (childLevel < (max - 0.4f))) {
                            childLevel = Float.valueOf(String.valueOf(childLevel).split("\\.")[0]) + 1.1f;
                        } else if (perc < 90.0f) {
                            if (String.valueOf(childLevel).split("\\.")[1].equalsIgnoreCase("4")) {
                                childLevel += 0.7f;
                            } else {
                                childLevel += 0.1f;
                            }
                        } else {
                            childLevel += 0.1f;
                        }
                        levelDBHelper.updateStudentLevel(WebViewActivity.studentId, childLevel,""+KksApplication.getCurrentDateTime());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@JavascriptInterface
    public void audioPlayer(String filename) {
        try {
            String path;
            if (filename.charAt(0) == '/') {
                path = filename;//check for recording game and then change
            } else {
                //path="/storage/sdcard1/.prathamMarathi/"+filename;
                path = sdCardPath.getSdCardPath()+ gameFolder + "/";
            }
            mp = new MediaPlayer();

            try {
                mp.setDataSource(path);
                mp.prepare();
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                    }
                });
            } catch (Exception e) {
*//*
                log.error("Exception occurred at : " + e.getMessage());
                showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");
                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                syncActivityLogs.addToDB("audioPlayer-JSInterface", e, "Error");
                BackupDatabase.backup(mContext);
*//*
                e.printStackTrace();
            }
        } catch (Exception e) {
*//*
            log.error("Exception occurred at : " + e.getMessage());
*//*
        }
    }
*/

    @JavascriptInterface
    public void audioPlayerForStory(String filename, String storyName) {
        try {
            mp.stop();
            mp.reset();
            if (tts.textToSpeech.isSpeaking()) {
                tts.stopSpeakerDuringJS();
            }
            String path = "";
            audioFlag = true;

            try {
                path= Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/Recordings/"+ filename;
                mp.setDataSource(path);

                if (mp.isPlaying())
                    mp.stop();

                mp.prepare();
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp.stop();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void stopAudioPlayer() {
        try {
            if (mp != null) {
                mp.stop();
                mp.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void addScore(String resId, int questionId, int scorefromGame, int totalMarks, int level, String startTime) {
        boolean _wasSuccessful = false;
        resId = "";
        String[] splited;
        String[] splitedDate;
        String[] splitedTime;
        String customDate;
        String customTime;
        //put try catch block for error handling
        try {
            StatusDBHelper statusDBHelper = new StatusDBHelper(mContext);
            ScoreDBHelper scoreDBHelper = new ScoreDBHelper(mContext);

            Score score = new Score();

            score.setSessionID(statusDBHelper.getValue("CurrentSession"));
            score.setResourceID(WebViewActivity.webResId);
            score.setQuestionId(questionId);
            score.setScoredMarks(scorefromGame);
            score.setTotalMarks(totalMarks);
            score.setStudentID(WebViewActivity.studentId);

            splited = startTime.split("\\s+");
            splitedDate = splited[0].split("\\-+");
            splitedTime = splited[1].split("\\:+");
            customDate = formatCustomDate(splitedDate, "-");
            customTime = formatCustomDate(splitedTime, ":");
            score.setStartDateTime(customDate + " " + customTime);

            String deviceId = statusDBHelper.getValue("DeviceID");
            score.setDeviceID(deviceId.equals(null) ? "0000" : deviceId);
            score.setEndDateTime(KksApplication.getCurrentDateTime());
            score.setLevel(level);
            _wasSuccessful = scoreDBHelper.Add(score);

            BackupDatabase.backup(mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String formatCustomDate(String[] splitedDate, String delimiter) {
        for (int k = 0; k < splitedDate.length; k++) {
            if (Integer.parseInt(splitedDate[k]) < 10) {
                splitedDate[k] = "0" + splitedDate[k];
            }
        }
        return TextUtils.join(delimiter, splitedDate);
    }


    @JavascriptInterface
    public void playTts(String theWordWasAndYouSaid, String ttsLanguage) {
        mp.stop();
        mp.reset();
        if (tts.textToSpeech.isSpeaking()) {
            tts.stopSpeakerDuringJS();
        }
        if (ttsLanguage == null) {
            tts.ttsFunction(theWordWasAndYouSaid, "eng", 0.4f);
        }
        if (ttsLanguage.equals("eng") || ttsLanguage.equals("hin")) {
            tts.ttsFunction(theWordWasAndYouSaid, ttsLanguage, 0.4f);
        }
    }

    @JavascriptInterface
    public void stopTts() {
        tts.stopSpeakerDuringJS();
    }


    /**
     * To get duration of media file
     */
    public long getMediaDuration(String mediaPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mediaPath);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong(time);
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return timeInmillisec;
    }


    @JavascriptInterface
    public String getTodaysLetters() {
        return aajKeLetters;
    }

    /**
     * STT Functions
     */

    @JavascriptInterface
    public String getSttResult() {
        return extractedText;
    }

    @JavascriptInterface
    public void startStt(final String selectedLanguage) {
        activity_instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (selectedLanguage.equals("eng"))
                    startSttIntent("en-IN");
                else
                    startSttIntent("hi-IN");
            }
        });
    }

    @JavascriptInterface
    public void stopStt() {
        activity_instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    speech.stopListening();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startSttIntent(String selectedLanguage) {
        try {
            speech = SpeechRecognizer.createSpeechRecognizer(mContext);
            speech.setRecognitionListener(JSInterface.this);
            extractedText = "$$$";
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLanguage);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speech.startListening(intent);
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
        extractedText = "###";
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        extractedText = matches.get(0);
        Log.d("Myresult:::", "" + extractedText);
//        Toast.makeText(mContext, ""+matches.get(0), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

}