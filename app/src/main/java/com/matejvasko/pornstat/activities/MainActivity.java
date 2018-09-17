package com.matejvasko.pornstat.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.matejvasko.pornstat.R;
import com.matejvasko.pornstat.adapters.PagerAdapter;
import com.matejvasko.pornstat.fragments.Tab1;
import com.matejvasko.pornstat.fragments.Tab2;

public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-9861673834715515~9871538949");

        pref = getApplicationContext().getSharedPreferences("days", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.addTab(tabLayout.newTab().setText("Badges"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        updateSelectedTabIndicatorColor(getResources(), pref.getInt("totalDays", 0), tabLayout);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    static public void updateSelectedTabIndicatorColor(Resources resources, int totalDays, TabLayout tabLayout) {
        if (totalDays <= 3) {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar1));
        } else if (totalDays <= 8) {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar2));
        } else if (totalDays <= 15) {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar3));
        } else if (totalDays <= 29) {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar4));
        } else if (totalDays <= 50) {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar5));
        } else {
            tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.colorCircleProgressBar6));
        }
    }

}
