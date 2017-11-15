package com.newhdc.pedergb.pozzum_20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
//import java.net.CookieManager;
import android.webkit.CookieManager;
import android.util.Log;
public class WebviewActivity extends AppCompatActivity {

    public static final String TAG = "kakebaker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(Globals.IP).split(";")[0];
        Log.e(TAG, cookies);

        WebView webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setDisplayZoomControls(false);
        //webSettings.setSupportZoom(true);
        //webSettings.setDefaultTextEncodingName("utf-8");
        webview.setWebChromeClient(new WebChromeClient());

        webview.loadUrl(Globals.IP + "/main.html");
    }
}
