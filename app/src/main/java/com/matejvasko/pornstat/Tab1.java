package com.matejvasko.pornstat;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Tab1 extends Fragment {

    TextView goal;
    TextView daysOfCurrentChallenge;
    TextView date;
    TextView question;
    CircleProgressBar circleProgressBar;
    CircleProgressBar circleProgressBar1;
    CircleProgressBar circleProgressBar2;
    CircleProgressBar circleProgressBar3;
    CircleProgressBar circleProgressBar4;
    CircleProgressBar circleProgressBar5;
    CircleProgressBar circleProgressBar6;
    Button yesButton;
    Button noButton;
    ConstraintLayout starsEarnedLayout;

    Handler handle = new Handler();
    int[] days    = new int[] {0, 0, 0,  0,  0,  0};
    int[] daysCap = new int[] {3, 5, 7, 14, 21, 28};

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private AdView mAdView;

    Calendar calendar;
    boolean wasAnsweredToday;

    private void retrieveDays() {
        int totalDays = pref.getInt("totalDays", 0);

        for (int i = 0; i < 6; i++) {
            if (totalDays == 0) break;
            if (totalDays - daysCap[i] < 0) {
                days[i] = totalDays;
                break;
            } else {
                days[i] = daysCap[i];
                totalDays -= daysCap[i];
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setTranslationZ(starsEarnedLayout, 1);

        pref = getContext().getSharedPreferences("days", MODE_PRIVATE);
        editor = pref.edit();
        wasAnsweredToday = pref.getBoolean("wasAnsweredToday", false);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        retrieveDays();
        prepareUI();
        updateUI();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSuccessfulDay();
            }
        });

        MobileAds.initialize(getContext(), "ca-app-pub-9861673834715515~9871538949");

        mAdView = getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("9F16DA0A735C9B0DBB4412A8FA4B6972").build();
        mAdView.loadAd(adRequest);

    }

    private void prepareUI() {
        question = getView().findViewById(R.id.question_text_view);
        date = getView().findViewById(R.id.date_text_view);
        daysOfCurrentChallenge = getView().findViewById(R.id.days_of_current_challenge_text_view);
        yesButton = getView().findViewById(R.id.yes_button);
        noButton  = getView().findViewById(R.id.no_button);
        goal = getView().findViewById(R.id.goal_text_view);
        goal.setText(Html.fromHtml("Your goal is <b>3</b> days"));

        if (wasAnsweredToday) {
            Calendar calendar1 = Calendar.getInstance();
            Date today = calendar1.getTime();
            if (today.after(new Date(pref.getLong("midnight", 0)))) {
                wasAnsweredToday = false;
                editor.putBoolean("wasAnsweredToday", false);
                editor.commit();
                showTodayUI();
            } else {
                showTomorrowUI(pref.getBoolean("wasYesterdaySuccessful", true));
            }
        } else {
            showTodayUI();
        }

        circleProgressBar = getView().findViewById(R.id.custom_progressBar);
        circleProgressBar1 = getView().findViewById(R.id.custom_progressBar1);
        circleProgressBar1.setColor(getResources().getColor(R.color.colorCircleProgressBar1));
        circleProgressBar2 = getView().findViewById(R.id.custom_progressBar2);
        circleProgressBar2.setColor(getResources().getColor(R.color.colorCircleProgressBar2));
        circleProgressBar3 = getView().findViewById(R.id.custom_progressBar3);
        circleProgressBar3.setColor(getResources().getColor(R.color.colorCircleProgressBar3));
        circleProgressBar4 = getView().findViewById(R.id.custom_progressBar4);
        circleProgressBar4.setColor(getResources().getColor(R.color.colorCircleProgressBar4));
        circleProgressBar5 = getView().findViewById(R.id.custom_progressBar5);
        circleProgressBar5.setColor(getResources().getColor(R.color.colorCircleProgressBar5));
        circleProgressBar6 = getView().findViewById(R.id.custom_progressBar6);
        circleProgressBar6.setColor(getResources().getColor(R.color.colorCircleProgressBar6));

    }

    private void updateUI() {

        goal.setText(Html.fromHtml("Your goal is <b>" + getCurrentChallenge() + "</b> days"));

        if (days[0] == daysCap[0]) {
            circleProgressBar1.setProgressWithAnimation(100);
        } else if (days[0] == 0) {
            return;
        } else {
            circleProgressBar1.setProgressWithAnimation((100f / daysCap[0]) * days[0]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[0]) * days[0]);
            setMainProgressbarColor(0);
            daysOfCurrentChallenge.setText(Integer.toString(days[0]));
        }

        if (days[1] == daysCap[1]) {
            circleProgressBar2.setColor(getResources().getColor(R.color.colorCircleProgressBar2));
            circleProgressBar2.setProgressWithAnimation(100);
        } else if (days[1] == 0) {
            return;
        } else {
            circleProgressBar2.setProgressWithAnimation((100f / daysCap[1]) * days[1]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[1]) * days[1]);
            setMainProgressbarColor(1);
            daysOfCurrentChallenge.setText(Integer.toString(days[1]));
        }

        if (days[2] == daysCap[2]) {
            circleProgressBar3.setColor(getResources().getColor(R.color.colorCircleProgressBar3));
            circleProgressBar3.setProgressWithAnimation(100);
        } else if (days[2] == 0) {
            return;
        } else {
            circleProgressBar3.setProgressWithAnimation((100f / daysCap[2]) * days[2]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[2]) * days[2]);
            setMainProgressbarColor(2);
            daysOfCurrentChallenge.setText(Integer.toString(days[2]));
        }

        if (days[3] == daysCap[3]) {
            circleProgressBar4.setColor(getResources().getColor(R.color.colorCircleProgressBar4));
            circleProgressBar4.setProgressWithAnimation(100);
        } else if (days[3] == 0) {
            return;
        } else {
            circleProgressBar4.setProgressWithAnimation((100f / daysCap[3]) * days[3]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[3]) * days[3]);
            setMainProgressbarColor(3);
            daysOfCurrentChallenge.setText(Integer.toString(days[3]));
        }

        if (days[4] == daysCap[4]) {
            circleProgressBar5.setColor(getResources().getColor(R.color.colorCircleProgressBar5));
            circleProgressBar5.setProgressWithAnimation(100);
        } else if (days[4] == 0) {
            return;
        } else {
            circleProgressBar5.setProgressWithAnimation((100f / daysCap[4]) * days[4]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[4]) * days[4]);
            setMainProgressbarColor(4);
            daysOfCurrentChallenge.setText(Integer.toString(days[4]));
        }

        if (days[5] == daysCap[5]) {
            circleProgressBar6.setColor(getResources().getColor(R.color.colorCircleProgressBar6));
            circleProgressBar6.setProgressWithAnimation(100);
        } else if (days[5] == 0) {
            return;
        } else {
            circleProgressBar6.setProgressWithAnimation((100f / daysCap[5]) * days[5]);
            circleProgressBar.setProgressWithAnimation((100f / daysCap[5]) * days[5]);
            setMainProgressbarColor(5);
            daysOfCurrentChallenge.setText(Integer.toString(days[5]));
        }

        if (pref.getInt("totalDays", 0) > 78) {
            int totalDays = pref.getInt("totalDays", 0);
            circleProgressBar.setProgressWithAnimation((100f / 28) * ((totalDays - 78) % 28));
            setMainProgressbarColor(5);
            daysOfCurrentChallenge.setText(Integer.toString((totalDays - 78) % 28));
        }

    }

    private void resetUI() {
        goal.setText(Html.fromHtml("Your goal is <b>" + getCurrentChallenge() + "</b> days"));
        daysOfCurrentChallenge.setText("0");
        circleProgressBar.setProgressWithAnimation(0);
        circleProgressBar1.setProgressWithAnimation(0);
        circleProgressBar2.setProgressWithAnimation(0);
        circleProgressBar3.setProgressWithAnimation(0);
        circleProgressBar4.setProgressWithAnimation(0);
        circleProgressBar5.setProgressWithAnimation(0);
        circleProgressBar6.setProgressWithAnimation(0);
        MainActivity.updateSelectedTabIndicatorColor(getResources(), pref.getInt("totalDays", 0));
    }

    private void addSuccessfulDay() {
        if (days[0] != daysCap[0]) {
            addSuccessfulDay(0);
        } else if (days[1] != daysCap[1]) {
            addSuccessfulDay(1);
        } else if (days[2] != daysCap[2]) {
            addSuccessfulDay(2);
        } else if (days[3] != daysCap[3]) {
            addSuccessfulDay(3);
        } else if (days[4] != daysCap[4]) {
            addSuccessfulDay(4);
        } else if (days[5] != daysCap[5]) {
            addSuccessfulDay(5);
        }

        editor.putInt("totalDays", pref.getInt("totalDays", 0) + 1);
        editor.putInt("pornpassDays", pref.getInt("pornpassDays", 0) + 1);
        editor.commit();
        Tab2.totalDays.setText(Integer.toString(pref.getInt("totalDays", 0)));
        Tab2.pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));

        if (pref.getInt("totalDays", 0) > 78) {
            int totalDays = pref.getInt("totalDays", 0);
            circleProgressBar.setProgressWithAnimation((100f / 28) * ((totalDays - 78) % 28));
            //System.out.println(pref.getInt("totalDays", 0));
            daysOfCurrentChallenge.setText(Integer.toString((totalDays - 78) % 28));
        }

        editor.putBoolean("wasAnsweredToday", true);
        editor.putLong("midnight", calendar.getTime().getTime());
        editor.putBoolean("wasYesterdaySuccessful", true);
        editor.commit();
        System.out.println("MIDNIGHT:" + pref.getLong("midnight", 0));
        showTomorrowUI(true);

        MainActivity.updateSelectedTabIndicatorColor(getResources(), pref.getInt("totalDays", 0));
    }

    private void showTodayUI(){
        question.setText("Did you watch porn yesterday?");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, getResources().getConfiguration().locale);
        Date myDate = new Date();
        // get yesterday date
        myDate.setTime(myDate.getTime() - TimeUnit.DAYS.toMillis(1));
        date.setText(df.format(myDate));
        yesButton.setVisibility(View.VISIBLE);
