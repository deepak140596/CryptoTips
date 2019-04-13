package com.localli.deepak.cryptotips.news;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.localli.deepak.cryptotips.R;

public class WebViewActivity extends AppCompatActivity implements CustomWebChromeClient.ProgressListener{

    private WebView webView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        toolbar = findViewById(R.id.toolbar_webview);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webView = this.findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar_webview);

        progressBar.setMax(100);
        webView.setWebChromeClient(new CustomWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);
        progressBar.setProgress(0);
    }


    @Override
    public void onUpdateProgress(int progressValue) {
        progressBar.setProgress(progressValue);
        if(progressValue == 100)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                finish();
                return true;
        }
    }
}
