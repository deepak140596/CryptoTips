package com.localli.deepak.cryptotips.currencydetails;

import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.currencydetails.chartandtable.GraphFragment;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.localli.deepak.cryptotips.rest.VolleyService;
import com.localli.deepak.cryptotips.utils.VolleyResult;

import java.util.Arrays;
import java.util.List;

public class CurrencyDetailsTabsActivity extends AppCompatActivity {

    String TAG = "CURRENCY_DETAILS";
    CurrencyDetailsPagerAdapter currencyDetailsPagerAdapter;
    ViewPager viewPager;
    Toolbar toolbar;

    CoinItem coinItem;
    String coinID;
    String vsCurrency;
    String COIN_DETAILS_URL;

    VolleyResult volleyResultCallback = null;
    VolleyService volleyService;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_details_tabs);
        setupTheme();

        toolbar = findViewById(R.id.activity_currency_details_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();


        Intent intent = getIntent();
        coinID = intent.getStringExtra(getString(R.string.coin_id));
        vsCurrency = intent.getStringExtra(getString(R.string.vs_currency));

        COIN_DETAILS_URL = String.format(CoinGeckoService.COINS_BY_ID_URL, vsCurrency,coinID);

        if(coinID != null && vsCurrency != null) {
            Log.i(TAG, "COIN ID: " + coinID);
            setupVolley();
        }
        else {

            coinItem = (CoinItem) intent.getSerializableExtra(GraphFragment.CURRENCY_ID);
            //Log.d(TAG, "Currency ID: " + coinItem.getId());
            setupViews(coinItem);
        }





    }

    public void setupVolley(){
        initVolleyCallback();
        volleyService = new VolleyService(volleyResultCallback,this);
        volleyService.getDataVolley("GET",COIN_DETAILS_URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.i(TAG,"Home  Clicked.");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    void initVolleyCallback(){
        volleyResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response) {

                List<CoinItem> coinList = Arrays.asList(gson.fromJson(response,CoinItem[].class));
                coinItem = coinList.get(0);

                setupViews(coinItem);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }

            @Override
            public void notifySuccess(String requestType, String response, AlertEntity alertEntity) {

            }
        };
    }

    void setupViews(CoinItem coinItem){
        getSupportActionBar().setTitle(coinItem.getName()+" ("+coinItem.getSymbol().toUpperCase()+") ");
        viewPager = findViewById(R.id.activity_currency_details_viewpager);
        Log.i(TAG,"Currency ID: "+coinItem.getId());


        currencyDetailsPagerAdapter = new CurrencyDetailsPagerAdapter(getSupportFragmentManager(), coinItem);

        viewPager.setAdapter(currencyDetailsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.activity_currency_details_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    public void setupTheme(){
        boolean isNightModeEnabled = SharedPrefSimpleDB.getIsNightModeEnabled(this);
        if(isNightModeEnabled){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
