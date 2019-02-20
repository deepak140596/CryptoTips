package com.localli.deepak.cryptotips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertRepository;

import java.util.List;

/**
 * Created by Deepak Prasad on 25-01-2019.
 */

public class AlertViewModel extends AndroidViewModel {
    private AlertRepository alertRepository;
    private LiveData<List<AlertEntity>> alertsList;
    //private LiveData<List<AlertEntity>> activeAlertsList;
    private List<AlertEntity> activeAlertsList;

    public AlertViewModel(Application application){
        super(application);
        alertRepository = new AlertRepository(application);
        alertsList = alertRepository.getAlertsList();
        activeAlertsList = alertRepository.getActiveAlertsList();
    }

    public LiveData<List<AlertEntity>> getAlertsList(){
        return alertsList;
    }

    public List<AlertEntity> getActiveAlertsList(){
        return activeAlertsList;
    }

    public void insert(AlertEntity alertEntity){
        alertRepository.insert(alertEntity);
    }

    public void delete(AlertEntity alertEntity){
        alertRepository.delete(alertEntity);
    }
}
