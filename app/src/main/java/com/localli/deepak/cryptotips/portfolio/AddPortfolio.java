package com.localli.deepak.cryptotips.portfolio;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.models.SupportedCurrency;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.localli.deepak.cryptotips.rest.VolleyService;
import com.localli.deepak.cryptotips.utils.VolleyResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPortfolio extends AppCompatActivity {

    String TAG = "ADD_PORTFOLIO";
    public static final String EXTRA_NAME = "com.localli.deepak.cryptotips.portfolio.EXTRA_NAME",
            EXTRA_ID ="com.localli.deepak.cryptotips.portfolio.EXTRA_ID",
            EXTRA_SYMBOL = "com.localli.deepak.cryptotips.portfolio.EXTRA_SYMBOL",
            EXTRA_AMT = "com.localli.deepak.cryptotips.portfolio.EXTRA_AMT",
            EXTRA_IP = "com.localli.deepak.cryptotips.portfolio.EXTRA_IP";
    EditText amountEt;
    TextView currencyNameTv, currentPriceTv, totalValueTv;
    FloatingActionButton doneFab;
    CircleImageView currencyIV;
    FloatingSearchView floatingSearchView;

    List<SupportedCurrency> coinList= new ArrayList<>();

    VolleyResult volleyResultCallback = null;
    VolleyResult currencyDetailResultCallback= null;
    VolleyService volleyService;
    Gson gson;

    double currentPrice = 0.0;
    double amount = 0.0;

    SupportedCurrency selectedCurrency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio);

        // initialise volley callback
        initVolleyCallback();

        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();

        volleyService = new VolleyService(volleyResultCallback,this);
        volleyService.getDataVolley("GET", CoinGeckoService.GET_SUPPORTED_CURRENCIES);

        initialiseViews();
    }

    private void initialiseViews(){

        amountEt = findViewById(R.id.add_portfolio_amount_et);
        currencyNameTv = findViewById(R.id.add_portfolio_currency_name_tv);
        currentPriceTv = findViewById(R.id.add_portfolio_current_price_tv);
        totalValueTv = findViewById(R.id.add_portfolio_total_value_tv);
        doneFab = findViewById(R.id.add_portfolio_save_fab);
        currencyIV = findViewById(R.id.add_portfolio_currency_iv);
        floatingSearchView = findViewById(R.id.add_portfolio_floating_search_view);


        amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                String s = cs.toString();
                if(s.startsWith("."))
                    s="0".concat(s);

                if(s.length() == 0 || currentPrice==0) {
                    doneFab.setVisibility(View.GONE);
                    totalValueTv.setText("0");
                    amount=0;
                }
                else{
                    amount = Double.parseDouble(s.trim());
                    double totalValue = currentPrice*amount;

                    PriceFormatter.setPriceFormatTextView(AddPortfolio.this,totalValueTv,totalValue);
                    doneFab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_NAME,selectedCurrency.getName());
                replyIntent.putExtra(EXTRA_ID,selectedCurrency.getId());
                replyIntent.putExtra(EXTRA_SYMBOL,selectedCurrency.getSymbol());
                replyIntent.putExtra(EXTRA_AMT,amount);
                replyIntent.putExtra(EXTRA_IP,0);

                setResult(RESULT_OK,replyIntent);
                finish();
            }
        });

    }


    void initVolleyCallback(){
        volleyResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                //Log.i(TAG,"Response: "+response);

                // setup adapter and recyclerview
                coinList = Arrays.asList(gson.fromJson(response,SupportedCurrency[].class));
                //Log.i(TAG,"NAME: "+coinList.get(0).getName());

                setSearchBar();

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

    void initCurrencyDetailVolleyCallabck(){
        currencyDetailResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                //Log.i(TAG,"Response: "+response);

                // setup adapter and recyclerview
                List<CoinItem> coinItemList = Arrays.asList(gson.fromJson(response,CoinItem[].class));
                if(coinItemList.size()>0) {
                    CoinItem coinItem = coinItemList.get(0);
                    setCurrencyDetails(coinItem);

                }

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

    void setSearchBar(){
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                if(!oldQuery.equals("") && newQuery.equals(""))
                    floatingSearchView.clearSuggestions();
                else{
                    floatingSearchView.showProgress();

                    // search action

                    floatingSearchView.swapSuggestions(filterResults(coinList,newQuery));
                    floatingSearchView.hideProgress();

                }
            }
        });


        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                SupportedCurrency supportedCurrency = (SupportedCurrency) searchSuggestion;
                floatingSearchView.clearSearchFocus();

                selectedCurrency = supportedCurrency;
                getCurrencyDetails(supportedCurrency);
            }

            @Override
            public void onSearchAction(String currentQuery) {

                floatingSearchView.showProgress();

                // search action

                floatingSearchView.swapSuggestions(filterResults(coinList,currentQuery));
                floatingSearchView.hideProgress();

            }
        });
    }

    List<SupportedCurrency> filterResults(List<SupportedCurrency> coinList, String query){

        List<SupportedCurrency> filteredValues = new ArrayList<>(coinList);
        for(SupportedCurrency currency : coinList){
            String searchText = currency.getBody().toLowerCase();
            if(!searchText.contains(query.toLowerCase()))
                filteredValues.remove(currency);

        }
        return filteredValues;
    }

    void getCurrencyDetails(SupportedCurrency currency){

        initCurrencyDetailVolleyCallabck();

        String GET_COINS_BY_ID_URL = String.format(CoinGeckoService.COINS_BY_ID_URL,
                SharedPrefSimpleDB.getPreferredCurrency(this),currency.getId());

        volleyService = new VolleyService(currencyDetailResultCallback,this);
        volleyService.getDataVolley("GET", GET_COINS_BY_ID_URL);
    }

    void setCurrencyDetails(CoinItem coinItem){
        currencyNameTv.setText(coinItem.getName()+" ("+coinItem.getSymbol().toUpperCase()+")");

        if(coinItem.getImage()!=null){
            Glide.with(this).load(coinItem.getImage()).into(currencyIV);
        }else{
            currencyIV.setVisibility(View.INVISIBLE);
        }

        currentPrice = coinItem.getCurrentPrice();
        PriceFormatter.setPriceFormatTextView(this,currentPriceTv,currentPrice);

        PriceFormatter.setPriceFormatTextView(this,totalValueTv,amount*coinItem.getCurrentPrice());

    }

}
