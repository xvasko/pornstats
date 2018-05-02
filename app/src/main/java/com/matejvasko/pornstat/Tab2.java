package com.matejvasko.pornstat;

import android.app.DialogFragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static android.content.Context.MODE_PRIVATE;


public class Tab2 extends Fragment implements RewardedVideoAdListener {

    Button earnStars;
    Button alarm;

    static TextView totalDays;
    static TextView pornpassDays;
    static TextView pornpassNum;
    static TextView starsNum;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RewardedVideoAd mRewardedVideoAd;

    IconManager iconManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iconManager = new IconManager();

        pref = getContext().getSharedPreferences("days", MODE_PRIVATE);
        editor = pref.edit();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-9861673834715515/6206081511", new AdRequest.Builder().build());

        prepareUI();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mRewardedVideoAd.loadAd("ca-app-pub-9861673834715515/6206081511", new AdRequest.Builder().build());
        } else {
        }
    }

    private void prepareUI() {
        totalDays    = getView().findViewById(R.id.total_days_text_view);
        pornpassDays = getView().findViewById(R.id.pornpass_days_text_view);
        pornpassNum  = getView().findViewById(R.id.pornpass_num_text_view);
        starsNum     = getView().findViewById(R.id.stars_num_text_view);

        earnStars    = getView().findViewById(R.id.earn_stars_button);
        earnStars.setEnabled(false);
        earnStars.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
        earnStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earnStars.setEnabled(false);
                earnStars.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    System.out.println("add is not loaded");
                }
            }
        });

        totalDays.setText(Integer.toString(pref.getInt("totalDays", 0)));
        pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));
        pornpassNum.setText(Integer.toString(pref.getInt("pornpasses", 1)));
        starsNum.setText(Integer.toString(pref.getInt("stars", 0)) + "/100");

        alarm        = getView().findViewById(R.id.alarm_button);
        alarm.setTypeface(iconManager.getIcons("fonts/MaterialIcons-Regular.ttf", getContext()));
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getFragmentManager(),"TimePicker");
            }
        });
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        earnStars.setEnabled(true);
        earnStars.setTextColor(getResources().getColor(R.color.colorWhite));
        Log.i("MainActivity", "An ad has loaded.");
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardedVideoAd.loadAd("ca-app-pub-9861673834715515/6206081511", new AdRequest.Builder().build());
        Log.i("MainActivity", "An ad has closed.");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        editor.putInt("stars", pref.getInt("stars", 0) + rewardItem.getAmount());
        editor.commit();

        if (pref.getInt("stars", 0) >= 100) {
            editor.putInt("pornpasses", pref.getInt("pornpasses", 1) + 1);
            editor.putInt("stars", pref.getInt("stars", 0) % 100);
            editor.commit();
        }
        pornpassNum.setText(Integer.toString(pref.getInt("pornpasses", 1)));
        starsNum.setText(Integer.toString(pref.getInt("stars", 0)) + "/100");
        Toast.makeText(getContext(), String.format("You have received %d stars!", rewardItem.getAmount()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
