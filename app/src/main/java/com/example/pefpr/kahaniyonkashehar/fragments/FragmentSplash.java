package com.example.pefpr.kahaniyonkashehar.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pefpr.kahaniyonkashehar.R;
import com.example.pefpr.kahaniyonkashehar.activities.AttendenceDisplay;
import com.example.pefpr.kahaniyonkashehar.activities.ContentDisplay;
import com.example.pefpr.kahaniyonkashehar.animations.MyBounceInterpolator;
import com.example.pefpr.kahaniyonkashehar.util.PD_Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pefpr on 23/11/2017.
 */

public class FragmentSplash extends android.support.v4.app.Fragment {

    @BindView(R.id.splashScreen)
    ImageView splashScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        animateView(splashScreen);
    }

    private void animateView(final View view) {
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.5, 15);
        animation.setInterpolator(interpolator);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1000);
                anim.setRepeatCount(1);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        TranslateAnimation anim = new TranslateAnimation(0, 700, 0, 700);
                        anim.setDuration(500);

                        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                        /*PD_Utility.showFragment(getActivity(),new FragmentLogin(),R.id.main_container,
                                                null,FragmentLogin.class.getSimpleName());*/
                                Intent groupSelect = new Intent(getActivity(), AttendenceDisplay.class);
                                startActivityForResult(groupSelect, 46);
                            }
                        });

                        view.startAnimation(anim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 46) {
           /* PD_Utility.showFragment(getActivity(),new FragmentLogin(),R.id.main_container,
                    null,FragmentLogin.class.getSimpleName());*/
        }
    }
}
