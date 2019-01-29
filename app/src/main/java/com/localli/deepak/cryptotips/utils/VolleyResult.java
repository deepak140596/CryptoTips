package com.localli.deepak.cryptotips.utils;

import com.android.volley.VolleyError;

/**
 * Created by Deepak Prasad on 22-01-2019.
 */

public interface VolleyResult {
    public void notifySuccess(String requestType,String response);
    public void notifyError(String requestType, VolleyError error);
}
