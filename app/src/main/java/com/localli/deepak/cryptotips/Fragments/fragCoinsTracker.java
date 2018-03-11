package com.localli.deepak.cryptotips.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.localli.deepak.cryptotips.Adapters.CoinItemAdapter;
import com.localli.deepak.cryptotips.Objects.CoinItem;
import com.localli.deepak.cryptotips.Objects.TopCoinItem;
import com.localli.deepak.cryptotips.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Deepak Prasad on 19-01-2018.
 */

public class fragCoinsTracker extends Fragment {
    String json;
    ListView coinsListView;
    CoinItemAdapter adapter;
    List<CoinItem> coinList;
    ArrayList<TopCoinItem> topCoins;

    String baseImageUrl,baseLinkUrl;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    String PREFERRED_CURRENCY;

    private AdView ad_ban_bottom;
    private InterstitialAd interstitialAd;
    private int touchCount=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_coins_tracker,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Track");

        initialiseAds(view);
        getTopCoins();
        initialiseViews(view);
    }
    public void getTopCoins(){
        topCoins=new ArrayList<TopCoinItem>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("coins");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TopCoinItem item=dataSnapshot.getValue(TopCoinItem.class);
                topCoins.add(item);
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
    private void initialiseAds(View view){
        ad_ban_bottom=view.findViewById(R.id.adView_frag_coins_tracker_bottom);
        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);

        adRequest=new AdRequest.Builder().build();
        interstitialAd=new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial_coin_details));
        interstitialAd.loadAd(adRequest);

    }
    public void initialiseViews(View view){

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        coinsListView=(ListView)view.findViewById(R.id.item_coin_listview);
        coinList=new ArrayList<>();
        adapter=new CoinItemAdapter(getContext(),R.layout.item_coin,coinList);
        coinsListView.setAdapter(adapter);



        coinsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                touchCount++;
                Log.e("Touchcount: ",touchCount+"");
                if(touchCount==3) {
                    //checkAndShowInterstitialAd();
                    if(interstitialAd.isLoaded())
                        interstitialAd.show();
                    else
                        Log.e("touchCount","ad not loaded");
                    touchCount=1;
                    AdRequest adRequest=new AdRequest.Builder().addTestDevice("DCCBB4A4A5460C0D6982CD6C1E346859")
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
                    interstitialAd.loadAd(adRequest);
                }

                progressDialog.setMessage("Please Wait ...");
                progressDialog.show();
                getDetailsForCoin((CoinItem)adapterView.getItemAtPosition(i));
            }
        });

        Ion.with(getContext())
                .load("https://min-api.cryptocompare.com/data/all/coinlist")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        json=result.toString();
                        Log.e("ALLCOINS: ",json);

                        try {
                            extractDataFromJson(json);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });




    }
    public void extractDataFromJson(String data) throws JSONException{

        JSONObject baseJsonObject=new JSONObject(data);
        baseImageUrl=baseJsonObject.getString("BaseImageUrl");
        baseLinkUrl=baseJsonObject.getString("BaseLinkUrl");

        JSONObject coinsArray=baseJsonObject.getJSONObject("Data");

        int count=0,i=0;
        while(i<topCoins.size()){
            JSONObject currentCoin=coinsArray.getJSONObject(topCoins.get(i).getName());
            String id=currentCoin.getString("Id"),
                    url=baseLinkUrl.concat(currentCoin.getString("Url")),
                    imageUrl=baseImageUrl.concat(currentCoin.getString("ImageUrl")),
                    name=currentCoin.getString("Name"),
                    symbol=currentCoin.getString("Symbol"),
                    coinName=currentCoin.getString("CoinName"),
                    fullName=currentCoin.getString("FullName"),
                    totalCoinSupply=currentCoin.getString("TotalCoinSupply"),
                    sortOrder=currentCoin.getString("SortOrder");
            String rank=topCoins.get(i).getRank()+"";
            CoinItem coinItem=new CoinItem(id,url,imageUrl,name,symbol,coinName,fullName,totalCoinSupply,sortOrder,rank);
            adapter.add(coinItem);
            i++;

        }

        progressDialog.dismiss();


    }
    public void getDetailsForCoin(final CoinItem item){
        //https://min-api.cryptocompare.com/data/pricemultifull?fsyms=XRP,ETH&tsyms=USD,INR
        String getDetailsUrl="https://min-api.cryptocompare.com/data/pricemultifull?fsyms=";
        String append1="&tsyms=";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        PREFERRED_CURRENCY=prefs.getString(getString(R.string.pref_preferred_currency),"USD");
        Log.e("fragCoin PC: ",PREFERRED_CURRENCY);

        String fullName=item.getFullName(),
                imageUrl=item.getImageUrl(),
                fromSymbol=item.getSymbol(),
                toSymbol=PREFERRED_CURRENCY;

        Log.e("Currency",toSymbol);
        String callApiUrl=getDetailsUrl.concat(fromSymbol).concat(append1).concat("BTC,").concat(PREFERRED_CURRENCY);
        Ion.with(getContext())
                .load(callApiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.e("COIN DETAILS: ",result.toString());
                            extractCoinDetailsFromJSON(result.toString(),item);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

    }
    public void extractCoinDetailsFromJSON(String data,CoinItem item) throws JSONException{

        String fullName=item.getFullName(),
                imageUrl=item.getImageUrl(),
                fromSymbol=item.getSymbol(),
                coinRank=item.getRank();
        JSONObject baseJsonObject=new JSONObject(data);
        JSONObject display=baseJsonObject.getJSONObject("DISPLAY");
        JSONObject fCoin=display.getJSONObject(fromSymbol);

        JSONObject tBTC=fCoin.getJSONObject("BTC");
        String priceInBTC=tBTC.getString("PRICE");

        JSONObject tSymbol=fCoin.getJSONObject(PREFERRED_CURRENCY);
        String priceSymbol=tSymbol.getString("PRICE");
        String open24hr=tSymbol.getString("OPEN24HOUR"),
                high24hr=tSymbol.getString("HIGH24HOUR"),
                low24hr=tSymbol.getString("LOW24HOUR"),
                changePct24hr=tSymbol.getString("CHANGEPCT24HOUR"),
                mktCap=tSymbol.getString("MKTCAP"),
                totalVolOver24hr=tSymbol.getString("TOTALVOLUME24HTO"),
                lastUpdated=tSymbol.getString("LASTUPDATE");




        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getLayoutInflater();

        View customView=inflater.inflate(R.layout.custome_coin_details_popup,null);

        //Populate AlertDialog
        ImageView coinImageView=(ImageView)customView.findViewById(R.id.item_coin_details_iv);
        TextView nameTv=(TextView)customView.findViewById(R.id.item_coin_details_name_tv),
                priceToCurrencyTv=(TextView)customView.findViewById(R.id.price_to_currency),
                priceToBTCTv=(TextView)customView.findViewById(R.id.price_to_btc),
                open24Tv=(TextView)customView.findViewById(R.id.hrs24_open),
                high24Tv=(TextView)customView.findViewById(R.id.hrs24_high),
                low24Tv=(TextView)customView.findViewById(R.id.hrs24_low),
                pctChangeTv=(TextView)customView.findViewById(R.id.change_pct_24hrs),
                mktCapTv=(TextView)customView.findViewById(R.id.market_cap_tv),
                volO24Tv=(TextView)customView.findViewById(R.id.vol_over_24hrs),
                rankTv=(TextView)customView.findViewById(R.id.rank_in_cc),
                lastUpdatedTv=(TextView)customView.findViewById(R.id.last_updated_tv);

        if(imageUrl!=null)
            Glide.with(coinImageView.getContext()).load(imageUrl).into(coinImageView);
        nameTv.setText(fullName);
        priceToCurrencyTv.setText(priceSymbol);
        priceToBTCTv.setText(priceInBTC);
        open24Tv.setText(open24hr);
        high24Tv.setText(high24hr);
        low24Tv.setText(low24hr);


        float change=Float.parseFloat(changePct24hr);
        if(change<0) {
            pctChangeTv.setText(changePct24hr+"% (24hr)");
            pctChangeTv.setTextColor(Color.parseColor("#D50000"));
        }
        else {
            pctChangeTv.setText("+"+changePct24hr+"% (24hr)");
            pctChangeTv.setTextColor(Color.parseColor("#4CAF50"));
        }

        mktCapTv.setText(mktCap+" (mkt cap)");
        volO24Tv.setText(totalVolOver24hr+" (vol over 24hr)");
        rankTv.setText("#"+coinRank+" in cryptocurrencies");
        lastUpdatedTv.setText(lastUpdated);

        builder.setView(customView);
        builder.create();
        progressDialog.dismiss();
        builder.show();
    }
}
