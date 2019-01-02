package com.localli.deepak.cryptotips.utils;

import com.localli.deepak.cryptotips.models.CoinItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by Deepak Prasad on 31-12-2018.
 */

public class SortUtils {

    public static int SORT_BY_NAME_AZ = 0,
            SORT_BY_NAME_ZA = 1,
            RANK_LH = 2,
            RANK_HL = 3,
            CHANGE_24H_HL = 4,
            CHANGE_24H_LH = 5,
            MKT_CAP_HL = 6,
            MKT_CAP_LH = 7,
            PRICE_HL = 8,
            PRICE_LH = 9,
            VOL_24H_HL = 10,
            VOL_24H_LH = 11,
            ATH_CHANGE_LH = 12,
            ATH_CHANGE_HL = 13;

    public static void sortCoins(List<CoinItem> coinList, int sortType){
        switch (sortType){
            // NAME AZ
            case 0:
                Collections.sort(coinList,CoinItem.compareByNameAsc);
                break;
            // NAME ZA
            case 1:
                Collections.sort(coinList,CoinItem.compareByNameDesc);
                break;
            // RANK_LH
            case 2:
                Collections.sort(coinList, CoinItem.compareByMarketCapHL);
                break;
            // RANK HL:
            case 3:
                Collections.sort(coinList,CoinItem.compareByMarketCapLH);
                break;
            case 4:
                Collections.sort(coinList,CoinItem.compareByChange24hrHL);
                break;
            case 5:
                Collections.sort(coinList,CoinItem.compareByChange24hrLH);
                break;
            case 6:
                Collections.sort(coinList,CoinItem.compareByMarketCapHL);
                break;
            case 7:
                Collections.sort(coinList,CoinItem.compareByMarketCapLH);
                break;
            case 8:
                Collections.sort(coinList,CoinItem.compareByPriceHL);
                break;
            case 9:
                Collections.sort(coinList,CoinItem.compareByPriceLH);
                break;
            case 10:
                Collections.sort(coinList,CoinItem.compareByVolumeHL);
                break;
            case 11:
                Collections.sort(coinList,CoinItem.compareByVolumeLH);
                break;
            case 12:
                Collections.sort(coinList,CoinItem.compareByATHChangeLH);
                break;
            case 13:
                Collections.sort(coinList,CoinItem.compareByATHChangeHL);
                break;

            // default RANK LH
            default:
                Collections.sort(coinList,CoinItem.compareByRankAsc);
                break;


        }
    }
}
