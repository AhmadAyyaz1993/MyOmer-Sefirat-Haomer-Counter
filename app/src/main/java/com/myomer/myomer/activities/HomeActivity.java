package com.myomer.myomer.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myomer.myomer.R;
import com.myomer.myomer.event_bus.Events;
import com.myomer.myomer.event_bus.GlobalBus;
import com.myomer.myomer.fragments.BlessingsFragment;
import com.myomer.myomer.fragments.DailyFragment;
import com.myomer.myomer.fragments.ExerciseFragment;
import com.myomer.myomer.fragments.JournalsFragment;
import com.myomer.myomer.fragments.VideoFragment;
import com.myomer.myomer.fragments.WeekFragment;
import com.myomer.myomer.plist_parser.PListDict;
import com.myomer.myomer.plist_parser.PListException;
import com.myomer.myomer.plist_parser.PListParser;
import com.myomer.myomer.utilty.Constants;
import com.myomer.myomer.utilty.Utilty;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import hollowsoft.slidingdrawer.OnDrawerCloseListener;
import hollowsoft.slidingdrawer.OnDrawerOpenListener;
import hollowsoft.slidingdrawer.OnDrawerScrollListener;
import hollowsoft.slidingdrawer.SlidingDrawer;

public class HomeActivity extends AppCompatActivity implements DailyFragment.OnFragmentInteractionListener,
        WeekFragment.OnFragmentInteractionListener,
        VideoFragment.OnFragmentInteractionListener,
        ExerciseFragment.OnFragmentInteractionListener,
        JournalsFragment.OnFragmentInteractionListener,
        BlessingsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    TextView tvTopHeading, tvQuote,tvTitleOne,tvTitleTwo,tvContent;

    RadioRealButtonGroup buttonGroup;
    ImageView ivNext,ivPrevious,ivMenu;
    int count = 1;
    int weekCount = 1;
    String videoLink;
    int buttonClicked = 1;
    LinearLayout llParentPanel;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Utilty.replaceFragment(HomeActivity.this,new DailyFragment(),R.id.frame_container);
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    return true;
                case R.id.navigation_journal:
                    Utilty.replaceFragment(HomeActivity.this,new JournalsFragment(),R.id.frame_container);
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    return true;
                case R.id.navigation_exercise:
                    Utilty.replaceFragment(HomeActivity.this,new ExerciseFragment(),R.id.frame_container);
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    return true;
                case R.id.navigation_video:
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink)));
                    Utilty.replaceFragment(HomeActivity.this,new VideoFragment(),R.id.frame_container);
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        initViews();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Utilty.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Utilty.replaceFragment(this,new DailyFragment(),R.id.frame_container);
        populateViews(count);



        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                ivPrevious.setVisibility(View.VISIBLE);
                if (buttonClicked == 1 && count == Constants.MY_OMER_DAYS_COUNT){
                    ivNext.setVisibility(View.GONE);
                }
                if (buttonClicked == 2 && count == Constants.MY_OMER_WEEKS_COUNT){
                    ivNext.setVisibility(View.GONE);
                }
                populateViews(count);
            }
        });
        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
                GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                ivNext.setVisibility(View.VISIBLE);
                if (count == 1){
                    ivPrevious.setVisibility(View.GONE);
                }
                populateViews(count);
            }
        });
        buttonGroup.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0){
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    Utilty.replaceFragment(HomeActivity.this,new DailyFragment(),R.id.frame_container);
                }else if (position == 1){
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(weekCount));
                    Utilty.replaceFragment(HomeActivity.this,new WeekFragment(),R.id.frame_container);
                }else if(position == 2){
                    GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));
                    Utilty.replaceFragment(HomeActivity.this,new BlessingsFragment(),R.id.frame_container);
                }
            }
        });

        GlobalBus.getBus().postSticky(new Events.DayChangeEvent(count));

    }


    private void initViews(){
        tvTopHeading = (TextView) findViewById(R.id.tvTopHeading);
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvTitleOne = (TextView) findViewById(R.id.tvTitleOne);
        tvTitleTwo = (TextView) findViewById(R.id.tvTitleTwo);
        tvContent =(TextView) findViewById(R.id.tvContent);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrevious =(ImageView) findViewById(R.id.ivPrevious);
        buttonGroup = (RadioRealButtonGroup) findViewById(R.id.buttonGroup);
        llParentPanel = (LinearLayout) findViewById(R.id.parentPanel);
        ivMenu= (ImageView) findViewById(R.id.ivMenu);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }


    private void populateViews(int dayCount){
        AssetManager assetManager = getAssets();
        try {
            InputStream stream = null;

            stream = assetManager.open("days/day" + dayCount + ".plist");

            PListDict dict = PListParser.parse(stream);

            int day = dict.getInt("day");
            int week = dict.getInt("week");
            int dayOfWeek = dict.getInt("dayOfWeek");
            weekCount = week;
            if (week == 1){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }else if (week == 2){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));
                
            }else if (week == 3){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }else if (week == 4){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }else if (week == 5){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }else if (week == 6){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }else if (week == 7){
                InputStream is = null;

                is = assetManager.open("weeks/week" + week + ".plist");

                PListDict dict2 = PListParser.parse(is);

                String color = dict2.getString("color");
                String title = dict2.getString("title");
                String subTitle = dict2.getString("subtitle");
                tvTopHeading.setText(title);
                tvQuote.setText(subTitle);
                llParentPanel.setBackgroundColor(Color.parseColor("#"+color));

            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (PListException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register this fragment to listen to event.

    }


    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.omer_chart) {
            startActivity(new Intent(HomeActivity.this,OmerChartActivity.class));
        } else if (id == R.id.settings) {

        } else if (id == R.id.goToBook) {

        } else if (id == R.id.donate) {

        } else if (id == R.id.techSupport) {

        } else if (id == R.id.whatIsOmer) {

        }else if (id == R.id.nav_share) {

        }
        return true;
    }
}
