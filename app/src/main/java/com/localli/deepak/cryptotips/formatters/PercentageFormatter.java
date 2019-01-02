package com.localli.deepak.cryptotips.formatters;

import android.content.Context;
import android.widget.TextView;

import com.localli.deepak.cryptotips.R;

import org.w3c.dom.Text;

/**
 * Created by Deepak Prasad on 23-12-2018.
 */

public class PercentageFormatter {

    static int colorGreen = R.color.color_green;
    static int colorRed = R.color.color_red;
    static String POS_PCT_FORMAT = "+ %.2f";
    static String NEG_PCT_FORMAT = "%.2f";
    //static String PREFIX = "24hr: ";


    public static void setPercentChangeTextView(Context context, TextView textView, Double pctChange){

        if(pctChange >= 0){
            String text = String.format(POS_PCT_FORMAT,pctChange);
            textView.setText(text.concat("%"));
            textView.setTextColor(context.getResources().getColor(colorGreen));
        }else{
            String text = String.format(NEG_PCT_FORMAT,pctChange);
            textView.setText(text.concat("%"));
            textView.setTextColor(context.getResources().getColor(colorRed));
        }
    }

    public static void setPriceAndPercentChangeTextView(Context context, TextView textView, Double pctChange, Double priceChange){
        if(pctChange >= 0){
            String text = String.format(POS_PCT_FORMAT,pctChange);
            textView.setText(text.concat("%").concat(" (+"+PriceFormatter.priceFormatter(context,priceChange)+")"));
            textView.setTextColor(context.getResources().getColor(colorGreen));
        }else{
            String text = String.format(NEG_PCT_FORMAT,pctChange);
            textView.setText(text.concat("%").concat(" ("+PriceFormatter.priceFormatter(context,priceChange)+")"));
            textView.setTextColor(context.getResources().getColor(colorRed));
        }
    }


}
