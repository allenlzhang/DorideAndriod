package com.carlt.sesame.ui.activity.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;

public abstract class BaseLoadingView extends LinearLayout {

	protected Context mContext;

	private RelativeLayout mMainLayout;

	private View mMainView;

	private View mLoadingLayout;

	private TextView mLoadingTextView;

	private View mLoadingBar;

	private boolean hasData = true;

	public BaseLoadingView(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.base_loading_view_lay,
				this, true);
	}

	public BaseLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.base_loading_view_lay,
				this, true);

	}

	//
	// @Override
	// protected void onResume() {
	// if (!hasData) {
	// LoadData();
	// }
	// super.onResume();
	// }

	protected void setContent(int layoutResID) {
		mMainLayout = (RelativeLayout) findViewById(R.id.loading_view_mainlayout);
		mLoadingLayout = findViewById(R.id.loading_view_loading_lay);
		mLoadingTextView = (TextView) findViewById(R.id.loading_view_loading_text);
		mLoadingBar = findViewById(R.id.loading_view_loading_bar);
		mMainView = LayoutInflater.from(mContext).inflate(layoutResID, null);
		mMainLayout.addView(mMainView, 0);
		// mMainLayout.addView(mMainView);
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	protected void LoadSuccess(Object data) {
		hasData = true;
		mLoadingLayout.setVisibility(View.GONE);

	}

	protected void LoadErro(Object erro) {
		BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) erro;
		hasData = false;
		if (null != mBaseResponseInfo && null != mBaseResponseInfo.getInfo()) {
			mLoadingTextView.setText(mBaseResponseInfo.getInfo());
		} else {
			mLoadingTextView.setText("获取数据失败");
		}
		mLoadingBar.setVisibility(View.GONE);
		isRunning = false;

	}

	protected GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				LoadErro(msg.obj);
				break;
			case 1:
				LoadSuccess(msg.obj);
				break;

			default:
				break;
			}
		}
	};
	protected boolean isRunning = false;

	protected void LoadData() {
		isRunning = true;
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	public void OnShow() {
		if (!isRunning) {
			LoadData();
		}
	}

	public void onChanged() {

	}

	public void refreshImage(String url, Bitmap mBitmap) {

	}

	public void onShare() {

	}

}
