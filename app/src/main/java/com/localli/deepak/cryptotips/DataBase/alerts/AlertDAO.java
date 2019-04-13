package com.localli.deepak.cryptotips.DataBase.alerts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

@Dao
public interface AlertDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlertEntity entity);

    @Delete
    void delete(AlertEntity entity);

    @Query("SELECT * FROM alerts_table")
    LiveData<List<AlertEntity>> getAlerts();

    @Query("SELECT * FROM alerts_table WHERE is_triggered = 0")
    //LiveData<List<AlertEntity>> getActiveAlerts();
    List<AlertEntity> getActiveAlerts();
}
