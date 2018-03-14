package com.myomer.myomer.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.myomer.myomer.R;
import com.myomer.myomer.models.MyOmerPeriod;
import com.myomer.myomer.plist_parser.PListDict;
import com.myomer.myomer.plist_parser.PListException;
import com.myomer.myomer.plist_parser.PListParser;
import com.myomer.myomer.realm.RealmController;
import com.myomer.myomer.utilty.Utilty;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitingScreenActivity extends AppCompatActivity {

    public String startDateOfOmer = "a31/03/2018";
    TextView tvDaysTillOmer;
    int currentYear;
    int daysTillOmer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_waiting_screen);

        initViews();
        currentYear = Utilty.getYear(new Date());
        MyOmerPeriod myOmerPeriod = RealmController.with(this).getPeriodByYear(currentYear);
        startDateOfOmer = myOmerPeriod.getStartDate().toString();
        //DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        // Convert from String to Date
        Date startDate = new Date(startDateOfOmer);
        Date currentDate = new Date();
        daysTillOmer = Utilty.getDaysTillOmer(currentDate,startDate);
        tvDaysTillOmer.setText(daysTillOmer+"");


        if (daysTillOmer > 0){
            startActivity(new Intent(WaitingScreenActivity.this,HomeActivity.class));
            finish();
        }


    }

    private void initViews(){
        tvDaysTillOmer = (TextView) findViewById(R.id.tvDaysTillOmer);
    }
}
