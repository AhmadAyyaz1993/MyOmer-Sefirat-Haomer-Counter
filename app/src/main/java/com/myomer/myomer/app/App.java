package com.myomer.myomer.app;

import android.app.Application;

import com.myomer.myomer.models.MyOmerPeriod;
import com.myomer.myomer.realm.RealmController;
import com.myomer.myomer.utilty.Constants;
import com.myomer.myomer.utilty.Utilty;

import io.realm.Realm;
import io.realm.RealmCollection;
import io.realm.RealmConfiguration;

/**
 * Created by ahmad on 3/8/18.
 */

public class App extends Application {
    private Realm realm;
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
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        for (int i=0 ; i< Constants.myOmerStartDates.length;i++){
            MyOmerPeriod myOmerPeriod = new MyOmerPeriod();
            myOmerPeriod.setId(i+System.currentTimeMillis());
            myOmerPeriod.setStartDate(Utilty.toDate(Constants.myOmerStartDates[i]));
            myOmerPeriod.setEndDate(Utilty.toDate(Constants.myOmerEndDates[i]));
            myOmerPeriod.setStartYear(Utilty.getYear(Utilty.toDate(Constants.myOmerStartDates[i])));
            realm.copyToRealm(myOmerPeriod);
        }

        realm.commitTransaction();

    }
}
