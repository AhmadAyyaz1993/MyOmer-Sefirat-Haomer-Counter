package com.myomer.myomer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.myomer.myomer.R;
import com.myomer.myomer.fragments.BlessingsFragment;
import com.myomer.myomer.models.MyOmerPeriod;
import com.myomer.myomer.models.RecordBlessing;
import com.myomer.myomer.realm.RealmController;
import com.myomer.myomer.utilty.Utilty;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class OmerChartActivity extends AppCompatActivity {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5,expandableLayout6,expandableLayout7;
    ImageView ivBackButton;
    Toolbar toolbar;
    int[] ids = {R.id.llDay1,R.id.llDay2,R.id.llDay3,R.id.llDay4,R.id.llDay5,R.id.llDay6,R.id.llDay7,R.id.llDay8,R.id.llDay9,R.id.llDay10,R.id.llDay11,R.id.llDay12,R.id.llDay13,R.id.llDay14,R.id.llDay15,
            R.id.llDay16,R.id.llDay17,R.id.llDay18,R.id.llDay19,R.id.llDay20,R.id.llDay21,R.id.llDay22,R.id.llDay23,R.id.llDay24,R.id.llDay25,R.id.llDay26,R.id.llDay27,R.id.llDay28,R.id.llDay29,R.id.llDay30,R.id.llDay31,
            R.id.llDay32,R.id.llDay33,R.id.llDay34,R.id.llDay35,R.id.llDay36,R.id.llDay37,R.id.llDay38,R.id.llDay39,R.id.llDay40,R.id.llDay41,R.id.llDay42,R.id.llDay43,R.id.llDay44,R.id.llDay45,R.id.llDay46,
            R.id.llDay47,R.id.llDay48,R.id.llDay49};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_omer_chart);

        initToolBar();

        initViews();
        initView2();
        initiliseLongClicks();




    }
    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Blessings Log");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setSupportActionBartoolbar.setNavigationIcon(android.);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OmerChartActivity.this.finish();
                    }
                }

        );

    }
    public void weekOneClicked(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle();
    }

    public void weekTwoClicked(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle();
    }

    public void weekThreeClicked(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle();
    }

    public void weekFourClicked(View view) {
        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle();
    }

    public void weekFiveClicked(View view) {
        expandableLayout5 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout5);
        expandableLayout5.toggle();
    }

    public void weekSixClicked(View view) {
        expandableLayout6 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout6);
        expandableLayout6.toggle();
    }

    public void weekSevenClicked(View view) {
        expandableLayout7 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout7);
        expandableLayout7.toggle();
    }


    private void initiliseLongClicks(){

        for (int i = 0 ; i < ids.length; i++) {

            final int finalI = i+1;
            findViewById(ids[i]).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Realm realm = RealmController.with(OmerChartActivity.this).getRealm();
                    realm.beginTransaction();
                    RecordBlessing rb = new RecordBlessing();
                    rb.setId(finalI);
                    rb.setRecorded(true);
                    rb.setYear(Utilty.getYear(new Date()));
                    realm.copyToRealmOrUpdate(rb);
                    realm.commitTransaction();
                    Toast.makeText(OmerChartActivity.this, "Recorded", Toast.LENGTH_LONG).show();
                    initViews();
                    return true;
                }
            });
            findViewById(ids[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OmerChartActivity.this, HomeActivity.class);
                    intent.putExtra("DayNumber", finalI);
                    startActivity(intent);
                }
            });
        }
    }




    private void initViews(){
        RealmResults<RecordBlessing> recordedBlessings = RealmController.with(this).getBlessingsRecorded();

        for (int i = 0; i < recordedBlessings.size();i++){

            if (recordedBlessings.get(i).getId() == 1){
                findViewById(R.id.llDay1).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName1 = (TextView) findViewById(R.id.tvDayName1);
                TextView tvDayNum1 = (TextView) findViewById(R.id.tvDayNum1);
                tvDayName1.setTextColor(Color.parseColor("#000000"));
                tvDayNum1.setTextColor(Color.parseColor("#000000"));


            }

            if (recordedBlessings.get(i).getId() == 2){
                findViewById(R.id.llDay2).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName2 = (TextView) findViewById(R.id.tvDayName2);
                TextView tvDayNum2 = (TextView) findViewById(R.id.tvDayNum2);
                tvDayName2.setTextColor(Color.parseColor("#000000"));
                tvDayNum2.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 3){
                findViewById(R.id.llDay3).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName3 = (TextView) findViewById(R.id.tvDayName3);
                TextView tvDayNum3 = (TextView) findViewById(R.id.tvDayNum3);
                tvDayName3.setTextColor(Color.parseColor("#000000"));
                tvDayNum3.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 4){
                findViewById(R.id.llDay4).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName4 = (TextView) findViewById(R.id.tvDayName4);
                TextView tvDayNum4 = (TextView) findViewById(R.id.tvDayNum4);
                tvDayName4.setTextColor(Color.parseColor("#000000"));
                tvDayNum4.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 5){
                findViewById(R.id.llDay5).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName5 = (TextView) findViewById(R.id.tvDayName5);
                TextView tvDayNum5 = (TextView) findViewById(R.id.tvDayNum5);
                tvDayName5.setTextColor(Color.parseColor("#000000"));
                tvDayNum5.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 6){
                findViewById(R.id.llDay6).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName6 = (TextView) findViewById(R.id.tvDayName6);
                TextView tvDayNum6 = (TextView) findViewById(R.id.tvDayNum6);
                tvDayName6.setTextColor(Color.parseColor("#000000"));
                tvDayNum6.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 7){
                findViewById(R.id.llDay7).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName7 = (TextView) findViewById(R.id.tvDayName7);
                TextView tvDayNum7 = (TextView) findViewById(R.id.tvDayNum7);
                tvDayName7.setTextColor(Color.parseColor("#000000"));
                tvDayNum7.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 8){
                findViewById(R.id.llDay8).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName8 = (TextView) findViewById(R.id.tvDayName8);
                TextView tvDayNum8 = (TextView) findViewById(R.id.tvDayNum8);
                tvDayName8.setTextColor(Color.parseColor("#000000"));
                tvDayNum8.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 9){
                findViewById(R.id.llDay9).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName9 = (TextView) findViewById(R.id.tvDayName9);
                TextView tvDayNum9 = (TextView) findViewById(R.id.tvDayNum9);
                tvDayName9.setTextColor(Color.parseColor("#000000"));
                tvDayNum9.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 10){
                findViewById(R.id.llDay10).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName10 = (TextView) findViewById(R.id.tvDayName10);
                TextView tvDayNum10 = (TextView) findViewById(R.id.tvDayNum10);
                tvDayName10.setTextColor(Color.parseColor("#000000"));
                tvDayNum10.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 11){
                findViewById(R.id.llDay11).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName11 = (TextView) findViewById(R.id.tvDayName11);
                TextView tvDayNum11 = (TextView) findViewById(R.id.tvDayNum11);
                tvDayName11.setTextColor(Color.parseColor("#000000"));
                tvDayNum11.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 12){
                findViewById(R.id.llDay12).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName12 = (TextView) findViewById(R.id.tvDayName12);
                TextView tvDayNum12 = (TextView) findViewById(R.id.tvDayNum12);
                tvDayName12.setTextColor(Color.parseColor("#000000"));
                tvDayNum12.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 13){
                findViewById(R.id.llDay13).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName13 = (TextView) findViewById(R.id.tvDayName13);
                TextView tvDayNum13 = (TextView) findViewById(R.id.tvDayNum13);
                tvDayName13.setTextColor(Color.parseColor("#000000"));
                tvDayNum13.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 14){
                findViewById(R.id.llDay14).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName14 = (TextView) findViewById(R.id.tvDayName14);
                TextView tvDayNum14 = (TextView) findViewById(R.id.tvDayNum14);
                tvDayName14.setTextColor(Color.parseColor("#000000"));
                tvDayNum14.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 15){
                findViewById(R.id.llDay15).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName15 = (TextView) findViewById(R.id.tvDayName15);
                TextView tvDayNum15 = (TextView) findViewById(R.id.tvDayNum15);
                tvDayName15.setTextColor(Color.parseColor("#000000"));
                tvDayNum15.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 16){
                findViewById(R.id.llDay16).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName16 = (TextView) findViewById(R.id.tvDayName16);
                TextView tvDayNum16 = (TextView) findViewById(R.id.tvDayNum16);
                tvDayName16.setTextColor(Color.parseColor("#000000"));
                tvDayNum16.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 17){
                findViewById(R.id.llDay17).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName17 = (TextView) findViewById(R.id.tvDayName17);
                TextView tvDayNum17 = (TextView) findViewById(R.id.tvDayNum17);
                tvDayName17.setTextColor(Color.parseColor("#000000"));
                tvDayNum17.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 18){
                findViewById(R.id.llDay18).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName18 = (TextView) findViewById(R.id.tvDayName18);
                TextView tvDayNum18 = (TextView) findViewById(R.id.tvDayNum18);
                tvDayName18.setTextColor(Color.parseColor("#000000"));
                tvDayNum18.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 19){
                findViewById(R.id.llDay19).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName19 = (TextView) findViewById(R.id.tvDayName19);
                TextView tvDayNum19 = (TextView) findViewById(R.id.tvDayNum19);
                tvDayName19.setTextColor(Color.parseColor("#000000"));
                tvDayNum19.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 20){
                findViewById(R.id.llDay20).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName20 = (TextView) findViewById(R.id.tvDayName20);
                TextView tvDayNum20 = (TextView) findViewById(R.id.tvDayNum20);
                tvDayName20.setTextColor(Color.parseColor("#000000"));
                tvDayNum20.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 21){
                findViewById(R.id.llDay21).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName21 = (TextView) findViewById(R.id.tvDayName21);
                TextView tvDayNum21 = (TextView) findViewById(R.id.tvDayNum21);
                tvDayName21.setTextColor(Color.parseColor("#000000"));
                tvDayNum21.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 22){
                findViewById(R.id.llDay22).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName22 = (TextView) findViewById(R.id.tvDayName22);
                TextView tvDayNum22 = (TextView) findViewById(R.id.tvDayNum22);
                tvDayName22.setTextColor(Color.parseColor("#000000"));
                tvDayNum22.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 23){
                findViewById(R.id.llDay23).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName23 = (TextView) findViewById(R.id.tvDayName23);
                TextView tvDayNum23 = (TextView) findViewById(R.id.tvDayNum23);
                tvDayName23.setTextColor(Color.parseColor("#000000"));
                tvDayNum23.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 24){
                findViewById(R.id.llDay24).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName24 = (TextView) findViewById(R.id.tvDayName24);
                TextView tvDayNum24 = (TextView) findViewById(R.id.tvDayNum24);
                tvDayName24.setTextColor(Color.parseColor("#000000"));
                tvDayNum24.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 25){
                findViewById(R.id.llDay25).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName25 = (TextView) findViewById(R.id.tvDayName25);
                TextView tvDayNum25 = (TextView) findViewById(R.id.tvDayNum25);
                tvDayName25.setTextColor(Color.parseColor("#000000"));
                tvDayNum25.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 26){
                findViewById(R.id.llDay26).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName26 = (TextView) findViewById(R.id.tvDayName26);
                TextView tvDayNum26 = (TextView) findViewById(R.id.tvDayNum26);
                tvDayName26.setTextColor(Color.parseColor("#000000"));
                tvDayNum26.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 27){
                findViewById(R.id.llDay27).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName27 = (TextView) findViewById(R.id.tvDayName27);
                TextView tvDayNum27 = (TextView) findViewById(R.id.tvDayNum27);
                tvDayName27.setTextColor(Color.parseColor("#000000"));
                tvDayNum27.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 28){
                findViewById(R.id.llDay28).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName28 = (TextView) findViewById(R.id.tvDayName28);
                TextView tvDayNum28 = (TextView) findViewById(R.id.tvDayNum28);
                tvDayName28.setTextColor(Color.parseColor("#000000"));
                tvDayNum28.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 29){
                findViewById(R.id.llDay29).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName29 = (TextView) findViewById(R.id.tvDayName29);
                TextView tvDayNum29 = (TextView) findViewById(R.id.tvDayNum29);
                tvDayName29.setTextColor(Color.parseColor("#000000"));
                tvDayNum29.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 30){
                findViewById(R.id.llDay30).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName30 = (TextView) findViewById(R.id.tvDayName30);
                TextView tvDayNum30 = (TextView) findViewById(R.id.tvDayNum30);
                tvDayName30.setTextColor(Color.parseColor("#000000"));
                tvDayNum30.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 31){
                findViewById(R.id.llDay31).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName31 = (TextView) findViewById(R.id.tvDayName31);
                TextView tvDayNum31 = (TextView) findViewById(R.id.tvDayNum31);
                tvDayName31.setTextColor(Color.parseColor("#000000"));
                tvDayNum31.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 32){
                findViewById(R.id.llDay32).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName32 = (TextView) findViewById(R.id.tvDayName32);
                TextView tvDayNum32 = (TextView) findViewById(R.id.tvDayNum32);
                tvDayName32.setTextColor(Color.parseColor("#000000"));
                tvDayNum32.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 33){
                findViewById(R.id.llDay33).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName33 = (TextView) findViewById(R.id.tvDayName33);
                TextView tvDayNum33 = (TextView) findViewById(R.id.tvDayNum33);
                tvDayName33.setTextColor(Color.parseColor("#000000"));
                tvDayNum33.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 34){
                findViewById(R.id.llDay34).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName34 = (TextView) findViewById(R.id.tvDayName34);
                TextView tvDayNum34 = (TextView) findViewById(R.id.tvDayNum34);
                tvDayName34.setTextColor(Color.parseColor("#000000"));
                tvDayNum34.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 35){
                findViewById(R.id.llDay35).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName35 = (TextView) findViewById(R.id.tvDayName35);
                TextView tvDayNum35 = (TextView) findViewById(R.id.tvDayNum35);
                tvDayName35.setTextColor(Color.parseColor("#000000"));
                tvDayNum35.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 36){
                findViewById(R.id.llDay36).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName36 = (TextView) findViewById(R.id.tvDayName36);
                TextView tvDayNum36 = (TextView) findViewById(R.id.tvDayNum36);
                tvDayName36.setTextColor(Color.parseColor("#000000"));
                tvDayNum36.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 37){
                findViewById(R.id.llDay37).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName37 = (TextView) findViewById(R.id.tvDayName37);
                TextView tvDayNum37 = (TextView) findViewById(R.id.tvDayNum37);
                tvDayName37.setTextColor(Color.parseColor("#000000"));
                tvDayNum37.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 38){
                findViewById(R.id.llDay38).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName38 = (TextView) findViewById(R.id.tvDayName38);
                TextView tvDayNum38 = (TextView) findViewById(R.id.tvDayNum38);
                tvDayName38.setTextColor(Color.parseColor("#000000"));
                tvDayNum38.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 39){
                findViewById(R.id.llDay39).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName39 = (TextView) findViewById(R.id.tvDayName39);
                TextView tvDayNum39 = (TextView) findViewById(R.id.tvDayNum39);
                tvDayName39.setTextColor(Color.parseColor("#000000"));
                tvDayNum39.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 40){
                findViewById(R.id.llDay40).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName40 = (TextView) findViewById(R.id.tvDayName40);
                TextView tvDayNum40 = (TextView) findViewById(R.id.tvDayNum40);
                tvDayName40.setTextColor(Color.parseColor("#000000"));
                tvDayNum40.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 41){
                findViewById(R.id.llDay41).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName41 = (TextView) findViewById(R.id.tvDayName41);
                TextView tvDayNum41 = (TextView) findViewById(R.id.tvDayNum41);
                tvDayName41.setTextColor(Color.parseColor("#000000"));
                tvDayNum41.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 42){
                findViewById(R.id.llDay42).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName42 = (TextView) findViewById(R.id.tvDayName42);
                TextView tvDayNum42 = (TextView) findViewById(R.id.tvDayNum42);
                tvDayName42.setTextColor(Color.parseColor("#000000"));
                tvDayNum42.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 43){
                findViewById(R.id.llDay43).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName43 = (TextView) findViewById(R.id.tvDayName43);
                TextView tvDayNum43 = (TextView) findViewById(R.id.tvDayNum43);
                tvDayName43.setTextColor(Color.parseColor("#000000"));
                tvDayNum43.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 44){
                findViewById(R.id.llDay44).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName44 = (TextView) findViewById(R.id.tvDayName44);
                TextView tvDayNum44 = (TextView) findViewById(R.id.tvDayNum44);
                tvDayName44.setTextColor(Color.parseColor("#000000"));
                tvDayNum44.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 45){
                findViewById(R.id.llDay45).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName45 = (TextView) findViewById(R.id.tvDayName45);
                TextView tvDayNum45 = (TextView) findViewById(R.id.tvDayNum45);
                tvDayName45.setTextColor(Color.parseColor("#000000"));
                tvDayNum45.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 46){
                findViewById(R.id.llDay46).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName46 = (TextView) findViewById(R.id.tvDayName46);
                TextView tvDayNum46 = (TextView) findViewById(R.id.tvDayNum46);
                tvDayName46.setTextColor(Color.parseColor("#000000"));
                tvDayNum46.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 47){
                findViewById(R.id.llDay47).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName47 = (TextView) findViewById(R.id.tvDayName47);
                TextView tvDayNum47 = (TextView) findViewById(R.id.tvDayNum47);
                tvDayName47.setTextColor(Color.parseColor("#000000"));
                tvDayNum47.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 48){
                findViewById(R.id.llDay48).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName48 = (TextView) findViewById(R.id.tvDayName48);
                TextView tvDayNum48 = (TextView) findViewById(R.id.tvDayNum48);
                tvDayName48.setTextColor(Color.parseColor("#000000"));
                tvDayNum48.setTextColor(Color.parseColor("#000000"));
            }
            if (recordedBlessings.get(i).getId() == 49){
                findViewById(R.id.llDay49).setBackgroundColor(Color.parseColor("#ffffff"));
                TextView tvDayName49 = (TextView) findViewById(R.id.tvDayName49);
                TextView tvDayNum49 = (TextView) findViewById(R.id.tvDayNum49);
                tvDayName49.setTextColor(Color.parseColor("#000000"));
                tvDayNum49.setTextColor(Color.parseColor("#000000"));
            }

        }
    }


    private void initView2(){
        int currentYear = Utilty.getYear(new Date());
        MyOmerPeriod myOmerPeriod = RealmController.with(this).getPeriodByYear(currentYear);
        Date startDateOfOmer = myOmerPeriod.getStartDate();
        Date currentDate = new Date();
        int fidd = Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays();
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 1){
            findViewById(R.id.llDay1).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName1 = (TextView) findViewById(R.id.tvDayName1);
            TextView tvDayNum1 = (TextView) findViewById(R.id.tvDayNum1);
            tvDayName1.setTextColor(Color.parseColor("#000000"));
            tvDayNum1.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay1).setEnabled(false);
        }

        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 2){
            findViewById(R.id.llDay2).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName2 = (TextView) findViewById(R.id.tvDayName2);
            TextView tvDayNum2 = (TextView) findViewById(R.id.tvDayNum2);
            tvDayName2.setTextColor(Color.parseColor("#000000"));
            tvDayNum2.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay2).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 3){
            findViewById(R.id.llDay3).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName3 = (TextView) findViewById(R.id.tvDayName3);
            TextView tvDayNum3 = (TextView) findViewById(R.id.tvDayNum3);
            tvDayName3.setTextColor(Color.parseColor("#000000"));
            tvDayNum3.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay3).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 4){
            findViewById(R.id.llDay4).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName4 = (TextView) findViewById(R.id.tvDayName4);
            TextView tvDayNum4 = (TextView) findViewById(R.id.tvDayNum4);
            tvDayName4.setTextColor(Color.parseColor("#000000"));
            tvDayNum4.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay4).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 5){
            findViewById(R.id.llDay5).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName5 = (TextView) findViewById(R.id.tvDayName5);
            TextView tvDayNum5 = (TextView) findViewById(R.id.tvDayNum5);
            tvDayName5.setTextColor(Color.parseColor("#000000"));
            tvDayNum5.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay5).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 6){
            findViewById(R.id.llDay6).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName6 = (TextView) findViewById(R.id.tvDayName6);
            TextView tvDayNum6 = (TextView) findViewById(R.id.tvDayNum6);
            tvDayName6.setTextColor(Color.parseColor("#000000"));
            tvDayNum6.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay6).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 7){
            findViewById(R.id.llDay7).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName7 = (TextView) findViewById(R.id.tvDayName7);
            TextView tvDayNum7 = (TextView) findViewById(R.id.tvDayNum7);
            tvDayName7.setTextColor(Color.parseColor("#000000"));
            tvDayNum7.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay7).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 8){
            findViewById(R.id.llDay8).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName8 = (TextView) findViewById(R.id.tvDayName8);
            TextView tvDayNum8 = (TextView) findViewById(R.id.tvDayNum8);
            tvDayName8.setTextColor(Color.parseColor("#000000"));
            tvDayNum8.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay8).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 9){
            findViewById(R.id.llDay9).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName9 = (TextView) findViewById(R.id.tvDayName9);
            TextView tvDayNum9 = (TextView) findViewById(R.id.tvDayNum9);
            tvDayName9.setTextColor(Color.parseColor("#000000"));
            tvDayNum9.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay9).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 10){
            findViewById(R.id.llDay10).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName10 = (TextView) findViewById(R.id.tvDayName10);
            TextView tvDayNum10 = (TextView) findViewById(R.id.tvDayNum10);
            tvDayName10.setTextColor(Color.parseColor("#000000"));
            tvDayNum10.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay10).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 11){
            findViewById(R.id.llDay11).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName11 = (TextView) findViewById(R.id.tvDayName11);
            TextView tvDayNum11 = (TextView) findViewById(R.id.tvDayNum11);
            tvDayName11.setTextColor(Color.parseColor("#000000"));
            tvDayNum11.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay11).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 12){
            findViewById(R.id.llDay12).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName12 = (TextView) findViewById(R.id.tvDayName12);
            TextView tvDayNum12 = (TextView) findViewById(R.id.tvDayNum12);
            tvDayName12.setTextColor(Color.parseColor("#000000"));
            tvDayNum12.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay12).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 13){
            findViewById(R.id.llDay13).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName13 = (TextView) findViewById(R.id.tvDayName13);
            TextView tvDayNum13 = (TextView) findViewById(R.id.tvDayNum13);
            tvDayName13.setTextColor(Color.parseColor("#000000"));
            tvDayNum13.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay13).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 14){
            findViewById(R.id.llDay14).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName14 = (TextView) findViewById(R.id.tvDayName14);
            TextView tvDayNum14 = (TextView) findViewById(R.id.tvDayNum14);
            tvDayName14.setTextColor(Color.parseColor("#000000"));
            tvDayNum14.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay14).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 15){
            findViewById(R.id.llDay15).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName15 = (TextView) findViewById(R.id.tvDayName15);
            TextView tvDayNum15 = (TextView) findViewById(R.id.tvDayNum15);
            tvDayName15.setTextColor(Color.parseColor("#000000"));
            tvDayNum15.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay15).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 16){
            findViewById(R.id.llDay16).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName16 = (TextView) findViewById(R.id.tvDayName16);
            TextView tvDayNum16 = (TextView) findViewById(R.id.tvDayNum16);
            tvDayName16.setTextColor(Color.parseColor("#000000"));
            tvDayNum16.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay16).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 17){
            findViewById(R.id.llDay17).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName17 = (TextView) findViewById(R.id.tvDayName17);
            TextView tvDayNum17 = (TextView) findViewById(R.id.tvDayNum17);
            tvDayName17.setTextColor(Color.parseColor("#000000"));
            tvDayNum17.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay17).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 18){
            findViewById(R.id.llDay18).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName18 = (TextView) findViewById(R.id.tvDayName18);
            TextView tvDayNum18 = (TextView) findViewById(R.id.tvDayNum18);
            tvDayName18.setTextColor(Color.parseColor("#000000"));
            tvDayNum18.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay18).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 19){
            findViewById(R.id.llDay19).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName19 = (TextView) findViewById(R.id.tvDayName19);
            TextView tvDayNum19 = (TextView) findViewById(R.id.tvDayNum19);
            tvDayName19.setTextColor(Color.parseColor("#000000"));
            tvDayNum19.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay19).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 20){
            findViewById(R.id.llDay20).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName20 = (TextView) findViewById(R.id.tvDayName20);
            TextView tvDayNum20 = (TextView) findViewById(R.id.tvDayNum20);
            tvDayName20.setTextColor(Color.parseColor("#000000"));
            tvDayNum20.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay20).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 21){
            findViewById(R.id.llDay21).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName21 = (TextView) findViewById(R.id.tvDayName21);
            TextView tvDayNum21 = (TextView) findViewById(R.id.tvDayNum21);
            tvDayName21.setTextColor(Color.parseColor("#000000"));
            tvDayNum21.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay21).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 22){
            findViewById(R.id.llDay22).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName22 = (TextView) findViewById(R.id.tvDayName22);
            TextView tvDayNum22 = (TextView) findViewById(R.id.tvDayNum22);
            tvDayName22.setTextColor(Color.parseColor("#000000"));
            tvDayNum22.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay22).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 23){
            findViewById(R.id.llDay23).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName23 = (TextView) findViewById(R.id.tvDayName23);
            TextView tvDayNum23 = (TextView) findViewById(R.id.tvDayNum23);
            tvDayName23.setTextColor(Color.parseColor("#000000"));
            tvDayNum23.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay23).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 24){
            findViewById(R.id.llDay24).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName24 = (TextView) findViewById(R.id.tvDayName24);
            TextView tvDayNum24 = (TextView) findViewById(R.id.tvDayNum24);
            tvDayName24.setTextColor(Color.parseColor("#000000"));
            tvDayNum24.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay24).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 25){
            findViewById(R.id.llDay25).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName25 = (TextView) findViewById(R.id.tvDayName25);
            TextView tvDayNum25 = (TextView) findViewById(R.id.tvDayNum25);
            tvDayName25.setTextColor(Color.parseColor("#000000"));
            tvDayNum25.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay25).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 26){
            findViewById(R.id.llDay26).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName26 = (TextView) findViewById(R.id.tvDayName26);
            TextView tvDayNum26 = (TextView) findViewById(R.id.tvDayNum26);
            tvDayName26.setTextColor(Color.parseColor("#000000"));
            tvDayNum26.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay26).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 27){
            findViewById(R.id.llDay27).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName27 = (TextView) findViewById(R.id.tvDayName27);
            TextView tvDayNum27 = (TextView) findViewById(R.id.tvDayNum27);
            tvDayName27.setTextColor(Color.parseColor("#000000"));
            tvDayNum27.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay27).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 28){
            findViewById(R.id.llDay28).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName28 = (TextView) findViewById(R.id.tvDayName28);
            TextView tvDayNum28 = (TextView) findViewById(R.id.tvDayNum28);
            tvDayName28.setTextColor(Color.parseColor("#000000"));
            tvDayNum28.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay28).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 29){
            findViewById(R.id.llDay29).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName29 = (TextView) findViewById(R.id.tvDayName29);
            TextView tvDayNum29 = (TextView) findViewById(R.id.tvDayNum29);
            tvDayName29.setTextColor(Color.parseColor("#000000"));
            tvDayNum29.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay29).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 30){
            findViewById(R.id.llDay30).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName30 = (TextView) findViewById(R.id.tvDayName30);
            TextView tvDayNum30 = (TextView) findViewById(R.id.tvDayNum30);
            tvDayName30.setTextColor(Color.parseColor("#000000"));
            tvDayNum30.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay30).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 31){
            findViewById(R.id.llDay31).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName31 = (TextView) findViewById(R.id.tvDayName31);
            TextView tvDayNum31 = (TextView) findViewById(R.id.tvDayNum31);
            tvDayName31.setTextColor(Color.parseColor("#000000"));
            tvDayNum31.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay31).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 32){
            findViewById(R.id.llDay32).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName32 = (TextView) findViewById(R.id.tvDayName32);
            TextView tvDayNum32 = (TextView) findViewById(R.id.tvDayNum32);
            tvDayName32.setTextColor(Color.parseColor("#000000"));
            tvDayNum32.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay32).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 33){
            findViewById(R.id.llDay33).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName33 = (TextView) findViewById(R.id.tvDayName33);
            TextView tvDayNum33 = (TextView) findViewById(R.id.tvDayNum33);
            tvDayName33.setTextColor(Color.parseColor("#000000"));
            tvDayNum33.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay33).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 34){
            findViewById(R.id.llDay34).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName34 = (TextView) findViewById(R.id.tvDayName34);
            TextView tvDayNum34 = (TextView) findViewById(R.id.tvDayNum34);
            tvDayName34.setTextColor(Color.parseColor("#000000"));
            tvDayNum34.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay34).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 35){
            findViewById(R.id.llDay35).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName35 = (TextView) findViewById(R.id.tvDayName35);
            TextView tvDayNum35 = (TextView) findViewById(R.id.tvDayNum35);
            tvDayName35.setTextColor(Color.parseColor("#000000"));
            tvDayNum35.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay35).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 36){
            findViewById(R.id.llDay36).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName36 = (TextView) findViewById(R.id.tvDayName36);
            TextView tvDayNum36 = (TextView) findViewById(R.id.tvDayNum36);
            tvDayName36.setTextColor(Color.parseColor("#000000"));
            tvDayNum36.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay36).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 37){
            findViewById(R.id.llDay37).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName37 = (TextView) findViewById(R.id.tvDayName37);
            TextView tvDayNum37 = (TextView) findViewById(R.id.tvDayNum37);
            tvDayName37.setTextColor(Color.parseColor("#000000"));
            tvDayNum37.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay37).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 38){
            findViewById(R.id.llDay38).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName38 = (TextView) findViewById(R.id.tvDayName38);
            TextView tvDayNum38 = (TextView) findViewById(R.id.tvDayNum38);
            tvDayName38.setTextColor(Color.parseColor("#000000"));
            tvDayNum38.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay38).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 39){
            findViewById(R.id.llDay39).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName39 = (TextView) findViewById(R.id.tvDayName39);
            TextView tvDayNum39 = (TextView) findViewById(R.id.tvDayNum39);
            tvDayName39.setTextColor(Color.parseColor("#000000"));
            tvDayNum39.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay39).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 40){
            findViewById(R.id.llDay40).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName40 = (TextView) findViewById(R.id.tvDayName40);
            TextView tvDayNum40 = (TextView) findViewById(R.id.tvDayNum40);
            tvDayName40.setTextColor(Color.parseColor("#000000"));
            tvDayNum40.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay40).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() <41){
            findViewById(R.id.llDay41).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName41 = (TextView) findViewById(R.id.tvDayName41);
            TextView tvDayNum41 = (TextView) findViewById(R.id.tvDayNum41);
            tvDayName41.setTextColor(Color.parseColor("#000000"));
            tvDayNum41.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay41).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 42){
            findViewById(R.id.llDay42).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName42 = (TextView) findViewById(R.id.tvDayName42);
            TextView tvDayNum42 = (TextView) findViewById(R.id.tvDayNum42);
            tvDayName42.setTextColor(Color.parseColor("#000000"));
            tvDayNum42.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay42).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 43){
            findViewById(R.id.llDay43).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName43 = (TextView) findViewById(R.id.tvDayName43);
            TextView tvDayNum43 = (TextView) findViewById(R.id.tvDayNum43);
            tvDayName43.setTextColor(Color.parseColor("#000000"));
            tvDayNum43.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay43).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 44){
            findViewById(R.id.llDay44).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName44 = (TextView) findViewById(R.id.tvDayName44);
            TextView tvDayNum44 = (TextView) findViewById(R.id.tvDayNum44);
            tvDayName44.setTextColor(Color.parseColor("#000000"));
            tvDayNum44.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay44).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 45){
            findViewById(R.id.llDay45).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName45 = (TextView) findViewById(R.id.tvDayName45);
            TextView tvDayNum45 = (TextView) findViewById(R.id.tvDayNum45);
            tvDayName45.setTextColor(Color.parseColor("#000000"));
            tvDayNum45.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay45).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 46){
            findViewById(R.id.llDay46).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName46 = (TextView) findViewById(R.id.tvDayName46);
            TextView tvDayNum46 = (TextView) findViewById(R.id.tvDayNum46);
            tvDayName46.setTextColor(Color.parseColor("#000000"));
            tvDayNum46.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay46).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 47){
            findViewById(R.id.llDay47).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName47 = (TextView) findViewById(R.id.tvDayName47);
            TextView tvDayNum47 = (TextView) findViewById(R.id.tvDayNum47);
            tvDayName47.setTextColor(Color.parseColor("#000000"));
            tvDayNum47.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay47).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 48){
            findViewById(R.id.llDay48).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName48 = (TextView) findViewById(R.id.tvDayName48);
            TextView tvDayNum48 = (TextView) findViewById(R.id.tvDayNum48);
            tvDayName48.setTextColor(Color.parseColor("#000000"));
            tvDayNum48.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay48).setEnabled(false);
        }
        if (Days.daysBetween(new LocalDate(startDateOfOmer),new LocalDate(currentDate)).getDays() < 49){
            findViewById(R.id.llDay49).setBackgroundColor(Color.parseColor("#bebebe"));
            TextView tvDayName49 = (TextView) findViewById(R.id.tvDayName49);
            TextView tvDayNum49 = (TextView) findViewById(R.id.tvDayNum49);
            tvDayName49.setTextColor(Color.parseColor("#000000"));
            tvDayNum49.setTextColor(Color.parseColor("#000000"));
            findViewById(R.id.llDay49).setEnabled(false);
        }
    }

}
