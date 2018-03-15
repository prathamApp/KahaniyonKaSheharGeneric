package com.example.pefpr.kahaniyonkashehar.activities;

import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.an.customfontview.CustomButton;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.AttendenceAdapter;
import com.example.pefpr.kahaniyonkashehar.adapters.ViewPagerAdapter;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.database.DataBaseHelper;
import com.example.pefpr.kahaniyonkashehar.fragments.AttendanceFragment;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentAddStudent;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentLogin;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentModules;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentQRScan;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentSplash;
import com.example.pefpr.kahaniyonkashehar.interfaces.ViewPagerFragmentReloaded;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StatusDBHelper;
import com.example.pefpr.kahaniyonkashehar.modalDBHelpers.StudentDBHelper;
import com.example.pefpr.kahaniyonkashehar.util.BackgroundManagement;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //This is our viewPager
    @BindView(R.id.viewpager)
    public ViewPager viewPager; 
    //This is our tablayout
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    //Fragments
    FragmentModules fragmentModules;
//    FragmentAddStudent registrationFragment;
//    AttendanceFragment attendanceFragment;
    FragmentQRScan fragmentQRScan;
    CustomButton customButton;
    ViewPagerAdapter adapter;
    BackgroundManagement backgroundManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        BackupDatabase.backup(this);
        //Initializing viewPager
        setupViewPager(viewPager);
        //Initializing the tablayout
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();

        backgroundManagement = new BackgroundManagement(this);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, true);
                ViewPagerFragmentReloaded fragment;
                if(position==1){
                    fragment = (ViewPagerFragmentReloaded) adapter.instantiateItem(viewPager, position);
                }/*else if(position==2){
                    fragment = (ViewPagerFragmentReloaded) adapter.instantiateItem(viewPager, position);
                }*/else{
                    fragment = (ViewPagerFragmentReloaded) adapter.instantiateItem(viewPager, position);
                }
                if (fragment != null) {
                    fragment.onViewPagerFragmentReloaded();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changeTabsFont() {
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/tektonpro-bold.otf");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
//        attendanceFragment = new AttendanceFragment();
//        registrationFragment = new FragmentAddStudent();
        fragmentModules = new FragmentModules();
        fragmentQRScan = new FragmentQRScan();

        adapter.addFragment(fragmentQRScan, "QRScan");
//        adapter.addFragment(attendanceFragment, "Attendance");
        adapter.addFragment(fragmentModules, "LOGIN");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        FragmentModules myFragment = (FragmentModules) getSupportFragmentManager().findFragmentByTag(FragmentModules.class.getSimpleName());
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundManagement.ActivityOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundManagement.ActivityResumed();
    }
}