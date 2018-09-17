package com.matejvasko.pornstat.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import com.matejvasko.pornstat.R;
import com.matejvasko.pornstat.activities.MainActivity;
import com.matejvasko.pornstat.utils.Utils;
import com.matejvasko.pornstat.receivers.WakefulReceiver;

import static android.content.Context.MODE_PRIVATE;


public class Tab2 extends Fragment implements RewardedVideoAdListener {

    Button earnStars, alarm, alarmCancel;
    TextView totalDays, pornpassDays, pornpassNum, starsNum;
    static TextView notification;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            totalDays.setText(Integer.toString(pref.getInt("totalDays", 0)));
            pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));
            pornpassNum.setText(Integer.toString(pref.getInt("pornpasses", 1)));
            mRewardedVideoAd.loadAd("ca-app-pub-9861673834715515/6206081511", new AdRequest.Builder().build());
        }
    }

    private void prepareUI() {
        totalDays    = getView().findViewById(R.id.total_days_text_view);
        pornpassDays = getView().findViewById(R.id.pornpass_days_text_view);
        pornpassNum  = getView().findViewById(R.id.pornpass_num_text_view);
        starsNum     = getView().findViewById(R.id.stars_num_text_view);
        notification = getView().findViewById(R.id.notification_text_view);

        earnStars = getView().findViewById(R.id.earn_stars_button);
        earnStars.setEnabled(false);
        earnStars.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
        earnStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earnStars.setEnabled(false);
                earnStars.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
//                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
//                } else {
//                    System.out.println("add is not loaded");
//                }
            }
        });

        totalDays.setText(Integer.toString(pref.getInt("totalDays", 0)));
        pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));
        pornpassNum.setText(Integer.toString(pref.getInt("pornpasses", 1)));
        starsNum.setText(Integer.toString(pref.getInt("stars", 0)) + "/100");

        alarm = getView().findViewById(R.id.alarm_button);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getFragmentManager(),"TimePicker");
            }
        });
        alarmCancel = getView().findViewById(R.id.cancel_alarm_button);
        alarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WakefulReceiver wakefulReceiver = new WakefulReceiver();
                wakefulReceiver.cancelAlarm(getActivity().getApplicationContext());
                editor.putInt("hour", -2);
                editor.putInt("minute", -2);
                editor.commit();
                notification.setText("No notification set up");
            }
        });

        updateNotificationTextView(getContext(), pref);
    }

    public void setText(String s){
        System.out.println("AAAA " + s);
    }

    public static void updateNotificationTextView(Context context, SharedPreferences pref) {
        int hour = pref.getInt("hour", -1);
        int minute = pref.getInt("minute", -1);

        if (hour == -1 || minute == -1) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("hour", 11);
            editor.putInt("minute", 0);
            editor.commit();
            WakefulReceiver wakefulReceiver = new WakefulReceiver();
            wakefulReceiver.setAlarm(context,false);
            notification.setText("Notification set to " + Utils.getTimeInCorrectFormat(context, pref.getInt("hour", -1), pref.getInt("minute", -1)));
        } else if (hour == -2 || minute == -2) {
            notification.setText("No notification set up");
        } else {
            notification.setText("Notification set to " + Utils.getTimeInCorrectFormat(context, hour, minute));
        }
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
    }

}
