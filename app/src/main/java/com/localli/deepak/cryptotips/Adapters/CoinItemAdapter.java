package com.localli.deepak.cryptotips.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.localli.deepak.cryptotips.Objects.CoinItem;
import com.localli.deepak.cryptotips.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Deepak Prasad on 20-01-2018.
 */

public class CoinItemAdapter extends ArrayAdapter<CoinItem> {


    public CoinItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CoinItem> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_coin, parent, false);

        ImageView coinIv=(ImageView)convertView.findViewById(R.id.item_coin_iv);
        TextView coinTv=(TextView)convertView.findViewById(R.id.item_coin_name_tv);
        TextView rankTv=(TextView)convertView.findViewById(R.id.item_coin_rank_tv);


        CoinItem item=getItem(position);
        String imageUrl=item.getImageUrl(),
                coinName=item.getFullName(),
                rank=item.getRank();
        coinTv.setText(coinName);
        rankTv.setText("#"+rank);
        if(imageUrl!=null)
            Glide.with(coinIv.getContext()).load(imageUrl).into(coinIv);

        getPriceAndPctChange(item,convertView);

        return convertView;
    }
    public void getPriceAndPctChange(final CoinItem item, final View view){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String fromSymbol=item.getSymbol();

        String getDetailsUrl="https://min-api.cryptocompare.com/data/pricemultifull?fsyms=";
        String append1="&tsyms=";

        String PREFERRED_CURRENCY=prefs.getString(getContext().getString(R.string.pref_preferred_currency),"USD");

        String callApiUrl=getDetailsUrl.concat(fromSymbol).concat(append1).concat(PREFERRED_CURRENCY);


        Ion.with(getContext())
                .load(callApiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.e("COIN DETAILS: ",result.toString());
                            extractCoinDetailsFromJSON(result.toString(),item,view);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }
    public void extractCoinDetailsFromJSON(String data,CoinItem item,View view) throws JSONException{

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String PREFERRED_CURRENCY=prefs.getString(getContext().getString(R.string.pref_preferred_currency),"USD");

        String fromSymbol=item.getSymbol();
        JSONObject baseJsonObject=new JSONObject(data);
        JSONObject display=baseJsonObject.getJSONObject("DISPLAY");
        JSONObject fCoin=display.getJSONObject(fromSymbol);

        JSONObject tSymbol=fCoin.getJSONObject(PREFERRED_CURRENCY);
        String priceSymbol=tSymbol.getString("PRICE");
        String changePct24hr=tSymbol.getString("CHANGEPCT24HOUR");

        TextView priceTv=(TextView)view.findViewById(R.id.item_coin_price_tv_adapter),
                changePctTv=(TextView)view.findViewById(R.id.item_pct_change_tv_adapter);

        priceTv.setText(priceSymbol);
        changePctTv.setText(changePct24hr);
        float pctChange=Float.parseFloat(changePct24hr);
        if(pctChange<0) {
            changePctTv.setText(" ("+changePct24hr+"%)");
            changePctTv.setTextColor(Color.parseColor("#D50000"));
        }
        else {
            changePctTv.setText(" (+"+changePct24hr+"%)");
            changePctTv.setTextColor(Color.parseColor("#4CAF50"));
        }

    }

}
