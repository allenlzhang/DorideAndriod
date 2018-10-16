package com.carlt.sesame.ui.activity.career;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.utility.UUToast;

/**
 * 展示网页内容Activity
 * 
 * @author daisy
 */
public class MaintainLogDetialActivity extends BaseActivity {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private WebView mWebView;

	public final static String ID = "id";

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_web);
		try {
			id = getIntent().getStringExtra(ID);
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		init();

		CPControl.GetSecretaryByIdResult(id, listener);
	}

	GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String content = msg.obj.toString();

				mWebView.loadDataWithBaseURL(null, content, "text/html",
						"utf-8", null);
				break;

			case 1:
				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				if (mBaseResponseInfo != null
						&& mBaseResponseInfo.getInfo() != null) {
					UUToast.showUUToast(MaintainLogDetialActivity.this,
							mBaseResponseInfo.getInfo());
				}
				break;
			}
		}

	};

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		title.setText("养护详情");
		back.setImageResource(R.drawable.arrow_back);

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
		mWebView = (WebView) findViewById(R.id.activity_web_webview);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
