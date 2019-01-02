package com.localli.deepak.cryptotips.formatters;

import android.content.Context;
import android.widget.TextView;

import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.utils.CurrencySymbolUtils;

/**
 * Created by Deepak Prasad on 27-12-2018.
 */

public class PriceFormatter {

    static String PRICE_FORMAT_GREATER_THAN_ONE = "%s %,.3f";
    static String PRICE_FORMAT_LESS_THAN_ONE = "%s %.5f";

    public static void setPriceFormatTextView(Context context, TextView textView, Double price){
        String symbol = CurrencySymbolUtils.getCurrencySymbol(context,SharedPrefSimpleDB.getPreferredCurrency(context));

        if(Math.abs(price)>1){
            textView.setText(String.format(PRICE_FORMAT_GREATER_THAN_ONE,symbol,price));
        }
        else{
            textView.setText(String.format(PRICE_FORMAT_LESS_THAN_ONE,symbol,price));
        }
    }

    public static String priceFormatter(Context context, Double price){
        String symbol = CurrencySymbolUtils.getCurrencySymbol(context,SharedPrefSimpleDB.getPreferredCurrency(context));

        if(Math.abs(price)>1){
            return String.format(PRICE_FORMAT_GREATER_THAN_ONE,symbol, price);
        }
        else{
            return String.format(PRICE_FORMAT_LESS_THAN_ONE,symbol,price );
        }
    }
}
