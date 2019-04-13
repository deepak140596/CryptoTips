package com.localli.deepak.cryptotips.utils;

import com.android.volley.VolleyError;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;

/**
 * Created by Deepak Prasad on 22-01-2019.
 */

public interface VolleyResult {
    public void notifySuccess(String requestType,String response);
    public void notifyError(String requestType, VolleyError error);

    public void notifySuccess(String requestType, String response, AlertEntity alertEntity);
}
