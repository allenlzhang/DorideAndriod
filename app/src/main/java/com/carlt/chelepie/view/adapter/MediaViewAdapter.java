package com.carlt.chelepie.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.carlt.doride.base.BaseFragment;

import java.util.ArrayList;


public class MediaViewAdapter extends FragmentStatePagerAdapter {

	private ArrayList<BaseFragment> dataList;

	public MediaViewAdapter(FragmentManager fragmentManager,
                            ArrayList<BaseFragment> dataList) {
		super(fragmentManager);
		this.dataList = dataList;
	}

	@Override
	public Fragment getItem(int position) {
		if (dataList != null) {
			return dataList.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		if (dataList != null) {
			return dataList.size();
		}
		return 0;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);屏蔽掉super代码以解决不相邻的Tab切换时导致空白界面的问题
	}
	
}
