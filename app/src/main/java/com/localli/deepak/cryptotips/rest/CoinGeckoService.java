package com.localli.deepak.cryptotips.rest;

/**
 * Created by Deepak Prasad on 23-12-2018.
 */

public class CoinGeckoService {

    public static String ALL_COINS_BY_PAGE_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&order=market_cap_desc&per_page=%s&page=%s";
    public static String COINS_BY_ID_URL="https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s";
    public static String SAMPLE_TEST_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=500&page=1";
    public static String MARKET_DATA_URL = "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=%s";
    public static String GET_SIMPLE_PRICE = "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s";
}
