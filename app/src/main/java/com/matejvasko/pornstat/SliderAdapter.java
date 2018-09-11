package com.matejvasko.pornstat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ImageView slideImage;
    TextView  slideHeading;
    TextView  slideDescription1;
    TextView  slideDescription2;
    TextView  slideDescription3;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImages = {
            R.drawable.ic_a,
            R.drawable.ic_b,
            R.drawable.ic_c,
            R.drawable.ic_d,
    };

    public String[] slideHeadings = {
            "WELCOME",
            "HOW IT WORKS",
            "FAILURE",
            "GOOD LUCK!"
    };

    public String[] slideDescriptions1 = {
            "Thank you for downloading PornStats.",
            "Submit your progress for every day.",
            "Everyone has days of weakness.",
            "Stay strong and don’t forget to be honest."
    };

    public String[] slideDescriptions2 = {
            "Let’s get you started!",
            "Your first goal is to reach 3 days without \nwatching porn.",
            "If you fail, you can still keep your progress with \npornpass.",
            "Remember, only person you will be lying to is you."
    };

    public String[] slideDescriptions3 = {
            "",
            "",
            "Collect 100 stars to get 1 pornpass.\n",
            ""
    };

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        slideImage       = view.findViewById(R.id.slide_image);
        slideHeading     = view.findViewById(R.id.slide_heading);
        slideDescription1 = view.findViewById(R.id.slide_description1);
        slideDescription2 = view.findViewById(R.id.slide_description2);
        slideDescription3 = view.findViewById(R.id.slide_description3);

        slideImage.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescription1.setText(slideDescriptions1[position]);
        slideDescription2.setText(slideDescriptions2[position]);
        slideDescription3.setText(slideDescriptions3[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
