package com.matejvasko.pornstat.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.matejvasko.pornstat.R;
import com.matejvasko.pornstat.fragments.Tab2;
import com.matejvasko.pornstat.receivers.WakefulReceiver;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private WakefulReceiver wakefulReceiver;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        pref = getActivity().getSharedPreferences("days", MODE_PRIVATE);
        editor = pref.edit();

        int totalDays = pref.getInt("totalDays", 0);
        int timePickerDialogTheme;
        if (totalDays <= 3) {
            timePickerDialogTheme = R.style.TimePickerDialogTheme1;
        } else if (totalDays <= 8) {
            timePickerDialogTheme = R.style.TimePickerDialogTheme2;
        } else if (totalDays <= 15) {
            timePickerDialogTheme = R.style.TimePickerDialogTheme3;
        } else if (totalDays <= 29) {
            timePickerDialogTheme = R.style.TimePickerDialogTheme4;
        } else if (totalDays <= 50) {
            timePickerDialogTheme = R.style.TimePickerDialogTheme5;
        } else {
            timePickerDialogTheme = R.style.TimePickerDialogTheme6;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timePickerDialogTheme,this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.commit();
        wakefulReceiver = new WakefulReceiver();
        wakefulReceiver.setAlarm(getActivity().getApplicationContext(),false);


        Tab2.updateNotificationTextView(getActivity().getApplicationContext(), pref);
    }
}