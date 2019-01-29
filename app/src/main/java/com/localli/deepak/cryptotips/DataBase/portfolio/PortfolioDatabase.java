package com.localli.deepak.cryptotips.DataBase.portfolio;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.localli.deepak.cryptotips.DataBase.favorite.FavoriteDatabase;

/**
 * Created by Deepak Prasad on 20-01-2019.
 */

@Database(entities = {PortfolioEntity.class}, version = 1,exportSchema = false)
public abstract class PortfolioDatabase extends RoomDatabase {

    public abstract PortfolioDAO portfolioDAO();

    private static volatile PortfolioDatabase INSTANCE;

    static PortfolioDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (FavoriteDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PortfolioDatabase.class,"portfolio_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private PortfolioDAO dao;
        private PopulateDbAsyncTask(PortfolioDatabase db){
            dao = db.portfolioDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // set insert statements or do any work after creating new database
            return null;
        }
    }

}
