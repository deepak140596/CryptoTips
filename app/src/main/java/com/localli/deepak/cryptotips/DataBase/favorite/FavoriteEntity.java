package com.localli.deepak.cryptotips.DataBase.favorite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Created by Deepak Prasad on 29-12-2018.
 */
@Entity(tableName = "fav_table")
public class FavoriteEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public FavoriteEntity(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }


}
