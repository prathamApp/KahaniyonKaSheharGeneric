package com.example.pefpr.kahaniyonkashehar.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.an.customfontview.CustomButton;
import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.adapters.ViewPagerAdapter;
import com.example.pefpr.kahaniyonkashehar.database.BackupDatabase;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentModules;
import com.example.pefpr.kahaniyonkashehar.fragments.FragmentQRScan;
import com.example.pefpr.kahaniyonkashehar.interfaces.ViewPagerFragmentReloaded;
import com.example.pefpr.kahaniyonkashehar.util.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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
    protected void onPause() {
        Log.d("MAIN:::::", "onResume: IN MAIN ACTIVITY");
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        FragmentModules myFragment = (FragmentModules) getSupportFragmentManager().findFragmentByTag(FragmentModules.class.getSimpleName());
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            getSupportFragmentManager().popBackStack();
        } else {
            finishAffinity();
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}