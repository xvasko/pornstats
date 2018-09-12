package com.matejvasko.pornstat.receivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.matejvasko.pornstat.R;
import com.matejvasko.pornstat.activities.MainActivity;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then posts the notification.
 */
public class WakefulReceiver extends BroadcastReceiver {
    // provides access to the system alarm services.
    private AlarmManager mAlarmManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context.getApplicationContext());
        setAlarm(context,true);
    }

    public void showNotification(Context context) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.app_notification_icon)
                        .setContentTitle("Submit your progress!")
                        .setColor(context.getResources().getColor(R.color.colorNotification))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Notification";
                    String description ="";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel("1", name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(1, mBuilder.build());
    }

    public void setAlarm(Context context, boolean cycle) {
        cancelAlarm(context);

        pref = context.getSharedPreferences("days", MODE_PRIVATE);
        editor = pref.edit();

        int hourOfDay = pref.getInt("hour", -1);
        int minute    = pref.getInt("minute", -1);

        System.out.println(hourOfDay);
        System.out.println(minute);

        if (hourOfDay < 0 || minute < 0) {
            return;
        }

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakefulReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        long alarm;

        if (!cycle) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.MILLISECOND, 00);

            alarm = calendar.getTimeInMillis();
            long now = System.currentTimeMillis();

            if (alarm < now) {
                System.out.println("SET IN PAST");
                alarm += AlarmManager.INTERVAL_DAY;
            } else {
                System.out.println("SET IN FUTURE");
            }
        } else {
            alarm = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;
        }

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm, alarmIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Cancels the next alarm from running. Removes any intents set by this
     * WakefulBroadcastReceiver.
     *
     * @param context the context of the app's Activity
     */
    public void cancelAlarm(Context context) {
        Log.d("WakefulAlarmReceiver", "{cancelAlarm}");

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakefulReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        mAlarmManager.cancel(alarmIntent);

        // Disable {@code BootReceiver} so that it doesn't automatically restart when the device is rebooted.
        //// TODO: you may need to reference the context by ApplicationActivity.class
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

}