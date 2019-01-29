package com.localli.deepak.cryptotips.DataBase.portfolio;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
