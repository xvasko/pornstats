package com.matejvasko.pornstat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matejvasko.pornstat.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        pref = getApplicationContext().getSharedPreferences("isNewUser", MODE_PRIVATE);
        final boolean isNewUser = pref.getBoolean("isNewUser", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNewUser) {
                    Intent onBoardingActivityIntent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                    startActivity(onBoardingActivityIntent);
                } else {
                    Intent mainActivityIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                }
            }
        }, 500);
    }
}
