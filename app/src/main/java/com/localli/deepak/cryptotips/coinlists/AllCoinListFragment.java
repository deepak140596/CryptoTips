package com.localli.deepak.cryptotips.coinlists;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.NavigationActivity;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.currencydetails.CurrencyDetailsTabsActivity;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.localli.deepak.cryptotips.utils.SortUtils;
import com.localli.deepak.cryptotips.views.CustomItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Deepak Prasad on 12-12-2018.
 */

public class AllCoinListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener,MenuItem.OnActionExpandListener{



    AppCompatActivity activity;
    Context context;
    String TAG = "COIN_LIST_FRAGMENT";

    RequestQueue requestQueue;
    Gson gson;

    List<CoinItem> coinList;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CoinItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = (AppCompatActivity)getActivity();
        context = getContext();
        return inflater.inflate(R.layout.fragment_coin_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        // get swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.fragment_coin_list_swipe_layout);

        // set recycler view
        recyclerView = view.findViewById(R.id.fragment_coin_list_recycler_view);
        LinearLayoutManager ll = new LinearLayoutManager(activity);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setHasFixedSize(true);


        // set on refresh for swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this);

        // set scroll listener for recyler view
        addScrollListenerToRecyclerView();

        // initialise a request queue for volley
        requestQueue = Volley.newRequestQueue(context);

        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();

        prepareURL();
    }

    private void prepareURL(){
        String URL = String.format(CoinGeckoService.ALL_COINS_BY_PAGE_URL,
                SharedPrefSimpleDB.getPreferredCurrency(context).toLowerCase(),
                SharedPrefSimpleDB.getNoOfCoins(context),1);
        fetchCoinList(URL);
    }

    private void fetchCoinList(String URL){
        StringRequest request = new StringRequest(Request.Method.GET, URL, onCoinListLoaded, onCoinListError);
        requestQueue.add(request);
        swipeRefreshLayout.setRefreshing(true);
    }

    private final Response.Listener<String> onCoinListLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i(TAG,response);

            coinList = Arrays.asList(gson.fromJson(response,CoinItem[].class));
            Log.i(TAG,coinList.size()+" coins loaded");

            setAdapter(coinList);
        }
    };

    private final Response.ErrorListener onCoinListError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void setAdapter(final List<CoinItem> coinList){
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        // first sort the coin list and then set it to adapter
        sortByMethod(SharedPrefSimpleDB.getSortMethod(context));

        // initialise adapter and set its onClickListener
        adapter = new CoinItemAdapter(activity.getApplication(),(AppCompatActivity)getActivity(),context, new ArrayList<CoinItem>(), new CustomItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent intent = new Intent(activity,CurrencyDetailsTabsActivity.class);
                intent.putExtra(getString(R.string.currency_id),coinList.get(position));
                startActivity(intent);
            }
        });

        adapter.setData(coinList);
        recyclerView.setAdapter(adapter);

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    @Override
    public void onRefresh() {
        prepareURL();
    }

    public void addScrollListenerToRecyclerView(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(false);
                }else if(dy<0){
                    ((NavigationActivity)getActivity()).setBottomNavigationViewVisibility(true);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_coins_frag,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_refresh:
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                return true;

            case R.id.action_sort:
                showSortDialogBox();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(adapter!=null) {
            //adapter.notifyDataSetChanged();
            //sortByMethod(SharedPrefSimpleDB.getSortMethod(context));
            prepareURL();

        }
    }

    private void showSortDialogBox(){

        int sortMethod = SharedPrefSimpleDB.getSortMethod(context);
        new MaterialDialog.Builder(activity)
                .title("Sort Method")
                .items(R.array.sort_options)
                .buttonRippleColor(Color.RED)
                .itemsCallbackSingleChoice(sortMethod, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        SharedPrefSimpleDB.saveSortMethod(context,which);
                        sortByMethod(which);
                        return false;
                    }
                }).show();
    }


    private void sortByMethod(int sortMethod){
        SortUtils.sortCoins(coinList,sortMethod);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }




    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        onRefresh();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(newText==null || newText.trim().isEmpty())
            onRefresh();

        List<CoinItem> filteredValues = new ArrayList<>(coinList);
        for(CoinItem coinItem : coinList){
            String searchString = coinItem.getName()+ " " +coinItem.getId()+" "+coinItem.getSymbol();

            if(!searchString.toLowerCase().contains(newText.toLowerCase()))
                filteredValues.remove(coinItem);
        }

        setAdapter(filteredValues);
        return false;
    }


}
