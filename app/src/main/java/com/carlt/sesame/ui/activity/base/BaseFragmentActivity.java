package com.carlt.sesame.ui.activity.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.http.AsyncImageLoader.AsyncImageLoaderListener;

public class BaseFragmentActivity extends FragmentActivity implements
		AsyncImageLoaderListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityControl.addActivity(this);
		AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
		mAsyncImageLoader.setmListener(this);
	}

	@Override
	protected void onDestroy() {
		ActivityControl.removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
		mAsyncImageLoader.setmListener(this);
		Resources res = getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		
		super.onResume();
//		MobclickAgent.onResume(this);
	}
	
	

	@Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Log.e("info", "KYActivity");
	// ActivityControl.exit(this);
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

}
