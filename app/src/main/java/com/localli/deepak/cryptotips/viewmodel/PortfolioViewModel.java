package com.localli.deepak.cryptotips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.localli.deepak.cryptotips.DataBase.portfolio.PortfolioEntity;
import com.localli.deepak.cryptotips.DataBase.portfolio.PortfolioRepository;

import java.util.List;

/**
 * Created by Deepak Prasad on 20-01-2019.
 */

public class PortfolioViewModel extends AndroidViewModel {

    private PortfolioRepository mRepository;
    private LiveData<List<PortfolioEntity>> portfolio;

    public PortfolioViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PortfolioRepository(application);
        portfolio = mRepository.getPortfolio();
    }

    public LiveData<List<PortfolioEntity>> getPortfolio(){
        return portfolio;
    }

    public void insert(PortfolioEntity entity){
        mRepository.insert(entity);
    }

    public void delete(PortfolioEntity entity){
        mRepository.delete(entity);
    }
}
