package com.carlt.sesame.ui.upload;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

public class ImgDetialPagerAdapter extends PagerAdapter {
	private ArrayList<BaseImgDetialView> views;

	public ArrayList<BaseImgDetialView> getViews() {
		return views;
	}

	public ImgDetialPagerAdapter(ArrayList<BaseImgDetialView> views) {
		this.views = views;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1));
		return views.get(arg1);

	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {

		return views.size();

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return (arg0 == arg1);

	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}