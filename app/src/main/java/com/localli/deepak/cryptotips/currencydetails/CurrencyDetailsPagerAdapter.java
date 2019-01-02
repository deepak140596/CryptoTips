package com.localli.deepak.cryptotips.currencydetails;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.localli.deepak.cryptotips.currencydetails.chartandtable.GraphFragment;
import com.localli.deepak.cryptotips.currencydetails.marketdetails.MarketDetailsFragment;
import com.localli.deepak.cryptotips.models.CoinItem;

public class CurrencyDetailsPagerAdapter extends FragmentPagerAdapter {

    CoinItem coinItem;

    public CurrencyDetailsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public CurrencyDetailsPagerAdapter(FragmentManager fm, CoinItem coinItem){
        super(fm);
        this.coinItem= coinItem;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return GraphFragment.newInstance(coinItem);

            case 1:
                return MarketDetailsFragment.newInstance(coinItem);

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Chart";
            case 1:
                return "Details";
            default:
                return "Chart";
        }
    }
}
