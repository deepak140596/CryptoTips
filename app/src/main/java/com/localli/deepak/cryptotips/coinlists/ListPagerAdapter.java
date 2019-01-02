package com.localli.deepak.cryptotips.coinlists;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.localli.deepak.cryptotips.coinlists.favorites.FavoriteListFragment;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */

public class ListPagerAdapter extends FragmentPagerAdapter {

    public ListPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllCoinListFragment();
            case 1:
                return new FavoriteListFragment();
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
                return "All";
            case 1:
                return "Favorites";
            default:
                return "All";
        }
    }
}
