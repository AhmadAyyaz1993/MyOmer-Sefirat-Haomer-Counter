package com.myomer.myomer.realm;

/**
 * Created by ahmad on a3/a8/18.
 */

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.myomer.myomer.models.JournalQuestionModel;
import com.myomer.myomer.models.MyOmerPeriod;
import com.myomer.myomer.models.RecordBlessing;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }



    //find all objects in the Book.class
    public RealmResults<MyOmerPeriod> getBooks() {

        return realm.where(MyOmerPeriod.class).findAll();
    }

    //query a single item with the given id
    public MyOmerPeriod getPeriodByYear(int startDateYear) {

        return realm.where(MyOmerPeriod.class).equalTo("startYear", startDateYear).findFirst();
    }


    public RecordBlessing getRecorderBlessing(int day){
        return realm.where(RecordBlessing.class).equalTo("id",day).findFirst();
    }

    public JournalQuestionModel getAnswer(int day, int questionId){
        return realm.where(JournalQuestionModel.class).equalTo("id",day).and().equalTo("questionId",questionId).findFirst();
    }

    //query example
    public RealmResults<MyOmerPeriod> queryPeriods(String startDate, String endDate) {

        return realm.where(MyOmerPeriod.class)
                .contains("startDate", startDate)
                .or()
                .contains("endDate", endDate)
                .findAll();

    }
}
