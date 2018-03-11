package com.localli.deepak.cryptotips.Objects;

/**
 * Created by Deepak Prasad on 19-01-2018.
 */

public class CoinItem {
    String id,url,imageUrl,name,symbol,coinName,fullName,totalCoinSupply,sortOrder,rank;

    public CoinItem(){}
    public CoinItem(String id,String url,String imageUrl,String name,String symbol,String coinName,String fullName,
                    String totalCoinSupply,String sortOrder,String rank){
        this.id=id;
        this.url=url;
        this.imageUrl=imageUrl;
        this.name=name;
        this.symbol=symbol;
        this.coinName=coinName;
        this.fullName=fullName;
        this.totalCoinSupply=totalCoinSupply;
        this.sortOrder=sortOrder;
        this.rank=rank;
    }
    public String getId(){return id;}

    public String getCoinName() {
        return coinName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getTotalCoinSupply() {
        return totalCoinSupply;
    }

    public String getUrl() {
        return url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }
}
