package com.localli.deepak.cryptotips.formatters;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Deepak Prasad on 27-12-2018.
 */

public class SupplyFormatter {

    static String SUPPLY_FORMAT="%,.1f";

    public static void setSupplyTextView(Context context, TextView textView, Double volume){
        textView.setText(String.format(SUPPLY_FORMAT,volume));
    }
}
