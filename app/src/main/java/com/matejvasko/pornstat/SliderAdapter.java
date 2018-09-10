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

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImages = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.b,
    };

    public String[] slideHeadings = {
            "WELCOME",
            "HOW IT WORKS",
            "FAILURE",
            "GOOD LUCK!"
    };

    public String[] slideDescriptions = {
            "Thank you for downloading PornStats.\nLet’s get you started!",
            "Submit your progress for every day.\nYour first goal is to reach 3 days without porn.\n",
            "Everyone has days of weakness.\nIf you fail, you can still keep your progress with pornpass.\nCollect 100 stars to get 1 pornpass.\n",
            "Stay strong and don’t forget to be honest.\nRemember, only person you will be lying to is you."
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

        ImageView slideImage       = view.findViewById(R.id.slide_image);
        TextView  slideHeading     = view.findViewById(R.id.slide_heading);
        TextView  slideDescription = view.findViewById(R.id.slide_description);

        slideImage.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescription.setText(slideDescriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
