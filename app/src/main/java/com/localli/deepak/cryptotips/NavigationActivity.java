package com.localli.deepak.cryptotips;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.localli.deepak.cryptotips.alerts.AlertBackgroundService;
import com.localli.deepak.cryptotips.alerts.AlertFragment;
import com.localli.deepak.cryptotips.coinlists.ListFragment;
import com.localli.deepak.cryptotips.news.NewsListFragment;
import com.localli.deepak.cryptotips.portfolio.PortfolioFragment;

public class NavigationActivity extends AppCompatActivity {

    public static String CHANNEL_ID = "default";
    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigation;
    public static CoordinatorLayout coordinatorLayout;
    LinearLayout noInternetState;
    Button retryBtn;

    boolean doubleBackToExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);



        // as soon as the app starts, create a notification channel
        createNotificationChannel();
        // OneSignal Initialization
        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/
        noInternetState = findViewById(R.id.navigation_no_internet_state_ll);
        retryBtn = findViewById(R.id.nav_retry_ll);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUI();
            }
        });
        startUI();


    }

    private void startUI(){
        if(isNetworkConnected()) {

            noInternetState.setVisibility(View.GONE);
            bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
            bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            coordinatorLayout = findViewById(R.id.navigation_coordinator_layout);

            startBackgroundAlert();
            startCoinListFragment();
        }else {
            noInternetState.setVisibility(View.VISIBLE);
            return;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()) {

                case R.id.bottom_navigation_home:
                    fragment = new ListFragment();
                    break;

                case R.id.bottom_navigation_portfolio:
                    fragment = new PortfolioFragment();
                    break;
                case R.id.bottom_navigation_news:
                    fragment = new NewsListFragment();
                    break;

                case R.id.bottom_navigation_alert:
                    fragment = new AlertFragment();
                    break;

                case R.id.bottom_navigation_about:
                    fragment = new AboutUsFragment();
                    break;

                default:
                    fragment = new ListFragment();
                    break;


            }
            if(fragment!=null){
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment,"").commit();
                return true;
            }
            return false;
        }
    };

    public void startCoinListFragment(){
        fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame,new AllCoinListFragment(),"").commit();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new ListFragment(),"").commit();

    }


    public void setBottomNavigationViewVisibility(boolean visible){
        if(bottomNavigation.isShown() && !visible){
            bottomNavigation.setVisibility(View.GONE);
        }else if(!bottomNavigation.isShown() && visible){
            bottomNavigation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {


        if(doubleBackToExit){
            super.onBackPressed();
        }else{
            if(bottomNavigation.getVisibility()==View.GONE)
                bottomNavigation.setVisibility(View.VISIBLE);
            this.doubleBackToExit = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExit=false;
                }
            }, 2000);
        }

    }

    public void startBackgroundAlert(){
        Intent i = new Intent(this, AlertBackgroundService.class);
        startService(i);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
