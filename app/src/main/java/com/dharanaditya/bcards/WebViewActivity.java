package com.dharanaditya.bcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.dharanaditya.bcards.api.LinkedinWebClient;

public class WebViewActivity extends AppCompatActivity implements LinkedinWebClient.OnTokenFetchListner {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    private WebView webView;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.wv_login_page);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_webpage_loading);
        // Setup web client
        // get instance of websittig and enable js
        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        LinkedinWebClient linkedinWebClient = new LinkedinWebClient(progressBar, this);
        webView.setWebViewClient(linkedinWebClient);
        // get URL from Intent and load the webpage in webview
        if (getIntent() != null && getIntent().getData() != null) {
            webView.loadUrl(getIntent().getData().toString());
        }
    }

    // clear session data to ensure end user security and new login prompt every time the user add bcard
    private void clearSessionData() {
        webView.clearHistory();
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                Log.i(TAG, "Removed all session cookies = [" + aBoolean + "]");
            }
        });
    }

    @Override
    public void onTokenFetch(Uri data) {
        clearSessionData();
        Intent result = new Intent();
        result.setData(data);
        setResult(RESULT_OK, result);
        finish();
    }
}
