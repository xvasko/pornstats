package com.matejvasko.pornstat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matejvasko.pornstat.R;
import com.matejvasko.pornstat.adapters.SliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mBottomLinearLayout;

    TextView mBackButton;
    TextView mNextButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    SliderAdapter sliderAdapter;

    TextView[] mDots;

    int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        pref = getApplicationContext().getSharedPreferences("isNewUser", MODE_PRIVATE);
        editor = pref.edit();

        prepareUI();

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        addDotsIndicator(0);

        final Intent intent = new Intent(getBaseContext(), MainActivity.class);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage == mDots.length - 1) {
                    editor.putBoolean("isNewUser", false);
                    editor.apply();
                    startActivity(intent);
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[4];
        mBottomLinearLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
            mBottomLinearLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void prepareUI() {
        mSlideViewPager     = findViewById(R.id.slide_view_pager);
        mBottomLinearLayout = findViewById(R.id.bottom_linear_layout);
        mBackButton = findViewById(R.id.back_button);
        mNextButton = findViewById(R.id.next_button);
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0) {
                mBackButton.setText("");
                mBackButton.setEnabled(false);
                mBackButton.setVisibility(View.INVISIBLE);
                mNextButton.setText("NEXT");
                mNextButton.setEnabled(true);
            } else if (position == mDots.length - 1) {
                mBackButton.setText("BACK");
                mBackButton.setEnabled(true);
                mBackButton.setVisibility(View.VISIBLE);
                mNextButton.setText("FINISH");
                mBackButton.setEnabled(true);
            } else {
                mBackButton.setText("BACK");
                mBackButton.setEnabled(true);
                mBackButton.setVisibility(View.VISIBLE);
                mNextButton.setText("NEXT");
                mBackButton.setEnabled(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
