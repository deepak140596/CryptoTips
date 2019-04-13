package com.localli.deepak.cryptotips.DataBase.alerts;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.localli.deepak.cryptotips.DataBase.portfolio.PortfolioDatabase;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

@Database(entities = {AlertEntity.class}, version = 1, exportSchema = false)
public abstract class AlertDatabase extends RoomDatabase{

    public abstract AlertDAO alertDAO();

    private static volatile AlertDatabase INSTANCE;

    static AlertDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AlertDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlertDatabase.class,"alert_database")
                            .fallbackToDestructiveMigration()
                            //.addCallback(roomCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAlertDBAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateAlertDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private AlertDAO dao;
        private PopulateAlertDBAsyncTask(AlertDatabase db){
            dao = db.alertDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // do something here
            dao.insert(new AlertEntity(1,"bitcoin,","Bitcoin","btc",
                    "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                    3597.9051,3597.90510,1.1,"inr",1,0));

            dao.insert(new AlertEntity(2,"ripple,","XRP","xrp",
                    "https://assets.coingecko.com/coins/images/44/large/XRP.png?1547033685",
                    0.3147749,0.31477490,1.1,"inr",1,0));
            return null;

        }
    }
}
