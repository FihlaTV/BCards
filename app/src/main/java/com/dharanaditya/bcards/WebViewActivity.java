package com.dharanaditya.bcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    private WebView webView;
    private WebSettings webSettings;
    private WebViewClient client;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.wv_login_page);
        // Clear previous session data
        clearSessionData();
        // Setup web client
        client = new WebViewClient();
        // get instance of websittig and enable js
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !url.contains("www.linkedin.com") && shouldOverrideUrlLoading(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                Log.d(TAG, "shouldOverrideUrlLoading: " + uri.getHost());
                if (request.getUrl().getHost().equals("www.linkedin.com")) {
                    return false;
                }
                return shouldOverrideUrlLoading(uri.toString());
            }

            private boolean shouldOverrideUrlLoading(final String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                clearSessionData();
                data = Uri.parse(url);
                Intent result = new Intent();
                result.setData(data);
                setResult(RESULT_OK, result);
                finish();
                return true;
            }
        });
        // get URL from Intent and load the webpage in webview
        if (getIntent() != null && getIntent().getData() != null) {
            webView.loadUrl(getIntent().getData().toString());
        }
    }

    // clear session data to ensure end user security and new login prompt every time the user add bcard
    private void clearSessionData() {
        webView.clearCache(true);
        webView.clearHistory();
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
