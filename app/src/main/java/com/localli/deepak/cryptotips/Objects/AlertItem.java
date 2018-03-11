package com.localli.deepak.cryptotips.Objects;

/**
 * Created by Deepak Prasad on 19-01-2018.
 */

public class AlertItem {

    String alertId, time, date, year,color,body1,body2;

    public AlertItem(){}
    public AlertItem(String alertId,String time,String date,String year,String color,String bdoy1,String body2){
        this.alertId=alertId;
        this.time=time;
        this.date=date;
        this.year=year;
        this.color=color;
        this.body1=bdoy1;
        this.body2=body2;
    }
    public String getAlertId(){return alertId;}

    public String getColor() {
        return color;
    }

    public String getBody1() {
        return body1;
    }

    public String getBody2() {
        return body2;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getYear() {
        return year;
    }

}
