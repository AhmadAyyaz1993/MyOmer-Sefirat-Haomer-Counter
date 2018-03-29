package com.myomer.myomer.utilty;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.myomer.myomer.R;
import com.myomer.myomer.helpers.SharedPreferenceHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ahmad on a3/a7/18.
 */

public class Utilty {
    public static boolean isOmerDay(final Date startDate, final Date endDate, final Date currentDate){
        return !(currentDate.after(startDate) || currentDate.before(endDate));
    }
    public static int getDaysTillOmer(Date d1, Date d2) {
        int daysdiff = 0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }
    public static Date toDate(String dateString) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        try {
            date = formatter.parse(dateString);

            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }
    public static int getYear(Date date){
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        return year;
    }
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment newFragment, int containerId){
        final FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public static boolean isFirstTime(Context context) {
        Boolean firstTime = null;
        if (firstTime == null) {
            firstTime = SharedPreferenceHelper.getSharedPreferenceBoolean(context,"firstTime", true);
            if (firstTime) {
                SharedPreferenceHelper.setSharedPreferenceBoolean(context,"firstTime",false);
            }
        }
        return firstTime;
    }

    public static List<String> readDataFromCSV(Context c) {
        InputStream is = c.getResources().openRawResource(R.raw.reminders3);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        String[] tokens = null;
        List<String> bodies = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
               tokens  = line.split(",");
               bodies.add(tokens[3]);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
        return bodies;
    }
}
