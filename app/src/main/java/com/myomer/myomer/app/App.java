package com.myomer.myomer.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import com.myomer.myomer.background.AlarmReceiver;
import com.myomer.myomer.helpers.SharedPreferenceHelper;
import com.myomer.myomer.models.MyOmerPeriod;
import com.myomer.myomer.plist_parser.PListArray;
import com.myomer.myomer.plist_parser.PListDict;
import com.myomer.myomer.plist_parser.PListException;
import com.myomer.myomer.plist_parser.PListParser;
import com.myomer.myomer.realm.RealmController;
import com.myomer.myomer.utilty.Constants;
import com.myomer.myomer.utilty.Utilty;

import org.ankit.gpslibrary.MyTracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ahmad on 3/8/18.
 */

public class App extends Application {
    private Realm realm;
    String format;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();
        MyTracker tracker=new MyTracker(App.this);
        Location location = new Location(tracker.getLatitude()+"", tracker.getLongitude()+"");
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar c = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());

        int hourOfDay =c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int AM_PM = c.get(Calendar.AM_PM);

        if (AM_PM == Calendar.AM){
            format = "AM";
        }else {
            format = "PM";
        }


        SharedPreferenceHelper.setSharedPreferenceInt(App.this, "NightfallHour", c.get(Calendar.HOUR));
        SharedPreferenceHelper.setSharedPreferenceInt(App.this, "NightfallMin", c.get(Calendar.MINUTE));
        SharedPreferenceHelper.setSharedPreferenceString(App.this, "NightfallFormat", format);
        SharedPreferenceHelper.setSharedPreferenceBoolean(App.this, "NightFallEnabled", true);
        if (Utilty.isFirstTime(this)) {
            SharedPreferenceHelper.setSharedPreferenceInt(App.this,"NightAlarmHour",20);
            SharedPreferenceHelper.setSharedPreferenceInt(App.this,"NightAlarmMin",0);
            SharedPreferenceHelper.setSharedPreferenceBoolean(App.this,"NightAlarmEnabled",true);

            Calendar calNow = Calendar.getInstance();
            calNow.setTimeInMillis(System.currentTimeMillis());
            Calendar calSet = (Calendar) calNow.clone();
            calSet.setTimeInMillis(System.currentTimeMillis());
            calSet.set(Calendar.HOUR_OF_DAY, 20);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.AM_PM, Calendar.PM);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet,1);
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            for (int i = 0; i < Constants.myOmerStartDates.length; i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(Utilty.toDate(Constants.myOmerStartDates[i]));
                cal.set(Calendar.HOUR,hourOfDay);
                cal.set(Calendar.MINUTE,minute);
                if (format.equals("AM"))
                    cal.set(Calendar.AM_PM,0);
                else
                    cal.set(Calendar.AM_PM,1);

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(Utilty.toDate(Constants.myOmerEndDates[i]));
                cal2.set(Calendar.HOUR,hourOfDay);
                cal2.set(Calendar.MINUTE,minute);
                if (format.equals("AM"))
                    cal2.set(Calendar.AM_PM,0);
                else
                    cal2.set(Calendar.AM_PM,1);
                MyOmerPeriod myOmerPeriod = new MyOmerPeriod();
                myOmerPeriod.setId(i + System.currentTimeMillis());
                myOmerPeriod.setStartDate(new Date(cal.getTimeInMillis()));
                myOmerPeriod.setEndDate(new Date(cal2.getTimeInMillis()));
                myOmerPeriod.setStartYear(Utilty.getYear(Utilty.toDate(Constants.myOmerStartDates[i])));
                realm.copyToRealm(myOmerPeriod);
            }

            realm.commitTransaction();


        }
        initiateAlarms();
    }

