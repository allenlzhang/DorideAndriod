package com.carlt.sesame.ui.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carlt.doride.R;
import com.carlt.sesame.http.AsyncImageLoader;

public class BaseImgDetialView extends LinearLayout {

	protected Context mContext;

	private View mLoadingLayout;

	// private View mLoadingBar;

	private boolean hasData = false;

	private AsyncImageLoader mLoader;

	private String imgUrl;

	private ImageView mImageView;

	public BaseImgDetialView(Context context, String imgUrl) {
		super(context);
		this.imgUrl = imgUrl;
		mContext = context;
		mLoader = AsyncImageLoader.getInstance();
		LayoutInflater.from(context).inflate(R.layout.img_detial_lay, this,
				true);
		init();
	}

	public BaseImgDetialView(Context context, String imgUrl, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.img_detial_lay, this,
				true);
		init();

	}

	protected void init() {
		mLoadingLayout = findViewById(R.id.img_detial_loading_lay);
		// mLoadingBar = findViewById(R.id.img_detial_loading_bar);
		// mMainLayout.addView(mMainView);
		mImageView = (ImageView) findViewById(R.id.img_detial_img);
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	protected void LoadSuccess(Bitmap data) {
		mImageView.setImageBitmap(data);
		hasData = true;
		mLoadingLayout.setVisibility(View.GONE);

	}

	public void OnShow() {
		if (!hasData) {
			Bitmap b = mLoader.getBitmapByUrl(imgUrl);
			if (b != null) {
				LoadSuccess(b);
			}

			// 拉取图片

		}
	}

	public void OnImgFinisher(String url, Bitmap mBitmap) {
		LoadSuccess(mBitmap);

	}
}
