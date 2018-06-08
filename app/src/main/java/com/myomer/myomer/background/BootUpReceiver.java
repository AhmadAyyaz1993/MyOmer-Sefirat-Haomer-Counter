package com.sefirah.myomer.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sefirah.myomer.helpers.SharedPreferenceHelper;

import java.util.Calendar;

/**
 * Created by ahmad on 3/16/18.
 */

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean DailyAlarmEnabled = SharedPreferenceHelper.getSharedPreferenceBoolean(context, "DailyAlarmEnabled", false);
        boolean NightAlarmEnabled = SharedPreferenceHelper.getSharedPreferenceBoolean(context, "NightAlarmEnabled", false);
        if (DailyAlarmEnabled == true){
            int hour =  SharedPreferenceHelper.getSharedPreferenceInt(context,"DailyAlarmHour",0);
            int min = SharedPreferenceHelper.getSharedPreferenceInt(context,"DailyAlarmMin",0);

            if (hour != 0 && min != 0){
                Calendar calNow = Calendar.getInstance();
                calNow.setTimeInMillis(System.currentTimeMillis());
                Calendar calSet = (Calendar) calNow.clone();
                calSet.setTimeInMillis(System.currentTimeMillis());
                calSet.set(Calendar.HOUR_OF_DAY, hour);
                calSet.set(Calendar.MINUTE, min);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                if (calSet.compareTo(calNow) <= 0) {
                    // Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }

                setAlarm(context,calSet,1);
            }
        }
        if (NightAlarmEnabled == true){
           int hour =  SharedPreferenceHelper.getSharedPreferenceInt(context,"NightAlarmHour",0);
            int min = SharedPreferenceHelper.getSharedPreferenceInt(context,"NightAlarmMin",0);

            if (hour != 0 && min != 0){
                Calendar calNow = Calendar.getInstance();
                calNow.setTimeInMillis(System.currentTimeMillis());
                Calendar calSet = (Calendar) calNow.clone();
                calSet.setTimeInMillis(System.currentTimeMillis());
                calSet.set(Calendar.HOUR_OF_DAY, hour);
                calSet.set(Calendar.MINUTE, min);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                if (calSet.compareTo(calNow) <= 0) {
                    // Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }

                setAlarm(context,calSet,2);
            }
        }
    }

    private void setAlarm(Context context,Calendar targetCal, int reqCode) {

        long milis = targetCal.getTimeInMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, reqCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, milis,
                AlarmManager.INTERVAL_DAY, pendingIntent);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
//        else
//            alarmManager.set(ALARM_TYPE, milis, pendingIntent);


    }
}
