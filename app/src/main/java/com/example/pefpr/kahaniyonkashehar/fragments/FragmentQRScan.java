package com.example.pefpr.kahaniyonkashehar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.pefpr.kahaniyonkashehar.KksApplication;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.MainActivity;
import com.example.pefpr.kahaniyonkashehar.activities.StoriesDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.StoryOrGame;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.interfaces.ViewPagerFragmentReloaded;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.AttendanceDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.SessionDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalclasses.Student;
import com.google.zxing.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Pravin on 31/01/2018.
 */

public class FragmentQRScan extends Fragment implements ZXingScannerView.ResultHandler,
        ViewPagerFragmentReloaded {

    @BindView(R.id.launch_QR)
    Button launch_QR;
    @BindView(R.id.content_frame)
    ViewGroup content_frame;

    public ZXingScannerView mScannerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initCamera();
    }

    public void initCamera() {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setResultHandler(this);
        content_frame.addView((mScannerView));
    }

    @Override
    public void onViewPagerFragmentReloaded() {
        //todo reinitiate scan
        Log.d("fragment::", "reload");
        slide(launch_QR, 400, 1000);
    }

    public void slide(View view, int fromXDelta, int animateDuration) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                fromXDelta,                 // fromXDelta
                0,                 // toXDelta
                0,               // fromYDelta
                0);                // toYDelta
        animate.setDuration(animateDuration);
        animate.setInterpolator(new DecelerateInterpolator());
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onDestroyView() {
        if(mScannerView!=null)
            mScannerView.stopCamera();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if(mScannerView!=null)
            mScannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {

        mScannerView.stopCamera();
        Log.d("RawResult:::", "****" + rawResult.getText());

//        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+-[A-Za-z._]{2,50}");

//        Matcher mat = pattern2.matcher("04@-09-vale ketan 009");
        Matcher mat = pattern.matcher(rawResult.getText());

        //"vivek.mitra@gmail.com"
        if (mat.matches()) {
            //Valid pattern
            String[] id = decodeStudentId(rawResult.getText(), "-");

            String stdId = id[0];
            //String stdFirstName = id[1];

            String[] name = decodeStudentId(id[1], "_");
            String stdFirstName = name[0];
            String stdLastName = "";
            if (name.length > 1)
                stdLastName = name[1];

            try {
                StudentDBHelper studentDBHelper = new StudentDBHelper(getActivity());
                SessionDBHelper sessionDBHelper = new SessionDBHelper(getActivity());
                AttendanceDBHelper attendanceDBHelper = new AttendanceDBHelper(getActivity());
                StatusDBHelper statusDBHelper = new StatusDBHelper(getActivity());

                String sessionID = KksApplication.getUniqueID().toString();
                Boolean checkSessionEntry = sessionDBHelper.addToSessionTable(sessionID, KksApplication.getCurrentDateTime(), "NA");


                if (studentDBHelper.GetStudentDataByStdID(stdId) == null) {
                    // insert in student table id and name
                    Student student = new Student();
                    student.setStudentID(stdId);
                    student.setStudentUID(stdId);
                    student.setFirstName(""+stdFirstName);
                    student.setMiddleName("NA");
                    student.setRegDate("NA");
                    student.setLastName(""+stdLastName);
                    student.setGender("NA");
                    student.setAge(0);
                    student.setVillageName("NA");
                    student.setNewFlag(1);
                    student.setDeviceId(statusDBHelper.getValue("DeviceID"));

                    studentDBHelper.insertData(student);

                    attendanceDBHelper.add(sessionID, stdId);
                    statusDBHelper.Update("CurrentSession", "" + sessionID);

                    BackupDatabase.backup(getActivity());
                    Intent storiesDisplay = new Intent(getActivity(), StoriesDisplay.class);
                    storiesDisplay.putExtra("StudentID", stdId);
                    startActivity(storiesDisplay);
                    getActivity().finish();

                } else {
                    attendanceDBHelper.add(sessionID, stdId);
                    statusDBHelper.Update("CurrentSession", "" + sessionID);

                    BackupDatabase.backup(getActivity());

                    Intent intentStoryOrGame = new Intent(getActivity(), StoryOrGame.class);
                    intentStoryOrGame.putExtra("StudentID",stdId);
                    intentStoryOrGame.putExtra("StudentName", stdFirstName);
                    startActivity(intentStoryOrGame);
                    getActivity().finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Invalid Pattern
            Toast.makeText(getActivity(), "Invalid QR try logging in or registering", Toast.LENGTH_SHORT).show();
            WeakHandler handler = new WeakHandler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).viewPager.setCurrentItem(1, true);
                }
            }, 1500);
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
         /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SplashActivity.this);
            }
        }, 2000);*/
    }

    @OnClick(R.id.launch_QR)
    public void launchQR() {
        //initiating the qr code scan
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
//        mScannerView.setAspectTolerance(0.2f);
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(FragmentQRScan.this);
        mScannerView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.setVisibility(View.VISIBLE);
                slide(content_frame, -700, 1500);
            }
        }, 500);
    }

    private String[] decodeStudentId(String text, String s) {
        return text.split(s);
    }
}
