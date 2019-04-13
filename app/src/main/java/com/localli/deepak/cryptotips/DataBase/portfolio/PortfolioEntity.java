package com.localli.deepak.cryptotips.DataBase.portfolio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Comparator;


/**
 * Created by Deepak Prasad on 20-01-2019.
 */

@Entity(tableName = "portfolio_table")
public class PortfolioEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "amt_of_coin")
    private float amt_of_coin;

    @ColumnInfo(name = "initial_price")
    private float initial_price;

    public PortfolioEntity(String id, String name, String symbol, float amt_of_coin, float initial_price){
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.amt_of_coin = amt_of_coin;
        this.initial_price = initial_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getAmt_of_coin() {
        return amt_of_coin;
    }

    public void setAmt_of_coin(float amt_of_coin) {
        this.amt_of_coin = amt_of_coin;
    }

    public float getInitial_price() {
        return initial_price;
    }

    public void setInitial_price(float initial_price) {
        this.initial_price = initial_price;
    }


    public static Comparator<PortfolioEntity> compareByNameAsc = new Comparator<PortfolioEntity>() {
        @Override
        public int compare(PortfolioEntity o1, PortfolioEntity o2) {
            if(o1.getName() == null)
                return 1;
            else if(o2.getName() == null)
                return -1;

            if(o1.getName().compareTo(o2.getName()) < 0)
                return -1;
            else
                return 1;
        }
    };
}
