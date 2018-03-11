package com.localli.deepak.cryptotips.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.localli.deepak.cryptotips.Adapters.NewsItemAdapter;
import com.localli.deepak.cryptotips.Objects.NewsItem;
import com.localli.deepak.cryptotips.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Prasad on 16-01-2018.
 */

public class fragTips extends Fragment {

    private List<NewsItem> newsList=new ArrayList<>();
    private RecyclerView newsRecyclerView;
    private NewsItemAdapter newsItemAdapter;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private AdView ad_ban_bottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tips,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tips");

        initialiseAds(view);
        initialiseViews(view);
        initialiseFirebaseData();
    }
    private void initialiseAds(View view){
        ad_ban_bottom=view.findViewById(R.id.adView_frag_tips_bottom);
        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);

    }

    private void initialiseViews(View view){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();

        newsRecyclerView=(RecyclerView)view.findViewById(R.id.frag_tips_recycler_view);

        newsItemAdapter = new NewsItemAdapter(newsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerView.setAdapter(newsItemAdapter);

    }
    private void initialiseFirebaseData(){

        databaseReference= FirebaseDatabase.getInstance().getReference().child("tips");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewsItem item=dataSnapshot.getValue(NewsItem.class);
                //newsList.add(item);
                addNews(item);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void addNews(NewsItem item){
        newsList.add(item);
        newsItemAdapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }
}
