package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
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
public class TermsDeclareActivity extends BaseActivity implements DownloadListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private WebView declare_content;

    public final static String URL_INFO = "url_info";

    private final static String URL_PROVISION = "http://m.cheler.com/yema.html";// 服务条款URL

    private final static String URL_INTRODUCE = "http://m.cheler.com";// 大迈介绍URL

    private final static String URL_ZOTYE = "http://vip2.domyauto.com/api/servicestation.aspx";// 众泰售后服务页面

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_declare);
        try {
            url = getIntent().getStringExtra(URL_INFO);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);


        if (url.equals(URL_PROVISION)) {
            title.setText("服务条款");
        } else if (url.equals(URL_INTRODUCE)) {
            title.setText("大乘介绍");
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (declare_content.canGoBack()) {
                    declare_content.goBack();
                } else {
                    finish();
                }
            }
        });

    }

    private void init() {
        declare_content = (WebView) findViewById(R.id.declare_content);
        WebSettings mSettings = declare_content.getSettings();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setPluginState(WebSettings.PluginState.ON);

        declare_content.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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


        declare_content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        declare_content.loadUrl(url);
        declare_content.setDownloadListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && declare_content.canGoBack()) {
            declare_content.goBack();
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
