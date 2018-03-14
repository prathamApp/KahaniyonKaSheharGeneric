package com.example.pefpr.kahaniyonkashehar.contentplayer;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.GamesDisplay;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.tv_web_view_gname)
    TextView tv_GameName;
    @BindView(R.id.tv_web_view_sname)
    TextView tv_StudentName;
    @BindView(R.id.tv_gnamelbl)
    TextView tv_gnamelbl;

    WebView webView;
    String gamePath, currentGameName, webViewLang = "NA";
    static String webResId, studentId, gameLevel;
    StatusDBHelper statusDBHelper;
    AttendanceDBHelper attendanceDBHelper;
    StudentDBHelper studentDBHelper;
    TextToSpeechCustom tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        webView = (WebView) findViewById(R.id.loadPage);

        gamePath = getIntent().getStringExtra("path");
        webResId = getIntent().getStringExtra("resId");
        currentGameName = getIntent().getStringExtra("currentGameName");
        gameLevel = getIntent().getStringExtra("gameLevel");

        statusDBHelper = new StatusDBHelper(this);
        attendanceDBHelper = new AttendanceDBHelper(this);
        studentDBHelper = new StudentDBHelper(this);

        tts = new TextToSpeechCustom(this, 0.4f);


        String curSession = statusDBHelper.getValue("CurrentSession");
        studentId = attendanceDBHelper.getCurrenStudentId(curSession);
        String studentName = studentDBHelper.getStudentName(studentId);

        String appLang = getLanguage();

        if (appLang.equalsIgnoreCase("Punjabi")) {
            webViewLang = "pun";
        } else if (appLang.equalsIgnoreCase("Odiya")) {
            webViewLang = "odiya";
        } else if (appLang.equalsIgnoreCase("Gujarati")) {
            webViewLang = "guj";
        } else
            webViewLang = "NA";

        if (webViewLang.equalsIgnoreCase("pun")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/raavi_punjabi.ttf");
            tv_GameName.setTypeface(custom_font, Typeface.NORMAL);
        } else if (webViewLang.equalsIgnoreCase("odiya")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/lohit_oriya.ttf");
            tv_GameName.setTypeface(custom_font, Typeface.NORMAL);
        } else if (webViewLang.equalsIgnoreCase("guj")) {
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/muktavaani_gujarati.ttf");
            tv_GameName.setTypeface(custom_font, Typeface.NORMAL);
        }

        tv_GameName.setText(currentGameName);
        tv_StudentName.setText("Student Name: "+studentName);
        tv_GameName.setTextColor(Color.WHITE);
        tv_gnamelbl.setTextColor(Color.WHITE);
        tv_StudentName.setTextColor(Color.WHITE);
        createWebView(Uri.parse(gamePath));
    }

    private String getLanguage() {
        StatusDBHelper statusDBHelper = new StatusDBHelper(this);
        String language = statusDBHelper.getValue("AppLang");
        return language;
    }

    @SuppressLint("JavascriptInterface")
    public void createWebView(Uri GamePath) {

        String myPath = GamePath.toString();
        webView.loadUrl(myPath);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new JSInterface(this, webView,tts,this), "Android");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.clearCache(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.loadUrl("about:blank");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

