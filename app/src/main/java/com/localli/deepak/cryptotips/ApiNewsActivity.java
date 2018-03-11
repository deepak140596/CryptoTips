package com.localli.deepak.cryptotips;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ApiNewsActivity extends AppCompatActivity {
    WebView webView;
    String url;
    private AdView ad_ban_bottom;
    private InterstitialAd interstitialAd;
    SharedPreferences pref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_api_news);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        url=b.getString("key");
        pref=this.getSharedPreferences("show_ad_in_news",0);

        incrementReadCount();
        initialiseAds();
        initialiseViews();


        //progressDialog.dismiss();
    }
    public void initialiseViews(){
        webView=findViewById(R.id.api_news_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);
    }
    private void incrementReadCount(){
        SharedPreferences.Editor editor=pref.edit();
        int count=pref.getInt("read_news_count",0)+1;
        Log.e("NEWS READ COUNT1",count+"");
        editor.putInt("read_news_count",count);
        editor.apply();
    }
    private void initialiseAds(){
        ad_ban_bottom=findViewById(R.id.adView_activity_api_news_bottom);
        AdRequest adRequest=new AdRequest.Builder().build();
        ad_ban_bottom.loadAd(adRequest);


        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial_activity_news));
        adRequest=new AdRequest.Builder().build();

        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor=pref.edit();
        int count=pref.getInt("read_news_count",0);
        Log.e("NEWS READ COUNT",count+"");
        if(count>2) {
            if (interstitialAd.isLoaded())
                interstitialAd.show();
            else
                Log.e("AD ERROR LOG", "news interstitial ad wasn't loaded.");
            // this.adShown=true;
            editor.putInt("read_news_count",0);
            editor.apply();
        }
        finish();
        super.onBackPressed();

    }
}
