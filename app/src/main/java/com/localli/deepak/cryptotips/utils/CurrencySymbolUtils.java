package com.localli.deepak.cryptotips.utils;

import android.content.Context;

import com.localli.deepak.cryptotips.R;

/**
 * Created by Deepak Prasad on 02-01-2019.
 */

public class CurrencySymbolUtils {


    public static String getCurrencySymbol(Context context, String currency){

        String[] supportedCurrencies= context.getResources().getStringArray(R.array.currencies);
        String[] symbols = context.getResources().getStringArray(R.array.currency_symbols);
        int i;
        for(i=0;i<supportedCurrencies.length;i++){
            if(supportedCurrencies[i].equalsIgnoreCase(currency))
                break;
        }

        return symbols[i];
    }
}
