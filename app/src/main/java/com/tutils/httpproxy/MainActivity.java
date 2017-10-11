package com.tutils.httpproxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntent = new Intent(this, HttpProxyService.class);
        startService(mIntent);

        loadWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);// 在退出Activity时停止该服务
    }

    private void loadWebView() {
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webView.getSettings().setDefaultFontSize(18);
        webView.loadUrl(getString(R.string.url_home));
    }
}
