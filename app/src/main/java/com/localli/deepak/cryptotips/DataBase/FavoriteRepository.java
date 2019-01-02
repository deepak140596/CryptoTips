package com.localli.deepak.cryptotips.DataBase;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Deepak Prasad on 29-12-2018.
 */

public class FavoriteRepository {
    private FavoriteDAO favoriteDAO;
    private List<FavoriteEntity> allFavorites;

    FavoriteRepository(Application application){
        FavoriteDatabase db = FavoriteDatabase.getDatabase(application);
        favoriteDAO = db.favoriteDAO();
        allFavorites = favoriteDAO.getAllFav();
    }

    List<FavoriteEntity> getAllFavorites(){
        return allFavorites;
    }

    public void insert(FavoriteEntity favoriteEntity){
        new insertAsyncTask(favoriteDAO).execute(favoriteEntity);
    }

    public void delete(FavoriteEntity favoriteEntity){
        new deleteAsyncTask(favoriteDAO).execute(favoriteEntity);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteEntity,Void,Void>{
        private FavoriteDAO mAsyncTaskDao;

        insertAsyncTask(FavoriteDAO dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavoriteEntity... favoriteEntities) {
            mAsyncTaskDao.insert(favoriteEntities[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<FavoriteEntity,Void,Void>{
        private FavoriteDAO mAsyncTaskDao;

        deleteAsyncTask(FavoriteDAO dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavoriteEntity... favoriteEntities) {
            mAsyncTaskDao.delete(favoriteEntities[0]);
            return null;
        }
    }
}
