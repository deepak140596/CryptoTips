package com.localli.deepak.cryptotips.currencydetails;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.currencydetails.chartandtable.GraphFragment;
import com.localli.deepak.cryptotips.models.CoinItem;

public class CurrencyDetailsTabsActivity extends AppCompatActivity {

    String TAG = "CURRENCY_DETAILS";
    CurrencyDetailsPagerAdapter currencyDetailsPagerAdapter;
    ViewPager viewPager;
    Toolbar toolbar;

    CoinItem coinItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_details_tabs);

        Intent intent = getIntent();
        coinItem = (CoinItem)intent.getSerializableExtra(GraphFragment.CURRENCY_ID);
        Log.d(TAG,"Currency ID: "+coinItem.getId());

        toolbar = findViewById(R.id.activity_currency_details_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(coinItem.getName()+" ("+coinItem.getSymbol().toUpperCase()+") ");

        viewPager = findViewById(R.id.activity_currency_details_viewpager);
        currencyDetailsPagerAdapter = new CurrencyDetailsPagerAdapter(getSupportFragmentManager(), coinItem);

        viewPager.setAdapter(currencyDetailsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.activity_currency_details_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:

                break;
            default:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
