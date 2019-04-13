package com.localli.deepak.cryptotips.formatters;

import java.text.SimpleDateFormat;

/**
 * Created by Deepak Prasad on 28-12-2018.
 */

public class DateFormatter {

    public static String TimeOfDayFormat(long timestamp){

        String selectedDate = new SimpleDateFormat("HH:mm:ss").format(timestamp);
        return  selectedDate;
    }

}
