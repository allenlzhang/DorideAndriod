package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportCalendarWeekInfo;

import java.util.ArrayList;

/**
 * 驾驶证等级Adapter
 * 
 * @author daisy
 */
public class ReportCalendarWeekAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<ReportCalendarWeekInfo> mList;
	private int color1;
	private int color2;

	public ReportCalendarWeekAdapter(Context context,
			ArrayList<ReportCalendarWeekInfo> mList) {
		mInflater = LayoutInflater.from(context);
		color1 = context.getResources().getColor(R.color.text_color_gray3);
		color2 = context.getResources().getColor(R.color.text_color_gray4);
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
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
		// Holder mHolder;
		// if (convertView == null) {
		// mHolder = new Holder();
		// convertView = mInflater.inflate(R.layout.list_item_calendar_week,
		// null);
		// convertView.setTag(mHolder);
		// mHolder.mTextView1 = (TextView)convertView
		// .findViewById(R.id.list_item_calendar_week_txt1);
		// mHolder.mTextView2 = (TextView)convertView
		// .findViewById(R.id.list_item_calendar_week_txt2);
		// mHolder.mTextView3 = (TextView)convertView
		// .findViewById(R.id.list_item_calendar_week_txt3);
		// mHolder.mTextView4 = (TextView)convertView
		// .findViewById(R.id.list_item_calendar_week_txt4);
		// mHolder.mTextView5 = (TextView)convertView
		// .findViewById(R.id.list_item_calendar_week_txt5);
		// } else {
		// mHolder = (Holder)convertView.getTag();
		// }
		// ReportCalendarWeekInfo info = mList.get(position);
		// if (info.getStartDay() != null && !info.getStartDay().equals("")) {
		// mHolder.mTextView1.setText(info.getStartDay().split("-")[1]);
		// mHolder.mTextView2.setText(info.getStartDay().split("-")[2]);
		// }
		// if (info.getEndDay() != null && !info.getEndDay().equals("")) {
		// mHolder.mTextView3.setText(info.getEndDay().split("-")[1]);
		// mHolder.mTextView4.setText(info.getEndDay().split("-")[2]);
		// }
		// if (info.getPoint() != null) {
		// mHolder.mTextView5.setText(info.getPoint() + "分");
		// }

		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_calendar_week2,
					null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_calendar_week_txt1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.list_item_calendar_week_txt2);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		ReportCalendarWeekInfo info = mList.get(position);

		StringBuffer mStringBuffer = new StringBuffer();
		String startDay = info.getStartDay();
		String endDay = info.getEndDay();
		if (startDay != null && !startDay.equals("")) {
			String[] strings = startDay.split("-");
			if (strings.length > 2) {
				mStringBuffer.append(strings[1]);
				mStringBuffer.append("月");
				mStringBuffer.append(strings[2]);
				mStringBuffer.append("日");
			}
		}
		if (endDay != null && !endDay.equals("")) {
			String[] strings = endDay.split("-");
			if (strings.length > 2) {
				mStringBuffer.append("--");
				mStringBuffer.append(strings[1]);
				mStringBuffer.append("月");
				mStringBuffer.append(strings[2]);
				mStringBuffer.append("日");
			}
		}
		if (mStringBuffer.toString() != null
				&& mStringBuffer.toString().length() > 0) {
			mHolder.mTextView1.setText(mStringBuffer.toString());
		} else {
			mHolder.mTextView1.setText("月日--月日");
		}
		String ponit = info.getPoint();
		if (ponit == null || ponit.equals("")) {
			mHolder.mTextView1.setTextColor(color2);
			mHolder.mTextView2.setText("");
		} else if (ponit.equals("0")) {
			mHolder.mTextView1.setTextColor(color1);
			mHolder.mTextView2.setText("--");
		} else {
			mHolder.mTextView1.setTextColor(color1);
			mHolder.mTextView2.setText(info.getPoint() + "分");
		}
		mHolder.mTextView2.setTextColor(info.getPointColor());
		return convertView;
	}

	// class Holder {
	// private TextView mTextView1;// 周起始月
	//
	// private TextView mTextView2;// 周起始日
	//
	// private TextView mTextView3;// 周结束月
	//
	// private TextView mTextView4;// 周结束日
	//
	// private TextView mTextView5;// 得分
	// }

	class Holder {
		private TextView mTextView1;// 周起始日期

		private TextView mTextView2;// 得分
	}

}
