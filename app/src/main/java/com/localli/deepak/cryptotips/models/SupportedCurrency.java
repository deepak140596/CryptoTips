package com.localli.deepak.cryptotips.models;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Deepak Prasad on 22-01-2019.
 */



public class SupportedCurrency implements SearchSuggestion {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("name")
    @Expose
    private String name;


    public SupportedCurrency(Parcel source) {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBody() {
        return name+" ("+symbol.toUpperCase()+")";
    }

    public static final Creator<SupportedCurrency> CREATOR = new Creator<SupportedCurrency>() {
        @Override
        public SupportedCurrency createFromParcel(Parcel in) {
            return new SupportedCurrency(in);
        }

        @Override
        public SupportedCurrency[] newArray(int size) {
            return new SupportedCurrency[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}