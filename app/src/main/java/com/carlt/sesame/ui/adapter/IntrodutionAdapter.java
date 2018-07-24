
package com.carlt.sesame.ui.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * 引导页adapter
 * 
 * @author daisy
 */
public class IntrodutionAdapter extends PagerAdapter {

    private ArrayList<View> mViews;

    public IntrodutionAdapter(ArrayList<View> views) {
        mViews = views;
    }

    /**
     * 销毁arg1位置的界面
     */
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ViewPager mViewPager = (ViewPager)arg0;
        View mView = mViews.get(arg1);
        mViewPager.removeView(mView);

    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getCount() {
        if (mViews != null) {
            return mViews.size();
        }
        return 0;
    }

    /**
     * 初始化arg1位置的界面
     */
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ViewPager mViewPager = (ViewPager)arg0;
        View mView = mViews.get(arg1);
        mViewPager.addView(mView, 0);
        return mView;
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
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
