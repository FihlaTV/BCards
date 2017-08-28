package com.dharanaditya.bcards.api;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by dharan1011 on 28/8/17.
 */

public class LinkedinWebClient extends WebViewClient {
    private final String TAG = LinkedinWebClient.class.getSimpleName();
    private ProgressBar progressBar;
    private OnTokenFetchListner tokenFetchListner;

    public LinkedinWebClient(ProgressBar progressBar, OnTokenFetchListner tokenFetchListner) {
        this.progressBar = progressBar;
        this.tokenFetchListner = tokenFetchListner;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
//                TODO Hack the logic -> it return false for all other site, but returns true when url is redirect url
        return !request.getUrl().getHost().equals("www.linkedin.com") && shouldOverrideUrlLoading(uri.toString());
    }

    private boolean shouldOverrideUrlLoading(final String url) {
        Log.d(TAG, "shouldOverrideUrlLoading: " + url);
        Uri data = Uri.parse(url);
        tokenFetchListner.onTokenFetch(data);
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
        progressBar.setVisibility(View.INVISIBLE);
    }

    public interface OnTokenFetchListner {
        void onTokenFetch(Uri data);
    }
}
