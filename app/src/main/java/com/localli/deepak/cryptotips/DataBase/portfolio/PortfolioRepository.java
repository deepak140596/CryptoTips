package com.localli.deepak.cryptotips.DataBase.portfolio;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Deepak Prasad on 20-01-2019.
 */

public class PortfolioRepository {

    private PortfolioDAO portfolioDAO;
    private LiveData<List<PortfolioEntity>> portfolio;

    public PortfolioRepository(Application application){
        PortfolioDatabase db = PortfolioDatabase.getDatabase(application);
        portfolioDAO = db.portfolioDAO();
        portfolio = portfolioDAO.getPortfolio();
    }

    public LiveData<List<PortfolioEntity>> getPortfolio(){
        return portfolio;
    }

    public void insert(PortfolioEntity portfolioEntity){
        new insertAsyncTask(portfolioDAO).execute(portfolioEntity);
    }

    public void delete(PortfolioEntity portfolioEntity){
        new deleteAsyncTask(portfolioDAO).execute(portfolioEntity);
    }



    public static class insertAsyncTask extends AsyncTask<PortfolioEntity,Void,Void>{

        private PortfolioDAO mAsyncTaskDAO;
        insertAsyncTask(PortfolioDAO dao){
            mAsyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(PortfolioEntity... portfolioEntities) {
            mAsyncTaskDAO.insert(portfolioEntities[0]);
            return null;
        }
    }


    public static class deleteAsyncTask extends AsyncTask<PortfolioEntity,Void,Void>{
        private PortfolioDAO mAsyncTaskDao;
        deleteAsyncTask(PortfolioDAO dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(PortfolioEntity... portfolioEntities) {
            mAsyncTaskDao.delete(portfolioEntities[0]);
            return null;
        }
    }
}
