package com.localli.deepak.cryptotips.currencydetails;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;

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
