package com.carlt.doride.ui.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;


/**
 * 展示网页内容Activity
 * @author daisy
 */
public class ActivateHelpActivity extends BaseActivity implements DownloadListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private WebView webHelp;

    public final static String URL_INFO = "url_info";

    private final static String URL = "https://mp.weixin.qq.com/s/XraLB0NzR5a-VppUMTzQOw";// 激活帮助URL
    //    private final static String URL = "https://www.baidu.com";// 激活帮助URL


    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activate_help);
        try {
            url = URL;
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        title.setText("使用帮助");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webHelp.canGoBack()) {
                    webHelp.goBack();
                } else {
                    finish();
                }
            }
        });

    }

    private void init() {
        webHelp = (WebView) findViewById(R.id.webHelp);
        WebSettings mSettings = webHelp.getSettings();
        mSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setDomStorageEnabled(true);
        //        mSettings.setPluginState(WebSettings.PluginState.ON);

        webHelp.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showWaitingDialog(null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dissmissWaitingDialog();

            }
        });


        webHelp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webHelp.loadUrl(url);
        webHelp.setDownloadListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webHelp.destroy();
        webHelp = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webHelp.canGoBack()) {
            webHelp.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDownloadStart(String url, String userAgent,
                                String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
