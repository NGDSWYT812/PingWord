package com.ping.pingword.module.mine.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.utils.ToolBarUtils;

import java.io.IOException;

import ping.Lib.Utils.AssetsUtil;


public class AboutWebActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutwebview);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        ToolBarUtils.getSettor(this).setBackToolBar("关于&#38;帮助");
        webView = (WebView) findViewById(R.id.webview);
        webView.setBackgroundColor(0);
        webView.setWebChromeClient(new WebChromeClient() {
            // 扩充数据库的容量（在WebChromeClinet中实现）
            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
            }

            // 扩充缓存的容量
            @Override
            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }

            // 配置定位权限
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        try {
            String data = AssetsUtil.getStringFromAssets("about.html", this);
            webView.loadData(data, "text/html","UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}