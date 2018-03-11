package com.localli.deepak.cryptotips.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.localli.deepak.cryptotips.Objects.AlertItem;
import com.localli.deepak.cryptotips.R;

import java.util.List;

/**
 * Created by Deepak Prasad on 19-01-2018.
 */

public class AlertItemAdapter extends ArrayAdapter<AlertItem> {

    public AlertItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AlertItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_alerts,parent,false);

        TextView dateTv=(TextView)convertView.findViewById(R.id.item_alerts_date_tv),
                timeTv=(TextView)convertView.findViewById(R.id.item_alerts_time_tv),
                bodyTV=(TextView)convertView.findViewById(R.id.item_alerts_body_tv);
        ImageView alertIv=(ImageView)convertView.findViewById(R.id.item_alerts_alert_circle_iv);

        AlertItem alert=getItem(position);

        String date=alert.getDate(),
                time=alert.getTime(),
                body=alert.getBody1(),
                alertColor=alert.getColor();
        String color;
        if(date!=null)
            dateTv.setText(date);
        if(time!=null)
            timeTv.setText(time);
        if(body!=null)
            bodyTV.setText(body);
        if(alertColor!=null){
            if(alertColor.equalsIgnoreCase("green"))
                color="#00c853";
            else if(alertColor.equalsIgnoreCase("red"))
                color="#d50000";
            else
                color="#ff6d00";
        }else
            color="#ff6d00";
        alertIv.setColorFilter(Color.parseColor(color));


        return convertView;
    }
}
