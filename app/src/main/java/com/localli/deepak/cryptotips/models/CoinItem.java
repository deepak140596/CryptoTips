
package com.localli.deepak.cryptotips.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

public class CoinItem implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("current_price")
    @Expose
    private Double currentPrice;
    @SerializedName("market_cap")
    @Expose
    private Double marketCap;
    @SerializedName("market_cap_rank")
    @Expose
    private Integer marketCapRank;
    @SerializedName("total_volume")
    @Expose
    private Double totalVolume;
    @SerializedName("high_24h")
    @Expose
    private Double high24h;
    @SerializedName("low_24h")
    @Expose
    private Double low24h;
    @SerializedName("price_change_24h")
    @Expose
    private Double priceChange24h;
    @SerializedName("price_change_percentage_24h")
    @Expose
    private Double priceChangePercentage24h;
    @SerializedName("market_cap_change_24h")
    @Expose
    private Double marketCapChange24h;
    @SerializedName("market_cap_change_percentage_24h")
    @Expose
    private Double marketCapChangePercentage24h;
    @SerializedName("circulating_supply")
    @Expose
    private String circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Long totalSupply;
    @SerializedName("ath")
    @Expose
    private Double ath;
    @SerializedName("ath_change_percentage")
    @Expose
    private Double athChangePercentage;
    @SerializedName("ath_date")
    @Expose
    private String athDate;
    @SerializedName("roi")
    @Expose
    private Object roi;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @SerializedName("sparkline_in_7d")
    @Expose
    private SparklineIn7d sparklineIn7d;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(Integer marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(Double high24h) {
        this.high24h = high24h;
    }

    public Double getLow24h() {
        return low24h;
    }

    public void setLow24h(Double low24h) {
        this.low24h = low24h;
    }

    public Double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(Double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public Double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(Double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public Double getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(Double marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public Double getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(Double marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public String getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(String circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Long getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Long totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Double getAth() {
        return ath;
    }

    public void setAth(Double ath) {
        this.ath = ath;
    }

    public Double getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(Double athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public Object getRoi() {
        return roi;
    }

    public void setRoi(Object roi) {
        this.roi = roi;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public SparklineIn7d getSparklineIn7d() {
        return sparklineIn7d;
    }

    public void setSparklineIn7d(SparklineIn7d sparklineIn7d) {
        this.sparklineIn7d = sparklineIn7d;
    }

    public static Comparator<CoinItem> compareByRankAsc = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getMarketCapRank() == null)
                return 1;
            else if(o2.getMarketCapRank() == null)
                return -1;

            if(o1.getMarketCapRank()<o2.getMarketCapRank())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByRankDesc = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getMarketCapRank() == null)
                return 1;
            else if(o2.getMarketCapRank() == null)
                return -1;

            if(o1.getMarketCapRank()<o2.getMarketCapRank())
                return 1;
            else
                return -1;
        }
    };

    public static Comparator<CoinItem> compareByNameAsc = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
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

    public static Comparator<CoinItem> compareByNameDesc = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getName() == null)
                return 1;
            else if(o2.getName() == null)
                return -1;

            if(o1.getName().compareTo(o2.getName()) < 0)
                return 1;
            else
                return -1;
        }
    };

    public static Comparator<CoinItem> compareByChange24hrHL = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getPriceChangePercentage24h() == null)
                return 1;
            else if(o2.getPriceChangePercentage24h() == null)
                return -1;

            if(o1.getPriceChangePercentage24h()>o2.getPriceChangePercentage24h())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByChange24hrLH = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getPriceChangePercentage24h() == null)
                return 1;
            else if(o2.getPriceChangePercentage24h() == null)
                return -1;

            if(o1.getPriceChangePercentage24h()<o2.getPriceChangePercentage24h())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByMarketCapHL = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getMarketCap() == null)
                return 1;
            else if(o2.getMarketCap() == null)
                return -1;

            if(o1.getMarketCap() > o2.getMarketCap())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByMarketCapLH = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getMarketCap() == null)
                return 1;
            else if(o2.getMarketCap() == null)
                return -1;

            if(o1.getMarketCap() < o2.getMarketCap())
                return -1;
            else
                return 1;
        }
    };


    public static Comparator<CoinItem> compareByPriceHL = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getCurrentPrice() == null)
                return 1;
            else if(o2.getCurrentPrice() == null)
                return -1;

            if(o1.getCurrentPrice()>o2.getCurrentPrice())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByPriceLH = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getCurrentPrice() == null)
                return 1;
            else if(o2.getCurrentPrice() == null)
                return -1;

            if(o1.getCurrentPrice() < o2.getCurrentPrice())
                return -1;
            else
                return 1;
        }
    };
    public static Comparator<CoinItem> compareByVolumeHL = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getTotalVolume() == null)
                return 1;
            else if(o2.getTotalVolume() == null)
                return -1;

            if(o1.getTotalVolume() > o2.getTotalVolume())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByVolumeLH = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getTotalVolume() == null)
                return 1;
            else if(o2.getTotalVolume() == null)
                return -1;

            if(o1.getTotalVolume() < o2.getTotalVolume())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByATHChangeLH = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getAthChangePercentage() == null)
                return 1;
            else if(o2.getAthChangePercentage() == null)
                return -1;

            if(o1.getAthChangePercentage() < o2.getAthChangePercentage())
                return -1;
            else
                return 1;
        }
    };

    public static Comparator<CoinItem> compareByATHChangeHL = new Comparator<CoinItem>() {
        @Override
        public int compare(CoinItem o1, CoinItem o2) {
            if(o1.getAthChangePercentage() == null)
                return 1;
            else if(o2.getAthChangePercentage() == null)
                return -1;

            if(o1.getAthChangePercentage() > o2.getAthChangePercentage())
                return -1;
            else
                return 1;
        }
    };
}
