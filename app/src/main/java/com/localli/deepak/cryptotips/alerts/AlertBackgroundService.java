package com.localli.deepak.cryptotips.alerts;

import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.DataBase.alerts.AlertEntity;
import com.localli.deepak.cryptotips.NavigationActivity;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.currencydetails.CurrencyDetailsTabsActivity;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.localli.deepak.cryptotips.rest.VolleyService;
import com.localli.deepak.cryptotips.utils.VolleyResult;
import com.localli.deepak.cryptotips.viewmodel.AlertViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Deepak Prasad on 30-01-2019.
 */

public class AlertBackgroundService extends IntentService {

    String TAG = "ALERT_BG_SERVICE";

    private Long DURATION = 1000 * 60l; // 60 seconds
    private Long DELAY = 1000l;
    private Timer timer = new Timer();

    int notificationId = 0;

    Application application;

    private AlertViewModel alertViewModel;
    VolleyResult volleyResultCallback = null;
    VolleyService volleyService;

    Gson gson;

    /**
     * A constructor is required, and must call the super <code><a href="/reference/android/app/IntentService.html#IntentService(java.lang.String)">IntentService(String)</a></code>
     * constructor with a name for the worker thread.
     */
    public AlertBackgroundService() {
        super("HelloIntentService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        /*try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }*/


    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = getApplication();
        // initialise alert view model


        // inisitialise a gson builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // your code here
                alertViewModel = new AlertViewModel(application);
                try {
                    initVolleyCallback();
                    new GetActiveAlertsAsyncTask(alertViewModel).execute();
                } catch (Exception e) {
                    e.printStackTrace();

                }

                //getActiveAlertsFromDB();
            }
        }, DELAY, DURATION);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }




    public class GetActiveAlertsAsyncTask extends AsyncTask<Void,Void,Void>{

        AlertViewModel alertViewModel;
        public GetActiveAlertsAsyncTask(AlertViewModel alertViewModel){
            this.alertViewModel = alertViewModel;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            List<AlertEntity> alertEntities =  alertViewModel.getActiveAlertsList();
            getActiveAlertsFromDB(alertEntities);
            return null;
        }
    }

    private void getActiveAlertsFromDB(List<AlertEntity> alertEntities){

        //List<AlertEntity> alertEntities =  alertViewModel.getActiveAlertsList();
        Log.i(TAG,"Alert size: "+alertEntities.size());
        for(AlertEntity entity : alertEntities){

            // generate url for every coin for details
            String GET_COIN_BY_ID_URL = String.format(CoinGeckoService.GET_SIMPLE_PRICE,
                    entity.getCoinId(),entity.getVsCurrency());

            // call volley service
            volleyService = new VolleyService(volleyResultCallback,application);
            volleyService.getSimplePriceDataVolley("GET",GET_COIN_BY_ID_URL,entity);
        }


    }

    void initVolleyCallback() throws Exception{
        volleyResultCallback = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, String response,AlertEntity alertEntity) {
                Log.i(TAG,"Alert Service Breathing!");
                Log.i(TAG,"Response: "+response);
                //Log.i(TAG,"COIN: "+alertEntity.getName()+" PRICE: "+alertEntity.getVsCurrency()+" "+
                  //      getCurrentPrice(response,alertEntity));

                double currentPrice = 0;
                try {
                    currentPrice = getCurrentPrice(response,alertEntity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(currentPrice <0 )
                    return;
                double triggerPrice = alertEntity.getTriggerPrice();
                int isRiseDrop = alertEntity.getRiseDrop(); // Rise = 1 Drop = 0

                if(isRiseDrop == 1){ // if current price rises above trigger price
                    if(currentPrice >= triggerPrice)
                        sendNotification(alertEntity);

                }else if(isRiseDrop == 0){  // if current price drops below the trigger price
                    if(currentPrice <= triggerPrice)
                        sendNotification(alertEntity);
                }

            }

            @Override
            public void notifySuccess(String requestType, String response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.e(TAG,"Error: "+error);
            }
        };
    }


    private double getCurrentPrice(String response, AlertEntity alertEntity) throws InterruptedException {

        try {
            JSONObject object = new JSONObject(response);
            JSONObject coinObject = object.getJSONObject(alertEntity.getCoinId());
            Double price = coinObject.getDouble(alertEntity.getVsCurrency());

            //Log.i(TAG,"Price: "+getPrice);
            return price;

        } catch (JSONException e) {
            e.printStackTrace();
            Thread.sleep(10000l);
            //onCreate();
        }

        return -9999999;
    }

    public void sendNotification(AlertEntity alertEntity){

        // pending intent when user clicks on the active notification
        Intent intent = new Intent(this,CurrencyDetailsTabsActivity.class);
        Log.i(TAG,"COIN_ID: "+alertEntity.getCoinId());
        intent.putExtra(getString(R.string.coin_id),alertEntity.getCoinId());
        intent.putExtra(getString(R.string.vs_currency),alertEntity.getVsCurrency());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,notificationId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(TAG,"Notification triggered! Coin: "+alertEntity.getName()+" PRICE: "+alertEntity.getVsCurrency()+" "+
        alertEntity.getTriggerPrice()+" RISE_DROP : "+alertEntity.getRiseDrop());

        // set rise drop alert text
        String riseDropAlert = (alertEntity.getRiseDrop()==1)? " rises above ": " drops below ";
        String title = alertEntity.getName()+" ("+alertEntity.getSymbol().toUpperCase()+") price alert!";
        String textContent = alertEntity.getName()+" ("+alertEntity.getSymbol().toUpperCase()+") "+
                riseDropAlert +
                PriceFormatter.priceFormatterWithSymbol(application,alertEntity.getTriggerPrice(),alertEntity.getVsCurrency());

        // select notification sound
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.slow_spring_board);

        // build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(application,NavigationActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_zigzag_arrow)
                .setContentTitle(title)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);
        notificationManager.notify(notificationId++,notificationBuilder.build());  // increment notification ID

        // after notification, update the isTriggeredValue of the alert in DB
        updateIsTriggeredToDB(alertEntity);

    }

    private void updateIsTriggeredToDB(AlertEntity alertEntity){
        alertEntity.setIsTriggered(1);
        alertViewModel.insert(alertEntity);
    }


}