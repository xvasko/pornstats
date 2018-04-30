package com.matejvasko.pornstat;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivityIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        }, 1000);

    }
}
