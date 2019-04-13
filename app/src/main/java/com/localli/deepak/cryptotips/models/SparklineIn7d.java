
package com.localli.deepak.cryptotips.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SparklineIn7d {

    @SerializedName("price")
    @Expose
    private List<Double> price = null;

    public List<Double> getPrice() {
        return price;
    }

    public void setPrice(List<Double> price) {
        this.price = price;
    }

}
