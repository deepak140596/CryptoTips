package com.localli.deepak.cryptotips.DataBase.portfolio;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Deepak Prasad on 20-01-2019.
 */

@Dao
public interface PortfolioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PortfolioEntity entity);

    @Delete
    void delete(PortfolioEntity entity);

    @Query("SELECT * FROM portfolio_table" )
    LiveData<List<PortfolioEntity>> getPortfolio();
}
