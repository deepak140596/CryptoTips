package com.localli.deepak.cryptotips.coinlists;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localli.deepak.cryptotips.R;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */

public class ListFragment extends Fragment{

    String TAG = "CURRENCY_DETAILS";
    ListPagerAdapter pagerAdapter;
    ViewPager viewPager;

    AppCompatActivity activity;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.activity = (AppCompatActivity)getActivity();
        this.context = getContext();
        return inflater.inflate(R.layout.fragment_list,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_list);
        activity.setSupportActionBar(toolbar);
        activity.setTitle("Coins");

        viewPager = view.findViewById(R.id.fragment_list_view_pager);
        pagerAdapter=  new ListPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = view.findViewById(R.id.frag_list_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }


    //public List<FavoriteEntity> getAllFavorites
}
