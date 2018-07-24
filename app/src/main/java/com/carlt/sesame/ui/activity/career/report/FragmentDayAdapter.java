package com.carlt.sesame.ui.activity.career.report;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carlt.sesame.ui.activity.career.report.day.ReportDayFragment1;
import com.carlt.sesame.ui.activity.career.report.day.ReportDayFragment2;
import com.carlt.sesame.ui.activity.career.report.day.ReportDayFragment3;

import java.util.ArrayList;

/**
 * 作者：秋良 时间：2014-5-29 描述：此类是日报Adapter
 */
public class FragmentDayAdapter extends FragmentPagerAdapter {

	private ArrayList<BaseFragment> mFragmentList;

	public FragmentDayAdapter(FragmentManager fm) {
		super(fm);
		// 此处把所有fragment加载进去
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new ReportDayFragment1());
		mFragmentList.add(new ReportDayFragment2());
		mFragmentList.add(new ReportDayFragment3());

	}

	public void LoadData(Object mReportDayInfo) {

		for (int i = 0; i < mFragmentList.size(); i++) {
			mFragmentList.get(i).setData(mReportDayInfo);
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
