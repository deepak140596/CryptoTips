package com.localli.deepak.cryptotips.alerts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.viewmodel.AlertViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepak Prasad on 26-01-2019.
 */

public class AlertItemAdapter  extends RecyclerView.Adapter<AlertItemAdapter.ViewHolder> {

    String TAG = "ALERT_ITEM_ADAPTER";
    AppCompatActivity context;
    List<AlertEntity> alertList;

    public AlertItemAdapter(AppCompatActivity context, List<AlertEntity> alertList){
        this.context = context;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_alert,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AlertEntity item = alertList.get(i);
        Log.i(TAG,"NAME: "+item.getName());

        String fullName = item.getName()+" ("+ item.getSymbol().toUpperCase()+") ";
        viewHolder.coinNameTv.setText(fullName);

        viewHolder.triggerPriceTv.setText(PriceFormatter.priceFormatterWithSymbol(context,item.getTriggerPrice(),item.getVsCurrency()));

        if(item.getImgUrl()!=null){
            Glide.with(context).load(item.getImgUrl()).into(viewHolder.coinImageView);
        }else{
            viewHolder.coinImageView.setVisibility(View.INVISIBLE);
        }

        int isRise = item.getRiseDrop();
        // if Rises Above
        if(isRise == 1) {
            viewHolder.alertTypeTv.setText(R.string.string_rise_above);
            viewHolder.alertTypeIv.setImageResource(R.mipmap.ic_up_arrow_green);
        }
        // if Drops below
        else if(isRise == 0){
            viewHolder.alertTypeTv.setText(R.string.string_drops_below);
            viewHolder.alertTypeIv.setImageResource(R.mipmap.ic_down_arrow_red);
        }

        int isTriggered = item.getIsTriggered();
        if(isTriggered==1)
            viewHolder.triggeredSwitch.setChecked(false);
        else
            viewHolder.triggeredSwitch.setChecked(true);

        viewHolder.triggeredSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateAlert(item,isChecked?0:1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView coinNameTv, alertTypeTv, triggerPriceTv;
        public CircleImageView coinImageView,alertTypeIv;
        public RelativeLayout backgroundRl, foregroundRl;
        public Switch triggeredSwitch;

        public ViewHolder(@NonNull View view) {
            super(view);

            coinNameTv = view.findViewById(R.id.item_alert_coin_name_tv);
            alertTypeTv = view.findViewById(R.id.item_alert_alert_type_tv);
            triggerPriceTv = view.findViewById(R.id.item_alert_trigger_price_tv);
            coinImageView = view.findViewById(R.id.item_alert_coin_iv);
            alertTypeIv = view.findViewById(R.id.item_alert_alert_type_iv);
            backgroundRl = view.findViewById(R.id.item_alert_background);
            foregroundRl = view.findViewById(R.id.item_alert_foreground);
            triggeredSwitch = view.findViewById(R.id.item_alert_switch);
        }
    }


    public void removeItem(int position){
        alertList.remove(position);

        notifyItemRemoved(position);
    }

    public void restoreItem(AlertEntity item,int position){
        alertList.add(position,item);

        notifyItemInserted(position);
    }


    private void updateAlert(AlertEntity alertEntity, int isTriggered){
        alertEntity.setIsTriggered(isTriggered);
        AlertViewModel alertViewModel = ViewModelProviders.of(context).get(AlertViewModel.class);
        alertViewModel.insert(alertEntity);
    }
}