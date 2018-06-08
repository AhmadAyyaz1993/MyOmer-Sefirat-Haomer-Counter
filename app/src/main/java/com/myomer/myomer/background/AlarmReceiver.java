package com.sefirah.myomer.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.sefirah.myomer.R;
import com.sefirah.myomer.activities.HomeActivity;
import com.sefirah.myomer.models.MyOmerPeriod;
import com.sefirah.myomer.plist_parser.PListArray;
import com.sefirah.myomer.plist_parser.PListDict;
import com.sefirah.myomer.plist_parser.PListException;
import com.sefirah.myomer.plist_parser.PListParser;
import com.sefirah.myomer.realm.RealmController;
import com.sefirah.myomer.utilty.Utilty;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Ahmad on 3/15/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        generateNotification(k1);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void generateNotification(Context c){
        int currentYear = Utilty.getYear(new Date());
        MyOmerPeriod myOmerPeriod = RealmController.getInstance().getPeriodByYear(currentYear);
        Date startDateOfOmer = myOmerPeriod.getStartDate();
        Date currentDate = new Date();
        String quoteString = "Today is one of the omer day";
        String title = "Count the Omer";
        AssetManager assetManager = c.getAssets();
        int day = Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays();
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        List<String> body = Utilty.readDataFromCSV(c);
        Log.d("Dayyyyyyyyyyy",day+"");
        if (day > 0 && day < 49){
            InputStream stream = null;

            if (day ==1){
                title = "Meaningful Life Center";
            }else {
                title = "Count the Omer";
            }

            try {
                stream = assetManager.open("days/day" + day + ".plist");
                PListDict dict = PListParser.parse(stream);
                PListDict quote = dict.getPListDict("quote");
                quoteString = quote.getString("en");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PListException e) {
                e.printStackTrace();
            }


        }else {
            quoteString = "Today is one of the omer day";
        }
        try {
            InputStream is = assetManager.open("others/MyOmerCalendar2018.plist");
            PListDict dict = PListParser.parse(is);
            PListArray pListArray = dict.getPListArray("reminders");
            if (day > 0 && day < 49) {
                PListDict dayNum = pListArray.getPListDict(day);
                boolean early = dayNum.getBool("early");
                boolean late = dayNum.getBool("late");
                boolean dayEarly = dayNum.getBool("dayEarly");
                boolean twoDaysEarly = dayNum.getBool("2daysEarly");

                if (early == false && late == false && dayEarly == false && twoDaysEarly == false) {
                    Intent intent = new Intent(c, HomeActivity.class);
                    intent.putExtra("DayNumber", day);
                    PendingIntent pIntent = PendingIntent.getActivity(c, (int) System.currentTimeMillis(), intent, 0);

                    // Build notification
                    // Actions are just fake
                    Notification noti = new Notification.Builder(c)
                            .setContentTitle(title)
                            .setContentText(body.get(day))
                            .setSmallIcon(R.mipmap.noti_icon)
                            .setSound(uri)
                            .setAutoCancel(true)
                            .setContentIntent(pIntent).build();
                    NotificationManager notificationManager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;

                    notificationManager.notify(0, noti);
                }
            }else {
                Intent intent = new Intent(c, HomeActivity.class);
                intent.putExtra("DayNumber", day);
                PendingIntent pIntent = PendingIntent.getActivity(c, (int) System.currentTimeMillis(), intent, 0);

                // Build notification
                // Actions are just fake
                Notification noti = new Notification.Builder(c)
                        .setContentTitle(title)
                        .setContentText(body.get(day))
                        .setSmallIcon(R.mipmap.noti_icon)
                        .setSound(uri)
                        .setAutoCancel(true)
                        .setContentIntent(pIntent).build();
                NotificationManager notificationManager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PListException e) {
            e.printStackTrace();
        }





    }


}
