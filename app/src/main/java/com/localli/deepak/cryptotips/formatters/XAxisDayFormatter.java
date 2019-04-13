package com.localli.deepak.cryptotips.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;

/**
 * Created by Deepak Prasad on 28-12-2018.
 */

public class XAxisDayFormatter implements IAxisValueFormatter {

    public static int HOUR_FORMAT = 1;
    public static int DAY_FORMAT = 2;
    private SimpleDateFormat simpleDateFormat;

    public XAxisDayFormatter(int i){
        if(i==HOUR_FORMAT)
            this.simpleDateFormat = new SimpleDateFormat("HH:mm");
        else if(i==DAY_FORMAT)
            this.simpleDateFormat = new SimpleDateFormat("MM/dd");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return simpleDateFormat.format(value);
    }
}
