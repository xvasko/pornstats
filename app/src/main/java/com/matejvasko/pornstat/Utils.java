package com.matejvasko.pornstat;

import android.content.Context;
import android.text.format.DateFormat;

public class Utils {

    public static String getTimeInCorrectFormat(Context context, int hours, int minutes) {

        if (!DateFormat.is24HourFormat(context)) {

            String time;

            if (hours < 12) {
                time = hours + ":" + getFormattedMinutes(minutes) + " am";
            } else if (hours == 12) {
                time = hours + ":" + getFormattedMinutes(minutes) + " pm";
            } else {
                time = (hours % 12) + ":" + getFormattedMinutes(minutes) + " pm";
            }

            return time;

        } else {
            return hours + ":" + getFormattedMinutes(minutes);
        }
    }

    private static String getFormattedMinutes(int minutes) {
        if (minutes < 10) {
            return "0" + minutes;
        } else {
            return "" + minutes;
        }
    }

}
