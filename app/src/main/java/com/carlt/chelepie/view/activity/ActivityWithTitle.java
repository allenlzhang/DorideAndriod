package com.carlt.chelepie.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;


public abstract class ActivityWithTitle extends BaseActivity {

	private RelativeLayout mMainLayout;

	private View mMainView;

	private RelativeLayout mTitleLay;

	private boolean hasData = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.activity_with_title);
		mMainLayout = (RelativeLayout) findViewById(R.id.activity_with_title_mainlayout);
		mTitleLay = (RelativeLayout) findViewById(R.id.activity_with_title_title);
		mMainView = LayoutInflater.from(this).inflate(layoutResID, null);
		mMainLayout.addView(mMainView, 1);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				mMainView.getLayoutParams());
		int h = (int) getResources().getDimension(R.dimen.head_height) + 1;
		params.setMargins(0, h, 0, 0);
		mMainView.setLayoutParams(params);

	}

	public void setTitleView(int layoutResID) {
		mTitleLay.addView(LayoutInflater.from(this).inflate(layoutResID, null));
	}

	public void setTitleView(View title) {
		mTitleLay.addView(title);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

}
