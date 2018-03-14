package com.example.pefpr.kahaniyonkashehar.animations;

/**
 * Created by pefpr on 20/11/2017.
 */

public class MyBounceInterpolator implements android.view.animation.Interpolator {
    public double mAmplitude = 1;
    public double mFrequency = 10;

    public MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
