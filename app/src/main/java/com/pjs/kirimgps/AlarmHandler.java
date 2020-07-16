package com.pjs.kirimgps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {

    private Context context;

    public AlarmHandler(Context context)
    {
        this.context = context;
    }

    public void setAlarmManager()
    {
        Intent intent = new Intent(context, MyServices.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (am != null)
        {
//            the time is calculated in milliseconds / 60 minutes * 60 seconds * 1000 miliseconds = 1 hour

            long triggerAfter = 60 * 60 * 1000; //this will trigger the service after 1 hour
            long triggerEvery = 60 * 60 * 1000; //this will repeat it every hour after that
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);
        }

    }

//    this wil cancel the alarm
    public void cancelAlarm()
    {
        Intent intent = new Intent(context, MyServices.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null)
        {
            am.cancel(sender);
        }
    }

}
