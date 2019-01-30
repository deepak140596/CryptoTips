package com.localli.deepak.cryptotips;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.localli.deepak.cryptotips.R;
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

    boolean doubleBackToExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // as soon as the app starts, create a notification channel
        createNotificationChannel();

        bottomNavigation= (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        coordinatorLayout = findViewById(R.id.navigation_coordinator_layout);

        startBackgroundAlert();
        startCoinListFragment();


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
}
