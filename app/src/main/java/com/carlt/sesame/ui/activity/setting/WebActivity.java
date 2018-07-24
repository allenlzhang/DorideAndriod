
package com.carlt.sesame.ui.activity.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

/**
 * 展示网页内容Activity
 * 
 * @author daisy
 */
public class WebActivity extends BaseActivity implements DownloadListener{
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private WebView mWebView;

    public final static String URL_INFO = "url_info";

    private final static String URL_PROVISION = "http://m.cheler.com/domy.html";// 服务条款URL

    private final static String URL_INTRODUCE = "http://m.cheler.com";// 芝麻介绍URL
    

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        try {
            url = getIntent().getStringExtra(URL_INFO);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        back.setImageResource(R.drawable.arrow_back);

        if (url.equals(URL_PROVISION)) {
            title.setText("服务条款");
        } else if (url.equals(URL_INTRODUCE)) {
            title.setText("芝麻介绍");
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });

    }

    private void init() {
        mWebView = (WebView)findViewById(R.id.activity_web_webview);
        WebSettings mSettings=mWebView.getSettings();
//        mSettings.setJavaScriptEnabled(true);
        //mSettings.setPluginsEnabled(true);
        
        mSettings.setPluginState(WebSettings.PluginState.ON);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return true;
            }
        });

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(url);
        mWebView.setDownloadListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition,
            String mimetype, long contentLength) {
//        Uri uri=Uri.parse(url);
//        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//        startActivity(intent);
    }
}