//    private void initiateAlarms(){
//        AssetManager assetManager = getAssets();
//
//        InputStream stream = null;
//
//        try {
//            stream = assetManager.open("others/MyOmerCalendar.plist");
//            PListArray array = PListParser.parse(stream);
//            //PListArray array = dict.getPListArray();
//            for (int k = 0 ; k < array.size();k++) {
//
//                PListDict pListDict = array.getPListDict(k);
//                PListArray pListArray = pListDict.getPListArray("reminders");
//                int allDays = pListArray.size();
//
//                for (int i = 0; i < allDays; i++) {
//                    PListDict day = pListArray.getPListDict(i);
//                    boolean early = day.getBool("early");
//                    boolean late = day.getBool("late");
//                    boolean dayEarly = day.getBool("dayEarly");
//                    boolean twoDaysEarly = day.getBool("2daysEarly");
//
//                    setAlarm(early, late, dayEarly, twoDaysEarly, i);
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (PListException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void initiateAlarms(){
        AssetManager assetManager = getAssets();

        InputStream stream = null;

        try {
            stream = assetManager.open("others/MyOmerCalendar"+Utilty.getYear(new Date())+".plist");
            PListDict dict = PListParser.parse(stream);
            //PListArray array = dict.getPListArray();

            PListArray pListArray = dict.getPListArray("reminders");
            Date startDate = dict.getDate("start");
            int allDays = pListArray.size();

            for (int i = 0; i < allDays; i++) {
                PListDict day = pListArray.getPListDict(i);
                boolean early = day.getBool("early");
                boolean late = day.getBool("late");
                boolean dayEarly = day.getBool("dayEarly");
                boolean twoDaysEarly = day.getBool("2daysEarly");

                setAlarm(early, late, dayEarly, twoDaysEarly, i,startDate);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PListException e) {
            e.printStackTrace();
        }


    }

    private void setAlarm(boolean early, boolean late, boolean dayEarly, boolean twoDaysEarly,int day,Date startDate){
        int currentYear = Utilty.getYear(new Date());
        MyOmerPeriod myOmerPeriod = RealmController.with(this).getPeriodByYear(currentYear);
        Date startDateOfOmer = myOmerPeriod.getStartDate();
        Calendar calSet = Calendar.getInstance();
        calSet.setTimeInMillis(startDate.getTime());
        calSet.add(Calendar.DATE,day);
        if (early == true){
            calSet.set(Calendar.HOUR_OF_DAY, 14);
            calSet.set(Calendar.MINUTE, 30);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            Log.e("Alaaaaarm: ", String.valueOf(new Date(calSet.getTimeInMillis())));
//            if (calSet.compareTo(calNow) <= 0) {
//                // Today Set time passed, count to tomorrow
//                calSet.add(Calendar.DATE, 1);
//            }
            long milis = calSet.getTimeInMillis();
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), day+10, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

            alarmManager.cancel(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
            else
                alarmManager.set(ALARM_TYPE, milis, pendingIntent);

        }
        if (late == true){
            calSet.set(Calendar.HOUR_OF_DAY, 22);
            calSet.set(Calendar.MINUTE, 30);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            Log.e("Alaaaaarm: ", String.valueOf(new Date(calSet.getTimeInMillis())));
//            if (calSet.compareTo(calNow) <= 0) {
//                // Today Set time passed, count to tomorrow
//                calSet.add(Calendar.DATE, 1);
//            }
            long milis = calSet.getTimeInMillis();
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), day+10, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
            alarmManager.cancel(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
            else
                alarmManager.set(ALARM_TYPE, milis, pendingIntent);

        }
        if (dayEarly == true){
            calSet.set(Calendar.HOUR_OF_DAY, 14);
            calSet.set(Calendar.MINUTE, 30);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            calSet.add(Calendar.DATE, -1);
            Log.e("Alaaaaarm: ", String.valueOf(new Date(calSet.getTimeInMillis())));
            long milis = calSet.getTimeInMillis();
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), day+10, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

            alarmManager.cancel(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
            else
                alarmManager.set(ALARM_TYPE, milis, pendingIntent);

        }
        if (twoDaysEarly == true){
            calSet.set(Calendar.HOUR_OF_DAY, 14);
            calSet.set(Calendar.MINUTE, 30);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            calSet.add(Calendar.DATE, -2);
            Log.e("Alaaaaarm: ", String.valueOf(new Date(calSet.getTimeInMillis())));
            long milis = calSet.getTimeInMillis();
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), day+10, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

            alarmManager.cancel(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
            else
                alarmManager.set(ALARM_TYPE, milis, pendingIntent);

        }

        SharedPreferenceHelper.setSharedPreferenceBoolean(this,"FirstRun",true);
    }
    private void setAlarm(Calendar targetCal, int reqCode) {

        long milis = targetCal.getTimeInMillis();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), reqCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, milis,
                AlarmManager.INTERVAL_DAY, pendingIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                        alarmManager.setExactAndAllowWhileIdle(ALARM_TYPE, milis, pendingIntent);
//                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                        alarmManager.setExact(ALARM_TYPE, milis, pendingIntent);
//                    else
//                        alarmManager.set(ALARM_TYPE, milis, pendingIntent);




    }

}
