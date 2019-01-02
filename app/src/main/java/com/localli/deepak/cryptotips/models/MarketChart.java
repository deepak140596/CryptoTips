
package com.localli.deepak.cryptotips.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketChart implements Serializable
{

    @SerializedName("prices")
    @Expose
    private List<List<Double>> prices = null;
    @SerializedName("market_caps")
    @Expose
    private List<List<Double>> marketCaps = null;
    @SerializedName("total_volumes")
    @Expose
    private List<List<Double>> totalVolumes = null;
    private final static long serialVersionUID = 1581697518533496631L;

    public List<List<Double>> getPrices() {
        return prices;
    }

    public void setPrices(List<List<Double>> prices) {
        this.prices = prices;
    }

    public List<List<Double>> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<List<Double>> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<List<Double>> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<List<Double>> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }

}
