package com.localli.deepak.cryptotips.currencydetails.marketdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PercentageFormatter;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.formatters.SupplyFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;

import static com.localli.deepak.cryptotips.currencydetails.chartandtable.GraphFragment.CURRENCY_ID;

/**
 * Created by Deepak Prasad on 24-12-2018.
 */

public class MarketDetailsFragment extends Fragment{

    public String TAG ="FRAG_MARKET";
    AppCompatActivity activity;
    Context context;

    CoinItem coinItem;

    TextView rankTv,nameTv, currentSupplyTv, totalSupplyTv, marketCapTv,totalVolumeTv,
        high24hrTv, low24hrTv, priceChange24hrTv, priceChange24hrPctTv, marketCapChangeTv, marketCapChangePctTv,
        athTv, athPctChangeTv;

    RequestQueue requestQueue;
    Gson gson;


    public static MarketDetailsFragment newInstance(CoinItem coinItem){
        MarketDetailsFragment marketDetailsFragment = new MarketDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CURRENCY_ID,coinItem);
        marketDetailsFragment.setArguments(bundle);
        return marketDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.activity = (AppCompatActivity)getActivity();
        this.context = getContext();
        coinItem = (CoinItem)this.getArguments().getSerializable(CURRENCY_ID);
        return inflater.inflate(R.layout.fragment_market_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        initialiseView(view);
        setCoinDetails();
    }

    public void initialiseView(View view){
        rankTv = view.findViewById(R.id.frag_market_coin_rank_tv);
        nameTv = view.findViewById(R.id.frag_market_coin_name_tv);
        currentSupplyTv = view.findViewById(R.id.frag_market_current_supply_tv);
        totalSupplyTv = view.findViewById(R.id.frag_market_total_supply_tv);
        marketCapTv = view.findViewById(R.id.frag_market_mkt_cap_tv);
        totalVolumeTv = view.findViewById(R.id.frag_market_total_volume_tv);
        high24hrTv = view.findViewById(R.id.frag_market_high24hr_tv);
        low24hrTv = view.findViewById(R.id.frag_market_low24hr_tv);
        priceChange24hrPctTv = view.findViewById(R.id.frag_market_price_change_24hr_pct_tv);
        priceChange24hrTv = view.findViewById(R.id.frag_market_price_change_24hr_tv);
        marketCapChangePctTv = view.findViewById(R.id.frag_market_cap_change_pct_tv);
        marketCapChangeTv = view.findViewById(R.id.frag_market_cap_change_tv);
        athTv = view.findViewById(R.id.frag_market_ath_tv);
        athPctChangeTv = view.findViewById(R.id.frag_market_ath_pct_tv);



    }

    public void setCoinDetails(){
        rankTv.setText("#".concat(coinItem.getMarketCapRank().toString()));
        nameTv.setText(coinItem.getName().concat(" (").concat(coinItem.getSymbol().toUpperCase()).concat(") "));

        if(coinItem.getCirculatingSupply()==null)
            currentSupplyTv.setText("N/A");
        else
            SupplyFormatter.setSupplyTextView(context,currentSupplyTv,Double.parseDouble(coinItem.getCirculatingSupply()));

        if(coinItem.getTotalSupply()==null)
            totalSupplyTv.setText("N/A");
        else
            SupplyFormatter.setSupplyTextView(context,totalSupplyTv,coinItem.getTotalSupply().doubleValue());

        if(coinItem.getMarketCap() == null)
            marketCapTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,marketCapTv,coinItem.getMarketCap());

        if(coinItem.getTotalVolume() == null)
            totalVolumeTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,totalVolumeTv,coinItem.getTotalVolume());

        if(coinItem.getHigh24h() == null)
            high24hrTv.setText("N/A");
        else PriceFormatter.setPriceFormatTextView(context,high24hrTv,coinItem.getHigh24h());

        if(coinItem.getLow24h() == null)
            low24hrTv.setText("N/A");
        else PriceFormatter.setPriceFormatTextView(context,low24hrTv,coinItem.getLow24h());

        if(coinItem.getPriceChange24h() == null)
            priceChange24hrTv.setText("N/A");
        else PriceFormatter.setPriceFormatTextView(context,priceChange24hrTv,coinItem.getPriceChange24h());

        if(coinItem.getPriceChangePercentage24h() == null)
            priceChange24hrPctTv.setText("N/A");
        else PercentageFormatter.setPercentChangeTextView(context,priceChange24hrPctTv,coinItem.getPriceChangePercentage24h());


        if(coinItem.getMarketCapChange24h() == null)
            marketCapChangeTv.setText("N/A");
        else PriceFormatter.setPriceFormatTextView(context,marketCapChangeTv,coinItem.getMarketCapChange24h());

        if(coinItem.getMarketCapChangePercentage24h() == null)
            marketCapChangePctTv.setText("N/A");
        else PercentageFormatter.setPercentChangeTextView(context,marketCapChangePctTv,coinItem.getMarketCapChangePercentage24h());

        if(coinItem.getAth() == null)
            athTv.setText("N/A");
        else PriceFormatter.setPriceFormatTextView(context,athTv,coinItem.getAth());

        if(coinItem.getAthChangePercentage() == null)
            athPctChangeTv.setText("N/A");
        else PercentageFormatter.setPercentChangeTextView(context,athPctChangeTv,coinItem.getAthChangePercentage());

    }
}