//        yesButton.setTextColor(getResources().getColor(R.color.colorWhite));
        noButton.setVisibility(View.VISIBLE);
//        noButton.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void showTomorrowUI(boolean wasSuccessful){
        String text;
        if (wasSuccessful) {
            text = "Good job!";
        } else {
            text = "Maybe next time!";
        }
        date.setText("Come back tomorrow");
        question.setText(text);
        yesButton.setVisibility(View.INVISIBLE);
//        yesButton.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
        noButton.setVisibility(View.INVISIBLE);
//        noButton.setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
    }

    private void addSuccessfulDay(final int i) {
        setMainProgressbarColor(i);
        days[i] += 1;
        float graphPart = 100f / daysCap[i];
        if (days[i] == daysCap[i]) {
            daysOfCurrentChallenge.setText(String.valueOf(days[i]));
            circleProgressBar.setProgressWithAnimation(graphPart * days[i]);
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (i != 5) {
                        goal.setText(Html.fromHtml("Your goal is <b>" + daysCap[i+1] + "</b> days"));
                    }
                    daysOfCurrentChallenge.setText("0");
                    circleProgressBar.setProgress(0);
                }
            }, 1550);
        } else {
            daysOfCurrentChallenge.setText(String.valueOf(days[i]));
            circleProgressBar.setProgressWithAnimation(graphPart * days[i]);
        }

        addSuccessfulDaySmallGraph(i, graphPart);

    }

    private void addSuccessfulDaySmallGraph(int i, float graphPart) {
        if (i == 0) {
            circleProgressBar1.setProgressWithAnimation(graphPart * days[i]);
        } else if (i == 1) {
            circleProgressBar2.setProgressWithAnimation(graphPart * days[i]);
        } else if (i == 2) {
            circleProgressBar3.setProgressWithAnimation(graphPart * days[i]);
        } else if (i == 3) {
            circleProgressBar4.setProgressWithAnimation(graphPart * days[i]);
        } else if (i == 4) {
            circleProgressBar5.setProgressWithAnimation(graphPart * days[i]);
        } else if (i == 5) {
            circleProgressBar6.setProgressWithAnimation(graphPart * days[i]);
        }
    }

    private void setMainProgressbarColor(int i) {
        if (i == 0) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar1));
        } else if (i == 1) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar2));
        } else if (i == 2) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar3));
        } else if (i == 3) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar4));
        } else if (i == 4) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar5));
        } else if (i == 5) {
            circleProgressBar.setColor(getResources().getColor(R.color.colorCircleProgressBar6));
        }
    }

    private void showDialog() {
        String title;
        String message;

        final int pornpasses = pref.getInt("pornpasses", 1);

        if (pornpasses == 0) {
            title = "You have <b>0</b> pornpasses!";
            message = "You can get pornpass by earning 100 stars. <br> To earn stars, go to STATISTICS tab.";
        } else if (pornpasses == 1) {
            title = "You have <b>1</b> pornpass!";
            message = "Do you want to use it? <br> Your progress will be kept, otherwise you start from the beginning.";
        } else {
            title = "You have <b>" + pref.getInt("pornpasses", 0) + "</b> pornpasses!";
            message = "Do you want to use one? <br> Your progress will be kept, otherwise you start from the beginning.";
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(Html.fromHtml(title))
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do not add successful day
                        editor.putInt("pornpassDays", 0);
                        editor.putInt("pornpasses", pref.getInt("pornpasses", 1) - 1);
                        editor.putBoolean("wasAnsweredToday", true);
                        editor.putLong("midnight", calendar.getTime().getTime());
                        editor.putBoolean("wasYesterdaySuccessful", true);
                        editor.commit();
                        System.out.println("MIDNIGHT:" + pref.getLong("midnight", 0));
                        showTomorrowUI(true);

                        Tab2.pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));
                        Tab2.pornpassNum .setText(Integer.toString(pref.getInt("pornpasses", 1)));

                    }
                })
                .setNegativeButton("Don't use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        days = new int[] {0, 0, 0, 0, 0, 0};
                        editor.putInt("totalDays", 0);
                        editor.putInt("pornpassDays", 0);
                        editor.putBoolean("wasAnsweredToday", true);
                        editor.putLong("midnight", calendar.getTime().getTime());
                        editor.putBoolean("wasYesterdaySuccessful", false);
                        editor.commit();
                        System.out.println("MIDNIGHT:" + pref.getLong("midnight", 0));
                        showTomorrowUI(false);

                        Tab2.totalDays.setText(Integer.toString(pref.getInt("totalDays", 0)));
                        Tab2.pornpassDays.setText(Integer.toString(pref.getInt("pornpassDays", 0)));
                        resetUI();
                    }
                }).create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(18f);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorCircleProgressBar1));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(18f);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorCircleProgressBar1));
                if (pornpasses == 0) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorCircleProgressBar1Transparent));
                }
            }
        });
        alertDialog.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private int getCurrentChallenge() {
        for (int i = 0; i < days.length; i++) {
            if (days[i] != daysCap[i]) {
                return daysCap[i];
            }
        }
        return 28;
    }

}
