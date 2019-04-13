package com.localli.deepak.cryptotips.portfolio;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.DataBase.portfolio.PortfolioEntity;
import com.localli.deepak.cryptotips.NavigationActivity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.localli.deepak.cryptotips.rest.VolleyService;
import com.localli.deepak.cryptotips.utils.RecyclerItemTouchHelper;
import com.localli.deepak.cryptotips.utils.VolleyResult;
import com.localli.deepak.cryptotips.viewmodel.PortfolioViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Deepak Prasad on 20-01-2019.
 */

public class PortfolioFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        SwipeRefreshLayout.OnRefreshListener{

    String TAG = "PORTFOLIO_FRAGMENT";
    public static final int ADD_PORTFOLIO_REQUEST = 1;
    AppCompatActivity activity;

    private PortfolioViewModel portfolioViewModel;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton addFab;
    TextView totalWorthTv;
    LinearLayout emptyState;

    PortfolioItemAdapter adapter;
    List<PortfolioEntity> list = new ArrayList<>();
    List<CoinItem> coinList = new ArrayList<>();

    VolleyResult volleyResultCallback = null;
    VolleyService volleyService;
    Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        activity = (AppCompatActivity)getActivity();

        portfolioViewModel = ViewModelProviders.of(activity).get(PortfolioViewModel.class);

        return inflater.inflate(R.layout.fragment_portfolio,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialise volley callback
        initVolleyCallback();

        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();

        addFab = view.findViewById(R.id.fragment_portfolio_add_fab);
        // get swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.fragment_portfolio_swipe_layout);
        totalWorthTv = view.findViewById(R.id.frag_portfolio_total_worth_tv);
        emptyState = view.findViewById(R.id.frag_portfolio_empty_state_ll);

        // set recycler view
        recyclerView = view.findViewById(R.id.fragment_portfolio_recycler_view);
        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        // set on refresh for swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this);

        // add scroll listener to recyclerview
        addScrollListenerToRecyclerView();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this,1);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // get saved portfolio

        portfolioViewModel.getPortfolio().observe(activity, new Observer<List<PortfolioEntity>>() {
            @Override
            public void onChanged(@Nullable List<PortfolioEntity> portfolioEntities) {

                swipeRefreshLayout.setRefreshing(true);
                // update list
                list = portfolioEntities;

                String savedPortfolioCoins = "";
                for(PortfolioEntity entity : list){
                    Log.i(TAG," NAME: "+entity.getName());
                    savedPortfolioCoins = savedPortfolioCoins.concat(",").concat(entity.getId());
                }

                if(list.isEmpty()) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);

                    emptyState.setVisibility(View.VISIBLE);
                }else
                    emptyState.setVisibility(View.GONE);


                // get live prices for the saved portfolio
                setUpPortfolio(savedPortfolioCoins);

            }
        });


        // add a new coin to the portfolio
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,AddPortfolio.class);
                startActivityForResult(intent,ADD_PORTFOLIO_REQUEST);

            }
        });

    }

    private void setUpPortfolio(String savedPortfolioCoins){

        String GET_COINS_BY_ID_URL =  String.format(CoinGeckoService.COINS_BY_ID_URL,
                SharedPrefSimpleDB.getPreferredCurrency(activity),savedPortfolioCoins);

        volleyService = new VolleyService(volleyResultCallback,activity);
        volleyService.getDataVolley("GET",GET_COINS_BY_ID_URL);
    }

    void initVolleyCallback(){
        volleyResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                //Log.i(TAG,"Response: "+response);

                // setup adapter and recyclerview
                coinList = Arrays.asList(gson.fromJson(response,CoinItem[].class));
                setUpAdapter(list,coinList);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.e(TAG,"Error: "+error);
            }

            @Override
            public void notifySuccess(String requestType, String response, AlertEntity alertEntity) {

            }
        };
    }

    private void setUpAdapter(List<PortfolioEntity> portfolioEntityList, List<CoinItem> coinList){

        Collections.sort(portfolioEntityList,PortfolioEntity.compareByNameAsc);
        Collections.sort(coinList,CoinItem.compareByNameAsc);

        adapter = new PortfolioItemAdapter(activity,portfolioEntityList,coinList);
        recyclerView.setAdapter(adapter);
        updateTotalWorth();
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof PortfolioItemAdapter.ViewHolder){
            // get name of removed item to display in the snackbar
            String name = list.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final PortfolioEntity deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from the recycler view
            //adapter.removeItem(viewHolder.getAdapterPosition());
            // TODO delete from Portfolio DB
            portfolioViewModel.delete(deletedItem);

            // showing snackbar with undo option
            Snackbar snackbar = Snackbar.make(swipeRefreshLayout,name +" removed from portfolio!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // undo is selected, restore the item
                    //adapter.restoreItem(deletedItem,deletedIndex);
                    // TODO add to Portfolio DB
                    portfolioViewModel.insert(deletedItem);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onRefresh() {
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_PORTFOLIO_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddPortfolio.EXTRA_NAME);
            String id = data.getStringExtra(AddPortfolio.EXTRA_ID);
            String symbol = data.getStringExtra(AddPortfolio.EXTRA_SYMBOL);
            double amount = data.getDoubleExtra(AddPortfolio.EXTRA_AMT,0);
            double boughtAt = data.getDoubleExtra(AddPortfolio.EXTRA_IP,0);

            PortfolioEntity entity = new PortfolioEntity(id,name,symbol,(float)amount,0.0f);
            portfolioViewModel.insert(entity);
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


    void updateTotalWorth(){
        double totalWorth = 0;
        int i;
        if(coinList.size()==0 || list.size()==0) {
            totalWorthTv.setText("0.0");
            return;
        }
        for(i=0;i<list.size();i++){
            double currentPrice = coinList.get(i).getCurrentPrice();
            double amount = list.get(i).getAmt_of_coin();
            totalWorth+= amount*currentPrice;

        }

        PriceFormatter.setPriceFormatTextView(activity,totalWorthTv,totalWorth);

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }
}
