package com.localli.deepak.cryptotips.alerts;

import android.annotation.SuppressLint;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
;

import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.NavigationActivity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.utils.RecyclerItemTouchHelper;
import com.localli.deepak.cryptotips.viewmodel.AlertViewModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

public class AlertFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        SwipeRefreshLayout.OnRefreshListener{

    String TAG = "ALERT_FRAGMENT";
    AppCompatActivity activity;

    private AlertViewModel alertViewModel;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton addFab;
    LinearLayout emptyState;

    List<AlertEntity> alertList ;
    AlertItemAdapter adapter;

    //VolleyResult volleyResultCallback = null;
    //VolleyService volleyService;
    //Gson gson;

    boolean isRecyclerViewInflated;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        activity = (AppCompatActivity)getActivity();

        alertViewModel = ViewModelProviders.of(activity).get(AlertViewModel.class);

        isRecyclerViewInflated = false;

        return inflater.inflate(R.layout.fragment_alert,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.frag_alert_toolabr);
        activity.setSupportActionBar(toolbar);
        activity.setTitle("Alerts");

        addFab = view.findViewById(R.id.frag_alert_add_fab);
        swipeRefreshLayout = view.findViewById(R.id.frag_alert_swipe_layout);
        recyclerView = view.findViewById(R.id.frag_alert_recycler_view);
        emptyState = view.findViewById(R.id.frag_alert_empty_state_ll);

        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));


        swipeRefreshLayout.setOnRefreshListener(this);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,AddAlertActivity.class));
            }
        });

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this,2);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        alertList = new ArrayList<>();
        adapter = new AlertItemAdapter(activity,alertList);
        recyclerView.setAdapter(adapter);

        addScrollListenerToRecyclerView();
        getAlerts();
    }

    public void getAlerts(){
        alertViewModel.getAlertsList().observe(activity, new Observer<List<AlertEntity>>() {
            @Override
            public void onChanged(@Nullable List<AlertEntity> alertEntities) {
                alertList = alertEntities;
                //Log.i(TAG,"Size: "+alertEntities.size());
                //Log.i(TAG,"NAME2: "+alertEntities.get(1).getName());
                adapter.setItems(alertList);

                if(!isRecyclerViewInflated) {
                    recyclerView.setAdapter(adapter);
                    isRecyclerViewInflated = true;
                }

                if(adapter.getItemCount()==0){
                    emptyState.setVisibility(View.VISIBLE);
                }else
                    emptyState.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof AlertItemAdapter.ViewHolder){
            // get name of removed item to display in the snackbar
            String name = alertList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final AlertEntity deletedItem = alertList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from the recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            // TODO delete from Portfolio DB
            alertViewModel.delete(deletedItem);

            // showing snackbar with undo option
            Snackbar snackbar = Snackbar.make(swipeRefreshLayout,name +" removed from portfolio!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // undo is selected, restore the item
                    adapter.restoreItem(deletedItem,deletedIndex);
                    // TODO add to Portfolio DB
                    alertViewModel.insert(deletedItem);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    public void addScrollListenerToRecyclerView(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(false);
                    addFab.setVisibility(View.INVISIBLE);
                }else if(dy<0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(true);
                    addFab.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
