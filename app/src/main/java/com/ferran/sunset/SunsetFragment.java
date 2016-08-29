package com.ferran.sunset;


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean isSunset = false;

    public static SunsetFragment newInstance() {

        Bundle args = new Bundle();

        SunsetFragment fragment = new SunsetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SunsetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sunset, container, false);
        mSceneView = v;
        mSunView = v.findViewById(R.id.sun);
        mSkyView = v.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        return v;
    }

    private void startAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        // sunset
        ObjectAnimator sunsetHeightAnimator =
                ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd).setDuration(3000);
        sunsetHeightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                        .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunsetNightSkyAnimator =
                ObjectAnimator.ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                        .setDuration(1500);
        sunsetNightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet sunsetAnimatorSet = new AnimatorSet();
        sunsetAnimatorSet.play(sunsetHeightAnimator).with(sunsetSkyAnimator).before(sunsetNightSkyAnimator);

        // sunrise
        ObjectAnimator sunriseHeightAnimator =
                ObjectAnimator.ofFloat(mSunView, "y", sunYEnd, sunYStart).setDuration(3000);
        sunriseHeightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunriseSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor",
                mSunsetSkyColor, mBlueSkyColor).setDuration(3000);
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunriseNightSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor",
                mNightSkyColor, mSunsetSkyColor).setDuration(1500);
        sunriseNightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet sunriseAnimatorSet = new AnimatorSet();
        sunriseAnimatorSet.play(sunriseNightSkyAnimator).before(sunriseSkyAnimator)
                .with(sunriseHeightAnimator);

        if (!isSunset) {
            sunsetAnimatorSet.start();
            isSunset = true;
        } else {
            sunriseAnimatorSet.start();
            isSunset = false;
        }
    }

}
