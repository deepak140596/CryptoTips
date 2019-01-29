package com.localli.deepak.cryptotips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.localli.deepak.cryptotips.DataBase.favorite.FavoriteEntity;
import com.localli.deepak.cryptotips.DataBase.favorite.FavoriteRepository;

import java.util.List;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */

public class FavoritesViewModel extends AndroidViewModel {

    private FavoriteRepository favoriteRepository;
    private List<FavoriteEntity> allFavorites;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
        allFavorites = favoriteRepository.getAllFavorites();
    }

    public List<FavoriteEntity> getAllFavorites(){
        return allFavorites;
    }

    public void insert(FavoriteEntity favoriteEntity){
        favoriteRepository.insert(favoriteEntity);
    }

    public void delete(FavoriteEntity favoriteEntity){
        favoriteRepository.delete(favoriteEntity);
    }
}
