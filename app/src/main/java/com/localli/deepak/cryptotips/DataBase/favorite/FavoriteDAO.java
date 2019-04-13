package com.localli.deepak.cryptotips.DataBase.favorite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
