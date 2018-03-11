package com.localli.deepak.cryptotips.Objects;

import java.util.Comparator;

/**
 * Created by Deepak Prasad on 20-01-2018.
 */

public class TopCoinItem {
    String name;
    long rank;
    public TopCoinItem(){}
    public TopCoinItem(String name,long rank){
        this.name=name;
        this.rank=rank;
    }

    public String getName() {
        return name;
    }

    public long getRank() {
        return rank;
    }
}
