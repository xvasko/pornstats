package com.matejvasko.pornstat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

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

        //Create and return a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerDialogTheme,this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.commit();
        wakefulReceiver = new WakefulReceiver();
        wakefulReceiver.setAlarm(getActivity().getApplicationContext(),false);
        Toast.makeText(getActivity(), "Hour: " + hourOfDay + " Minute: " + minute, Toast.LENGTH_SHORT).show();
    }
}