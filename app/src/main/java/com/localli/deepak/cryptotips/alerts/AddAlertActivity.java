package com.localli.deepak.cryptotips.alerts;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.localli.deepak.cryptotips.utils.CurrencySymbolUtils;
import com.localli.deepak.cryptotips.utils.VolleyResult;
import com.localli.deepak.cryptotips.viewmodel.AlertViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAlertActivity extends AppCompatActivity {

    String TAG = "ADD_ALERT_ACTIVITY";

    CircleImageView coinImageView;
    TextView coinNameTv, currentPrice1Tv, currentPrice2Tv, alertPercentage1Tv, alertPercentage2Tv;
    RadioGroup triggeredByRg;
    CheckBox risesAboveCb, dropsBelowCb;
    EditText risesAboveEt, dropsBelowEt;
    FloatingActionButton saveFab;
    FloatingSearchView floatingSearchView;
    RelativeLayout addAlertRl;

    List<SupportedCurrency> supportedCurrencyList= new ArrayList<>();

    VolleyResult volleyResultCallback = null;
    VolleyResult currencyDetailResultCallback= null;
    VolleyService volleyService;
    Gson gson;

    SupportedCurrency selectedCurrency;
    CoinItem selectedAlertCurrency;

    boolean isTriggeredByPrice = false;


    double currentPrice = 0.0;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        // initialise Supported currencies volley callback
        initGetSupportedCurrenciesVolleyCallback();

        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();

        volleyService = new VolleyService(volleyResultCallback,this);
        volleyService.getDataVolley("GET", CoinGeckoService.GET_SUPPORTED_CURRENCIES);

        initialiseViews();

    }

    @SuppressLint("RestrictedApi")
    private void initialiseViews(){
        coinImageView = findViewById(R.id.add_alert_currency_iv);
        coinNameTv = findViewById(R.id.add_alert_currency_name_tv);
        currentPrice1Tv = findViewById(R.id.add_alert_current_price_tv1);
        currentPrice2Tv = findViewById(R.id.add_alert_current_price_tv2);
        alertPercentage1Tv = findViewById(R.id.add_alert_percentage_tv1);
        alertPercentage2Tv = findViewById(R.id.add_alert_percentage_tv2);
        triggeredByRg = findViewById(R.id.add_alert_trigger_rg);
        risesAboveCb = findViewById(R.id.add_alert_rise_above_cb);
        dropsBelowCb = findViewById(R.id.add_alert_drops_below_cb);
        risesAboveEt = findViewById(R.id.add_alert_rise_above_et);
        dropsBelowEt = findViewById(R.id.add_alert_drops_below_et);
        saveFab = findViewById(R.id.add_alert_save_fab);
        floatingSearchView = findViewById(R.id.add_alert_floating_search_view);
        addAlertRl = findViewById(R.id.add_alert_rl);



        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareToSaveAlerts();
            }
        });

        triggeredByRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setRiseDropEditText(checkedId);
            }
        });

        risesAboveEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().isEmpty()) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }
                if(Double.parseDouble(s.toString()) <=0 ) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }

                if(selectedAlertCurrency == null) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }

                /*if(currentPrice == 0){
                    saveFab.setVisibility(View.GONE);
                    return;
                }*/
                saveFab.setVisibility(View.VISIBLE);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dropsBelowEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty()) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }
                if(Double.parseDouble(s.toString()) <=0 ) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }
                /*if(currentPrice == 0){
                    saveFab.setVisibility(View.GONE);
                    return;
                }*/

                if(selectedAlertCurrency == null) {
                    saveFab.setVisibility(View.GONE);
                    return;
                }

                saveFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dropsBelowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(risesAboveCb.isChecked() || dropsBelowCb.isChecked()) {
                    if (selectedCurrency != null)
                        saveFab.setVisibility(View.VISIBLE);
                }
                else
                    saveFab.setVisibility(View.GONE);
            }
        });

        risesAboveCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(risesAboveCb.isChecked() || dropsBelowCb.isChecked()) {
                    if(selectedCurrency != null)
                        saveFab.setVisibility(View.VISIBLE);
                }
                else
                    saveFab.setVisibility(View.GONE);
            }
        });

    }

    void initGetSupportedCurrenciesVolleyCallback(){
        volleyResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                //Log.i(TAG,"Response: "+response);

                // setup adapter and recyclerview
                supportedCurrencyList = Arrays.asList(gson.fromJson(response,SupportedCurrency[].class));
                Log.i(TAG,"NAME: "+supportedCurrencyList.get(0).getName());

                setSearchBar();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.e(TAG,"Error: "+error);
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

                    floatingSearchView.swapSuggestions(filterResults(supportedCurrencyList,newQuery));
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

                floatingSearchView.swapSuggestions(filterResults(supportedCurrencyList,currentQuery));
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
        };
    }

    void getCurrencyDetails(SupportedCurrency currency){

        initCurrencyDetailVolleyCallabck();

        String GET_COINS_BY_ID_URL = String.format(CoinGeckoService.COINS_BY_ID_URL,
                SharedPrefSimpleDB.getPreferredCurrency(this),currency.getId());

        volleyService = new VolleyService(currencyDetailResultCallback,this);
        volleyService.getDataVolley("GET", GET_COINS_BY_ID_URL);
    }


    @SuppressLint("RestrictedApi")
    void setCurrencyDetails(CoinItem coinItem){
        selectedAlertCurrency = coinItem;
        coinNameTv.setText(coinItem.getName()+" ("+coinItem.getSymbol().toUpperCase()+")");

        if(coinItem.getImage()!=null){
            Glide.with(this).load(coinItem.getImage()).into(coinImageView);
        }else{
            coinImageView.setVisibility(View.INVISIBLE);
        }

        currentPrice = coinItem.getCurrentPrice();
        saveFab.setVisibility(View.VISIBLE);
        PriceFormatter.setPriceFormatTextView(this,currentPrice1Tv,currentPrice);
        PriceFormatter.setPriceFormatTextView(this,currentPrice2Tv,currentPrice);

        setRiseDropEditText(triggeredByRg.getCheckedRadioButtonId());

    }


    private void setRiseDropEditText(int checkedId){
        if(checkedId == R.id.add_alert_trigger_by_price_rb){

            isTriggeredByPrice = true;

            alertPercentage1Tv.setVisibility(View.INVISIBLE);
            alertPercentage2Tv.setVisibility(View.INVISIBLE);


            risesAboveEt.setText(PriceFormatter.priceFormatterWithoutSymbol(currentPrice*1.1));
            dropsBelowEt.setText(PriceFormatter.priceFormatterWithoutSymbol(currentPrice*0.9));
        }
        else if(checkedId==R.id.add_alert_trigger_by_percentage_rb){

            isTriggeredByPrice = false;

            alertPercentage1Tv.setVisibility(View.VISIBLE);
            alertPercentage2Tv.setVisibility(View.VISIBLE);

            risesAboveEt.setText("10");
            dropsBelowEt.setText("10");
        }
    }

    private void prepareToSaveAlerts(){

        double currentPrice = selectedAlertCurrency.getCurrentPrice();
        if(risesAboveCb.isChecked()){

            long currentTimeInMS = System.currentTimeMillis();
            double triggerPrice ;
            if(isTriggeredByPrice)
                triggerPrice = Double.parseDouble(risesAboveEt.getText().toString());
            else
                triggerPrice = ((100+Double.parseDouble(risesAboveEt.getText().toString()))/100)*currentPrice;

            int riseDrop = 1;

            saveAlertToDB(currentTimeInMS,triggerPrice,currentPrice,riseDrop);
        }
        if(dropsBelowCb.isChecked()){
            long currentTimeInMS = System.currentTimeMillis();
            double triggerPrice ;
            if(isTriggeredByPrice)
                triggerPrice = Double.parseDouble(dropsBelowEt.getText().toString());
            else
                triggerPrice = ((100-Double.parseDouble(dropsBelowEt.getText().toString()))/100)*currentPrice;

            int riseDrop = 0;

            saveAlertToDB(currentTimeInMS,triggerPrice,currentPrice,riseDrop);
        }

        if(!(dropsBelowCb.isChecked() || risesAboveCb.isChecked())){
            Snackbar.make(addAlertRl,"Please select one Check Box!",Snackbar.LENGTH_LONG).show();
            return;
        }

        finish();
    }


    private void saveAlertToDB(long id,double triggerPrice,double initialSavedPrice,int riseDrop){
        AlertViewModel alertViewModel = ViewModelProviders.of(this).get(AlertViewModel.class);


        //long id = System.currentTimeMillis();
        double percentChange = triggerPrice/initialSavedPrice;

        AlertEntity alertEntity = new AlertEntity(id,selectedAlertCurrency.getId(),selectedAlertCurrency.getName(),
                selectedAlertCurrency.getSymbol(),selectedAlertCurrency.getImage(),triggerPrice,initialSavedPrice,percentChange,
                SharedPrefSimpleDB.getPreferredCurrency(this),riseDrop,0);

        alertViewModel.insert(alertEntity);
    }
}
