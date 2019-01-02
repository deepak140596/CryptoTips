package com.localli.deepak.cryptotips.news;

import android.os.RecoverySystem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Deepak Prasad on 13-11-2018.
 */

public class CustomWebChromeClient extends WebChromeClient{
    private ProgressListener listener;

    public CustomWebChromeClient(ProgressListener listener){
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        this.listener.onUpdateProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

    public interface  ProgressListener{
        void onUpdateProgress(int progressValue);
    }
}
