package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.CarLogInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 行车日志Adapter
 * 
 * @author daisy
 */
public class DayLogAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private AsyncImageLoader mAsyncImageLoader;

	private ArrayList<CarLogInfo> mDataList;

	public DayLogAdapter(Context context, ArrayList<CarLogInfo> dataList) {
		mInflater = LayoutInflater.from(context);
		this.mDataList = dataList;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
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
			convertView = mInflater.inflate(R.layout.list_item_report_day_log,
					null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt2);
			mHolder.mTextView3 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt3);
			mHolder.mTextView4 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt4);
			mHolder.mTextView5 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt5);
			mHolder.mTextView6 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt6);
			mHolder.mTextView7 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt7);
			mHolder.mTextView8 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt8);
			mHolder.mTextView9 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt9);
			mHolder.mTextView10 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt10);
			mHolder.mTextView11 = (TextView) convertView
					.findViewById(R.id.list_item_report_day_txt11);

		} else {
			mHolder = (Holder) convertView.getTag();
		}
		CarLogInfo info = mDataList.get(position);

		if (info.getPoint() != null) {
			mHolder.mTextView1.setText(info.getPoint() + "分");
		}

		if (info.getStarttime() != null && info.getStopTime() != null
				&& info.getTime() != null) {
			StringBuffer mStringBuffer = new StringBuffer();
			if (info.getStarttime().split(" ").length > 0) {
				mStringBuffer.append(info.getStarttime().split(" ")[0]);
			}
			mStringBuffer.append("至");
			if (info.getStopTime().split(" ").length > 0) {
				mStringBuffer.append(info.getStopTime().split(" ")[0] + "(");
			}
			mStringBuffer.append(info.getTime() + "分钟)");

			mHolder.mTextView2.setText(mStringBuffer.toString());
		}

		if (info.getMiles() != null) {
			mHolder.mTextView3.setText(info.getMiles());
		}

		if (info.getBrake() != null) {
			mHolder.mTextView4.setText("急刹车："+info.getBrake());
		}

		if (info.getTurn() != null) {
			mHolder.mTextView5.setText("急转弯："+info.getTurn());
		}

		if (info.getSpeedup() != null) {
			mHolder.mTextView6.setText("急加速："+info.getSpeedup());
		}
		if (info.getOverspeed() != null) {
			mHolder.mTextView7.setText("高转速："+info.getOverspeed());
		}
		
		if (info.getFuel() != null) {
            mHolder.mTextView8.setText(info.getFuel());
        }

        if (info.getAvgfuel() != null) {
            mHolder.mTextView9.setText(info.getAvgfuel() + "升/百公里");
        }

        if (info.getAvgspeed() != null) {
            mHolder.mTextView10.setText(info.getAvgspeed() + "公里/小时");
        }

        if (info.getMaxspeed() != null) {
            mHolder.mTextView11.setText(info.getMaxspeed() + "公里/小时");
        }

		return convertView;
	}

	class Holder {
		private TextView mTextView1;// 驾驶得分

		private TextView mTextView2;// 驾驶时间段

		private TextView mTextView3;// 行驶里程

		private TextView mTextView4;// 急刹车

		private TextView mTextView5;// 急拐弯

		private TextView mTextView6;// 急加速

		private TextView mTextView7;// 高转速
		
		private TextView mTextView8;// 电耗

        private TextView mTextView9;// 百公里电耗

        private TextView mTextView10;// 均速

        private TextView mTextView11;// 最高速度
	}
}
