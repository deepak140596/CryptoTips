package com.localli.deepak.cryptotips.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.localli.deepak.cryptotips.utils.SortUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Deepak Prasad on 30-12-2018.
 */

public class SharedPrefSimpleDB {

    static String TAG = "SHARED_PREF_SIMPLE_DB";


    public static boolean checkForFavorite(Context context,String id){
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //String savedFavorites = sharedPreferences.getString(context.getString(R.string.saved_favorites),context.getString(R.string.default_saved_coins));

        //Set<String> setSavedFav = sharedPreferences.getStringSet("saved_fav_set",null);
        Set<String> setSavedFav = getAllFavorites(context);
        if(setSavedFav!=null){
            Log.v(TAG,"SAVED ID: "+setSavedFav);
            if(setSavedFav.contains(id))
                return true;
        }
        return false;
    }

    public static Set<String> getAllFavorites(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> setSavedFav = sharedPreferences.getStringSet("saved_fav_set",null);

        return setSavedFav;
    }

    public static void insertFavoriteToDB(Context context,String id){
        Log.i(TAG,"called insert "+id);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String favorites = sharedPreferences.getString(context.getString(R.string.saved_favorites),context.getString(R.string.default_saved_coins));
        Set<String> setSavedFav = sharedPreferences.getStringSet("saved_fav_set",null);

        if(setSavedFav!=null){
            if(setSavedFav.contains(id)) {
                editor.apply();
                editor.commit();
                return;
            }

        }
        if(setSavedFav == null)
            setSavedFav = new HashSet<>();

        setSavedFav.add(id);

        editor.clear();

        editor.remove("saved_fav_set");
        editor.putStringSet("saved_fav_set",setSavedFav);

        editor.apply();
        editor.commit();

    }

    public static void deleteFavoriteFromDB(Context context,String id){
        Log.i(TAG,"called delete "+id);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> setSavedFav = sharedPreferences.getStringSet("saved_fav_set",null);

        if(setSavedFav!=null){
            if(!setSavedFav.contains(id)) {
                editor.apply();
                editor.commit();
                return;
            }

        }

        if(setSavedFav == null)
            return;

        setSavedFav.remove(id);

        editor.clear();
        editor.remove("saved_fav_set");
        editor.putStringSet("saved_fav_set",setSavedFav);

        editor.apply();
        editor.commit();
    }

    public static int getSortMethod(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sort_settings",Context.MODE_PRIVATE);
        int sortMethod= sharedPreferences.getInt("saved_sort_method", SortUtils.RANK_LH);
        Log.v(TAG,"Saved Sort method: "+sortMethod);
        return sortMethod;
    }

    public static void saveSortMethod(Context context,int sortMethod){
        SharedPreferences sharedPreferences = context.getSharedPreferences("sort_settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().putInt("saved_sort_method",sortMethod).apply();
        editor.commit();

    }

    public static String getPreferredCurrency(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferred_currency",Context.MODE_PRIVATE);
        String prefCurrency = sharedPreferences.getString("saved_pref_currency","usd");
        Log.v(TAG,"Saved Preferred Currency: "+prefCurrency);
        return prefCurrency;
    }

    public static  void savePreferredCurrency(Context context, String prefCurrency){
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferred_currency",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().putString("saved_pref_currency",prefCurrency.toLowerCase());
        editor.apply();
        editor.commit();
    }


    public static int getNoOfCoins(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("no_of_coins",Context.MODE_PRIVATE);
        int noOfCoins = sharedPreferences.getInt("saved_no_of_coins",100);
        return noOfCoins;
    }

    public static void saveNoOfCoins(Context context,int noOfCoins){
        SharedPreferences sharedPreferences = context.getSharedPreferences("no_of_coins",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().putInt("saved_no_of_coins",noOfCoins);
        editor.apply();
        editor.commit();
    }
}
