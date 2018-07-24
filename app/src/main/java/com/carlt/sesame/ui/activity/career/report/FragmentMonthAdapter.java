package com.carlt.sesame.ui.activity.career.report;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment1;
import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment2;
import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment3;
import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment4;
import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment5;
import com.carlt.sesame.ui.activity.career.report.month.ReportMonthFragment6;

import java.util.ArrayList;

/**
 * 作者：秋良
 * 
 * 时间：2014-5-29
 * 
 * 描述：次类是月报的模板
 */
public class FragmentMonthAdapter extends FragmentPagerAdapter {

	private ArrayList<BaseFragment> mFragmentList;

	public FragmentMonthAdapter(FragmentManager fm) {
		super(fm);
		// 此处把所有fragment加载进去
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new ReportMonthFragment1());
		mFragmentList.add(new ReportMonthFragment2());
		mFragmentList.add(new ReportMonthFragment3());
		mFragmentList.add(new ReportMonthFragment4());
		mFragmentList.add(new ReportMonthFragment5());
		mFragmentList.add(new ReportMonthFragment6());

	}

	public void LoadData(Object mReportMonthInfo) {

		for (int i = 0; i < mFragmentList.size(); i++) {
			mFragmentList.get(i).setData(mReportMonthInfo);
		}

	}

	@Override
	public Fragment getItem(int position) {

		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	public void refreshImage(String url, Bitmap mBitmap) {
		mFragmentList.get(0).refreshImage(url,  mBitmap);
	}

}