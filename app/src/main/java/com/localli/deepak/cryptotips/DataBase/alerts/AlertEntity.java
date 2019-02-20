package com.localli.deepak.cryptotips.DataBase.alerts;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

@Entity(tableName = "alerts_table")
public class AlertEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "alert_id")
    private long alertId;

    @ColumnInfo(name = "coin_id")
    private String coinId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "img_url")
    private String imgUrl;

    @ColumnInfo(name = "trigger_price")
    private double triggerPrice;

    @ColumnInfo(name = "initial_saved_price")
    private double initialSavedPrice;

    @ColumnInfo(name = "percentage_change")
    private double percentageChange;

    @ColumnInfo(name = "vs_currency")
    private String vsCurrency;

    @ColumnInfo(name = "rise_drop")
    private int riseDrop; // Rise = 1 Drop = 0

    @ColumnInfo(name = "is_triggered")
    private int isTriggered; // Yes = 1 No = 0

    public AlertEntity(long alertId,String coinId, String name, String symbol, String imgUrl,
                       double triggerPrice,double initialSavedPrice, double percentageChange,
                       String vsCurrency, int riseDrop, int isTriggered ){
        this.alertId = alertId;
        this.coinId = coinId;
        this.name = name;
        this.symbol = symbol;
        this.imgUrl = imgUrl;
        this.triggerPrice = triggerPrice;
        this.riseDrop = riseDrop;
        this.isTriggered = isTriggered;
        this.percentageChange = percentageChange;
        this.vsCurrency = vsCurrency;
        this.initialSavedPrice = initialSavedPrice;
    }

    @Override
    public String toString() {
        return "AlertEntity{" +
                "alertId=" + alertId +
                ", coinId='" + coinId + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", triggerPrice=" + triggerPrice +
                ", initialSavedPrice=" + initialSavedPrice +
                ", percentageChange=" + percentageChange +
                ", vsCurrency='" + vsCurrency + '\'' +
                ", riseDrop=" + riseDrop +
                ", isTriggered=" + isTriggered +
                '}';
    }

    @NonNull
    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(@NonNull long alertId) {
        this.alertId = alertId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(double triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public int getRiseDrop() {
        return riseDrop;
    }

    public void setRiseDrop(int riseDrop) {
        this.riseDrop = riseDrop;
    }

    public int getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(int isTriggered) {
        this.isTriggered = isTriggered;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public String getVsCurrency() {
        return vsCurrency;
    }

    public void setVsCurrency(String vsCurrency) {
        this.vsCurrency = vsCurrency;
    }

    public double getInitialSavedPrice() {
        return initialSavedPrice;
    }

    public void setInitialSavedPrice(double initialSavedPrice) {
        this.initialSavedPrice = initialSavedPrice;
    }
}
