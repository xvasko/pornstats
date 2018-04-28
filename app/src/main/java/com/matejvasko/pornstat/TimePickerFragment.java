package com.matejvasko.pornstat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;



public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private WakefulReceiver wakefulReceiver;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerDialogTheme,this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        wakefulReceiver = new WakefulReceiver();
        wakefulReceiver.setAlarm(getActivity().getApplicationContext(), hourOfDay, minute);
        Toast.makeText(getActivity(), "Hour: " + hourOfDay + " Minute: " + minute, Toast.LENGTH_SHORT).show();
    }
}