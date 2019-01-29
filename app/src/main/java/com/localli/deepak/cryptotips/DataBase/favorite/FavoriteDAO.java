package com.localli.deepak.cryptotips.DataBase.favorite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */
@Dao
public interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity entity);

    @Query("DELETE FROM fav_table")
    void deleteAll();

    @Delete
    void delete(FavoriteEntity favoriteEntity);

    @Query("SELECT * FROM fav_table ORDER BY id ASC")
    List<FavoriteEntity> getAllFav();

}
