package com.localli.deepak.cryptotips.coinlists;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PercentageFormatter;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.views.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Prasad on 23-12-2018.
 */

public class CoinItemAdapter extends RecyclerView.Adapter<CoinItemAdapter.ViewHolder> {

    String TAG = "COIN_ITEM_ADAPTER";
    Application application;
    AppCompatActivity activity;
    Context context;
    private List<CoinItem> coinList;
    private CustomItemClickListener rowListener; // onClick for the row
    private CoinItemAdapter.ViewHolder viewHolder;



    public CoinItemAdapter(Application application,AppCompatActivity activity,Context context, ArrayList<CoinItem> coinList, CustomItemClickListener listener){
        this.context = context;
        this.coinList = coinList;
        this.rowListener = listener;
        this.application = application;
        this.activity = activity;
    }

    public CoinItemAdapter setData(List<CoinItem> coinList){
        this.coinList = coinList;
        this.notifyDataSetChanged();
        return this;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coin,parent,false);
        viewHolder = new CoinItemAdapter.ViewHolder(view,rowListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinItemAdapter.ViewHolder viewHolder, final int position) {

        final CoinItem coin = coinList.get(position);

        // set rank
        if(coin.getMarketCapRank() == null){
            viewHolder.rankTextView.setText("N/A");
        }else{
            viewHolder.rankTextView.setText("#"+coin.getMarketCapRank().toString());
        }

        // set name with symbol
        String fullName = coin.getName()+" ("+ coin.getSymbol().toUpperCase()+") ";
        if(fullName == null){
            viewHolder.currencyFullNameTv.setText("N/A");
        }else{
            viewHolder.currencyFullNameTv.setText(fullName);
        }

        // set price
        if(coin.getCurrentPrice() == null){
            viewHolder.currencyListCurrPriceTextView.setText("N/A");
        }else{
            PriceFormatter.setPriceFormatTextView(context,viewHolder.currencyListCurrPriceTextView,coin.getCurrentPrice());
        }

        // set 24hr change
        if(coin.getPriceChangePercentage24h() == null)
            viewHolder.dayChangeTextView.setText("N/A");
        else{
            //PercentageFormatter.setPercentChangeTextView(context,viewHolder.dayChangeTextView,coin.getPriceChangePercentage24h());
            PercentageFormatter.setPriceAndPercentChangeTextView(context,viewHolder.dayChangeTextView,coin.getPriceChangePercentage24h(),
                    coin.getPriceChange24h());
        }

        if(coin.getMarketCap() == null)
            viewHolder.mktCapTv.setText("N/A");
        else{
            PriceFormatter.setPriceFormatTextView(context,viewHolder.mktCapTv,coin.getMarketCap());
        }

        if(coin.getTotalVolume() == null)
            viewHolder.volumeTv.setText("N/A");
        else{
            PriceFormatter.setPriceFormatTextView(context,viewHolder.volumeTv,coin.getTotalVolume());
        }

        // set Image resource
        if(coin.getImage() != null){
            Glide.with(context).load(coin.getImage()).into(viewHolder.currencyListCoinImageView);
        }else{
            viewHolder.currencyListCoinImageView.setVisibility(View.INVISIBLE);
        }

        if(SharedPrefSimpleDB.checkForFavorite(context,coin.getId())) {
            viewHolder.favBorderIv.setVisibility(View.GONE);
            viewHolder.favFilledIv.setVisibility(View.VISIBLE);

        }
        else{
            viewHolder.favBorderIv.setVisibility(View.VISIBLE);
            viewHolder.favFilledIv.setVisibility(View.GONE);
        }

        viewHolder.favFilledIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefSimpleDB.deleteFavoriteFromDB(context,coin.getId());
                notifyDataSetChanged();
            }
        });

        viewHolder.favBorderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefSimpleDB.insertFavoriteToDB(context,coin.getId());
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView currencyFullNameTv;
        //private TextView oneHourChangeTextView;
        private TextView dayChangeTextView;
        //private TextView weekChangeTextView;
        private TextView rankTextView;
        private TextView currencyListCurrPriceTextView;
        private ImageView currencyListCoinImageView;
        private TextView volumeTv, mktCapTv;
        private CustomItemClickListener listener;
        private ImageView favFilledIv, favBorderIv;

        private ViewHolder(View view, CustomItemClickListener listener){
            super(view);
            view.setOnClickListener(this);

            // initialise the views with the layout
            rankTextView = view.findViewById(R.id.item_coin_rank_tv);
            currencyFullNameTv = view.findViewById(R.id.item_coin_name_tv);
            //oneHourChangeTextView = view.findViewById(R.id.item_coin_1hr_change_pct_tv);
            dayChangeTextView = view.findViewById(R.id.item_coin_24hr_change_pct_tv);
            //weekChangeTextView = view.findViewById(R.id.item_coin_7day_change_pct_tv);
            currencyListCurrPriceTextView = view.findViewById(R.id.item_coin_price_tv);
            currencyListCoinImageView  = view.findViewById(R.id.item_coin_iv);
            volumeTv = view.findViewById(R.id.item_coin_24hr_vol_tv);
            mktCapTv = view.findViewById(R.id.item_coin_mkt_cap_tv);

            favFilledIv = view.findViewById(R.id.item_coin_fav_filled_iv);
            favBorderIv = view.findViewById(R.id.item_coin_fav_border_iv);

            this.listener = listener;
        }

        // onClick will be used when the row is clicked
        @Override
        public void onClick(View v) {

            listener.onItemClick(getAdapterPosition(),v);
        }
    }

    public List<CoinItem> getCoinList(){
        return coinList;
    }
}
