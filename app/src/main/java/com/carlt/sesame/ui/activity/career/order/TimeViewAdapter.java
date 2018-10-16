package com.carlt.sesame.ui.activity.career.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.career.DayOrderStateInfo;

import java.util.ArrayList;

public class TimeViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<DayOrderStateInfo> mDayOrderStateInfoList;

	public TimeViewAdapter(Context context,
			ArrayList<DayOrderStateInfo> mDayOrderStateInfoList) {
		mInflater = LayoutInflater.from(context);
		this.mDayOrderStateInfoList = mDayOrderStateInfoList;
	}

	@Override
	public int getCount() {
		return mDayOrderStateInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_time_view, null);
			convertView.setTag(mHolder);
			mHolder.mJT = (ImageView) convertView
					.findViewById(R.id.list_item_time_view_jt);
			mHolder.mDian = (ImageView) convertView
					.findViewById(R.id.list_item_time_view_dian);
			mHolder.mText = (TextView) convertView
					.findViewById(R.id.list_item_time_view_text);
			mHolder.mLay = convertView
					.findViewById(R.id.list_item_time_view_lay);

		} else {
			mHolder = (Holder) convertView.getTag();
		}
		DayOrderStateInfo info = mDayOrderStateInfoList.get(position);

		if (info.isFlag()) {
			mHolder.mJT.setVisibility(View.GONE);
			mHolder.mDian.setVisibility(View.GONE);
			mHolder.mLay.setBackgroundColor(Color.TRANSPARENT);
		} else {
			mHolder.mDian.setVisibility(View.VISIBLE);
			if (info.getTotal() - info.getUsed() > 0) {

				mHolder.mDian.setImageResource(R.drawable.rili_day_state3);
				mHolder.mJT.setVisibility(View.VISIBLE);
				mHolder.mLay.setBackgroundResource(R.drawable.list_item_bg);
			} else {
				mHolder.mJT.setVisibility(View.GONE);
				mHolder.mDian.setImageResource(R.drawable.rili_day_state2);
				mHolder.mLay.setBackgroundColor(Color.TRANSPARENT);
			}
		}

		mHolder.mText.setText(info.getTime());
		return convertView;
	}

	class Holder {
		private ImageView mJT;
		private ImageView mDian;
		private TextView mText;
		private View mLay;
	}

}
