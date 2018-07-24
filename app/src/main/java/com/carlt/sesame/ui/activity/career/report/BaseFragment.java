package com.carlt.sesame.ui.activity.career.report;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected abstract void LoadDate();

	public abstract void setData(Object o);

	public void refreshImage(String url, Bitmap mBitmap) {

	}


    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getName());
    }
	
    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }
	
}
