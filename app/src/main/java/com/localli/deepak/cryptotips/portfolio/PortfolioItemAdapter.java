package com.localli.deepak.cryptotips.portfolio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.DataBase.portfolio.PortfolioEntity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.formatters.SupplyFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;

import java.util.List;

/**
 * Created by Deepak Prasad on 21-01-2019.
 */

public class PortfolioItemAdapter extends RecyclerView.Adapter<PortfolioItemAdapter.ViewHolder> {

    String TAG = "PORTFOLIO_ITEM_ADAPTER";
    Context context;
    List<PortfolioEntity> portfolio;
    List<CoinItem> coinList;

    public PortfolioItemAdapter(Context context, List<PortfolioEntity> portfolio, List<CoinItem> coinList){
        this.context = context;
        this.portfolio = portfolio;
        this.coinList = coinList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_portfolio,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final PortfolioEntity item = portfolio.get(i);
        final CoinItem coinItem = coinList.get(i);

        String fullName = item.getName()+" ("+ item.getSymbol().toUpperCase()+") ";
        viewHolder.coinNameTv.setText(fullName);

        //SupplyFormatter.setSupplyTextView(context,viewHolder.totalAmtTv,(double)item.getAmt_of_coin());
        viewHolder.totalAmtTv.setText(item.getAmt_of_coin()+" "+item.getSymbol().toUpperCase());

        PriceFormatter.setPriceFormatTextView(context,viewHolder.boughtAtTv,(double)item.getInitial_price());
        //viewHolder.boughtAtTv.setText(item.getInitial_price()+"");

        PriceFormatter.setPriceFormatTextView(context,viewHolder.currentPriceTv,(double)coinItem.getCurrentPrice());
        //viewHolder.currentPriceTv.setText(coinItem.getCurrentPrice()+"");

        PriceFormatter.setPriceFormatTextView(context,viewHolder.currentValueTv,
                (double)item.getAmt_of_coin()*coinItem.getCurrentPrice());
        //viewHolder.currentValueTv.setText(item.getAmt_of_coin()*coinItem.getCurrentPrice()+"");

        if(coinItem.getImage()!=null){
            Glide.with(context).load(coinItem.getImage()).into(viewHolder.coinImageView);
        }else{
            viewHolder.coinImageView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return portfolio.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView coinNameTv, currentPriceTv, totalAmtTv, boughtAtTv, currentValueTv;
        public ImageView coinImageView;
        public RelativeLayout foregroundRl, backgroundRl;

        public ViewHolder(@NonNull View view) {
            super(view);

            coinNameTv = view.findViewById(R.id.item_coin_name_tv);
            currentPriceTv = view.findViewById(R.id.item_coin_current_price_tv);
            boughtAtTv = view.findViewById(R.id.item_coin_bought_at_tv);
            totalAmtTv = view.findViewById(R.id.item_coin_total_amt_tv);
            currentValueTv = view.findViewById(R.id.item_coin_current_value_tv);
            coinImageView = view.findViewById(R.id.item_coin_iv);
            foregroundRl = view.findViewById(R.id.item_portfolio_foreground);
            backgroundRl = view.findViewById(R.id.item_portfolio_background);

        }
    }


    public void removeItem(int position){
        portfolio.remove(position);

        notifyItemRemoved(position);
    }

    public void restoreItem(PortfolioEntity item,int position){
        portfolio.add(position,item);

        notifyItemInserted(position);
    }

}
