package com.matejvasko.pornstat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    WakefulReceiver wakefulReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //// TODO: use calendar.add(Calendar.SECOND,MINUTE,HOUR, int);
            calendar.add(Calendar.SECOND, 20);

            //ALWAYS recompute the calendar after using add, set, roll
            wakefulReceiver = new WakefulReceiver();
            wakefulReceiver.setAlarm(context.getApplicationContext(),  false);
        }
    }
}