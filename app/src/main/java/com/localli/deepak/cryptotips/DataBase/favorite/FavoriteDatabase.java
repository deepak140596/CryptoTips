package com.localli.deepak.cryptotips.DataBase.favorite;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */


@Database(entities = {FavoriteEntity.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase{

    public abstract FavoriteDAO favoriteDAO();

    private static volatile FavoriteDatabase INSTANCE;

    static FavoriteDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (FavoriteDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDatabase.class,"favorite_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
