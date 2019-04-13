package com.localli.deepak.cryptotips.DataBase.favorite;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;


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
