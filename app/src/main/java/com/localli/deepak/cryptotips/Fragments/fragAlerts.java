package com.localli.deepak.cryptotips.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.localli.deepak.cryptotips.Adapters.AlertItemAdapter;
import com.localli.deepak.cryptotips.Objects.AlertItem;
import com.localli.deepak.cryptotips.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Prasad on 16-01-2018.
 */

public class fragAlerts extends Fragment {

    ListView alertListView;
    List<AlertItem> alertLists;
    AlertItemAdapter adapter;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    private AdView ad_ban_bottom;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_alerts,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Alerts");

        initialiseAds(view);
        initialiseViews(view);
        callFirebaseDatabase();
    }

    private void initialiseAds(View view){
        ad_ban_bottom=view.findViewById(R.id.adView_frag_alerts_bottom);
        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);

    }
    public void initialiseViews(View view){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Wait");
        progressDialog.show();
        alertListView=(ListView)view.findViewById(R.id.alerts_listview);
        alertLists=new ArrayList<>();
        adapter=new AlertItemAdapter(getContext(),R.layout.item_alerts,alertLists);
        alertListView.setAdapter(adapter);
    }
    public void callFirebaseDatabase(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("alerts");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AlertItem item=dataSnapshot.getValue(AlertItem.class);
                adapter.add(item);
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

        progressDialog.dismiss();
    }
}
