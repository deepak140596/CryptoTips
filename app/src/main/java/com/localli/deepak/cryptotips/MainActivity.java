package com.localli.deepak.cryptotips;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.localli.deepak.cryptotips.Fragments.fragAlerts;
import com.localli.deepak.cryptotips.Fragments.fragApiNews;
import com.localli.deepak.cryptotips.Fragments.fragCoinsTracker;
import com.localli.deepak.cryptotips.Fragments.fragNews;
import com.localli.deepak.cryptotips.Fragments.fragTips;
import com.onesignal.OneSignal;

import java.util.ArrayList;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    String PREFERRED_CURRENCY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, getString(R.string.admob_app_id));



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //initialise One signal
        OneSignal.startInit(getApplicationContext())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        displaySelectedScreen(R.id.nav_news);
        RateUs.app_launched(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_select_currency){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Select Currency: ");
            final String[] types = {"USD","CAD","GBP","EUR","CHF","SEK","JPY","CNY","INR","RUB","AUD",
                              "HKD","SGD","TWD","BRL","KRW","ZAR","MYR","IDR","NZD","MXN","PHP",
                              "DKK","PLN","XAU"};
            b.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    PREFERRED_CURRENCY=types[which];
                    Log.e("Preff curr main: ",PREFERRED_CURRENCY);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getString(R.string.pref_preferred_currency),PREFERRED_CURRENCY);
                    editor.apply();
                }

            });

            b.show();



        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
    public void displaySelectedScreen(int itemId){
        // Handle navigation view item clicks here.

        Fragment fragment=null;
        switch (itemId){
            case R.id.nav_news:
                //fragment = new fragNews();
                fragment=new fragApiNews();
                break;
            case R.id.nav_tips:
                fragment=new fragTips();
                break;
            case R.id.nav_alerts:
                fragment=new fragAlerts();
                break;
            case R.id.nav_coins_tracker:
                fragment=new fragCoinsTracker();
                break;
            case R.id.nav_rate_us:
                SharedPreferences prf=this.getSharedPreferences("rateus",0);
                SharedPreferences.Editor editor=prf.edit();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                startActivity(rateIntent);
                break;
            case R.id.nav_share:

                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Have a look at this app.");
                i.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.string_share_text)+ getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(i,"Share via"));
                break;
            case R.id.nav_about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            default:
                //fragment=new fragNews();
                fragment=new fragApiNews();

        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
