package com.localli.deepak.cryptotips.rest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.utils.VolleyResult;

import java.util.Arrays;

/**
 * Created by Deepak Prasad on 22-01-2019.
 */

public class VolleyService {

    String TAG = "VOLLEY_SERVICE";

    VolleyResult volleyResultCallback = null;
    Context context;

    public VolleyService(VolleyResult volleyResultCallback, Context context){
        this.volleyResultCallback = volleyResultCallback;
        this.context = context;

    }

    public void getDataVolley(final String requestType, String URL){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i(TAG, response);
                    if(volleyResultCallback != null)
                        volleyResultCallback.notifySuccess(requestType,response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                    if(volleyResultCallback != null)
                        volleyResultCallback.notifyError(requestType,error);
                }
            });
            requestQueue.add(stringRequest);
        }catch (Exception e){}
    }

    public void getSimplePriceDataVolley(final String requestType, String URL, final AlertEntity alertEntity){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i(TAG, response);
                    if(volleyResultCallback != null)
                        volleyResultCallback.notifySuccess(requestType,response,alertEntity);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                    if(volleyResultCallback != null)
                        volleyResultCallback.notifyError(requestType,error);
                }
            });
            requestQueue.add(stringRequest);
        }catch (Exception e){}
    }

}

