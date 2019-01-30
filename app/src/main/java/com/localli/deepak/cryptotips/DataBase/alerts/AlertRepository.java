package com.localli.deepak.cryptotips.DataBase.alerts;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

public class AlertRepository {

    private AlertDAO alertDAO;
    private LiveData<List<AlertEntity>> alertsList;
    private LiveData<List<AlertEntity>> activeAlertsList;

    public AlertRepository(Application application){
        AlertDatabase db = AlertDatabase.getDatabase(application);
        alertDAO = db.alertDAO();
        alertsList = alertDAO.getAlerts();
        activeAlertsList = alertDAO.getActiveAlerts();
    }

    public LiveData<List<AlertEntity>> getAlertsList(){
        return alertsList;
    }

    public LiveData<List<AlertEntity>> getActiveAlertsList(){
        return activeAlertsList;
    }

    public void insert(AlertEntity alertEntity){

        new insertAsyncTask(alertDAO).execute(alertEntity);
    }

    public void delete(AlertEntity alertEntity){
        new deleteAsyncTask(alertDAO).execute(alertEntity);
    }

    public static class insertAsyncTask extends AsyncTask<AlertEntity,Void,Void>{

        private AlertDAO mAlertDao;
        insertAsyncTask(AlertDAO dao){
            mAlertDao = dao;
        }

        @Override
        protected Void doInBackground(AlertEntity... alertEntities) {
            mAlertDao.insert(alertEntities[0]);
            return null;
        }
    }


    public static class deleteAsyncTask extends AsyncTask<AlertEntity,Void,Void>{

        private AlertDAO mAlertDao;
        deleteAsyncTask(AlertDAO dao){
            mAlertDao = dao;
        }

        @Override
        protected Void doInBackground(AlertEntity... alertEntities) {
            mAlertDao.delete(alertEntities[0]);
            return null;
        }
    }
}

