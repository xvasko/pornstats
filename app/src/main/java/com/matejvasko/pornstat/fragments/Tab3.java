package com.matejvasko.pornstat.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.matejvasko.pornstat.R;

import static android.content.Context.MODE_PRIVATE;

public class Tab3 extends Fragment {

    SharedPreferences pref;
    int maxHotStreak;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pref = getContext().getSharedPreferences("days", MODE_PRIVATE);
        maxHotStreak = pref.getInt("maxHotStreak", 0);

        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        showBadges(rootView, pref);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getContext().getSharedPreferences("days", MODE_PRIVATE);
        if (pref.getBoolean("secretBadge2", false)) {
            ((ImageView) getActivity().findViewById(R.id.imageViewSecret2)).setImageResource(R.drawable.s2);
            ((TextView) getActivity().findViewById(R.id.textViewSecret2)).setText("Change notification time!");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                if (pref.getBoolean("secretBadge2", false)) {
                    ((ImageView) getActivity().findViewById(R.id.imageViewSecret2)).setImageResource(R.drawable.s2);
                    ((TextView) getActivity().findViewById(R.id.textViewSecret2)).setText("Change notification time!");
                }
            } catch (NullPointerException e) {
                System.out.println("NullPointer - pref not initialized yet");
            }
        }
    }

    private void showBadges(View rootView,  SharedPreferences pref) {
        if (maxHotStreak >= 1) {
            ((ImageView)rootView.findViewById(R.id.imageView1)).setImageResource(R.drawable.badge1);
        }
        if (maxHotStreak >= 3) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge1)).setImageResource(R.drawable.m1);
        }
        if (maxHotStreak >= 5) {
            ((ImageView)rootView.findViewById(R.id.imageView2)).setImageResource(R.drawable.badge2);
        }
        if (maxHotStreak >= 8) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge2)).setImageResource(R.drawable.m2);
        }
        if (maxHotStreak >= 10) {
            ((ImageView)rootView.findViewById(R.id.imageView3)).setImageResource(R.drawable.badge3);
        }
        if (maxHotStreak >= 15) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge3)).setImageResource(R.drawable.m3);
        }
        if (maxHotStreak >= 20) {
            ((ImageView)rootView.findViewById(R.id.imageView4)).setImageResource(R.drawable.badge4);
        }
        if (maxHotStreak >= 25) {
            ((ImageView)rootView.findViewById(R.id.imageView5)).setImageResource(R.drawable.badge5);
        }
        if (maxHotStreak >= 29) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge4)).setImageResource(R.drawable.m4);
        }
        if (maxHotStreak >= 35) {
            ((ImageView)rootView.findViewById(R.id.imageView6)).setImageResource(R.drawable.badge6);
        }
        if (maxHotStreak >= 40) {
            ((ImageView)rootView.findViewById(R.id.imageView7)).setImageResource(R.drawable.badge7);
        }
        if (maxHotStreak >= 45) {
            ((ImageView)rootView.findViewById(R.id.imageView8)).setImageResource(R.drawable.badge8);
        }
        if (maxHotStreak >= 50) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge5)).setImageResource(R.drawable.m5);
        }
        if (maxHotStreak >= 78) {
            ((ImageView)rootView.findViewById(R.id.imageViewChallenge6)).setImageResource(R.drawable.m6);
        }
        // SECRET BADGES
        if (pref.getBoolean("secretBadge1", false)) {
            ((ImageView)rootView.findViewById(R.id.imageViewSecret1)).setImageResource(R.drawable.s1);
            ((TextView)rootView.findViewById(R.id.textViewSecret1)).setText("Honesty ribbon!");
        }
        if (pref.getBoolean("secretBadge3", false)) {
            ((ImageView)rootView.findViewById(R.id.imageViewSecret3)).setImageResource(R.drawable.s3);
            ((TextView)rootView.findViewById(R.id.textViewSecret3)).setText("Use pronpass for the first time!");
        }
    }

}
