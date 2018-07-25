package com.carlt.sesame.ui.activity.career.report;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.career.ReportCalendarWeekInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 周报日历
 * 
 * @author daisy
 */
public class CalendarWeek extends MenuCalendar implements OnClickListener {
	private ListView mListView;

	private TextView Top_Date;// 月报日历的年份

	private ImageView mImageViewL;// 上一月

	private ImageView mImageViewR;// 下一月

	private View mLoadingLayout;

	private TextView mLoadingTextView;

	private View mLoadingBar;

	private Calendar today = Calendar.getInstance();

	private ArrayList<ReportCalendarWeekInfo> mList = new ArrayList<ReportCalendarWeekInfo>();


	private Context mContext;

	private OnCalendarWeekClick mOnCalendarWeekClick;

	public CalendarWeek(Context mContext,
			OnCalendarWeekClick mOnCalendarWeekClick) {
		super(mContext);
		this.mContext = mContext;
		this.mOnCalendarWeekClick = mOnCalendarWeekClick;
		View child = LayoutInflater.from(mContext).inflate(
				R.layout.layout_calendar_week, null);
		init(child);

		setTitle("选择周报日期");
		// 声明控件，并绑定事件
		Top_Date = (TextView) child.findViewById(R.id.head_calender_txt);
		mImageViewL = (ImageView) child.findViewById(R.id.head_calender_img1);
		mImageViewR = (ImageView) child.findViewById(R.id.head_calender_img2);
		mImageViewL.setOnClickListener(this);
		mImageViewR.setOnClickListener(this);
		// Loading控件
		mLoadingLayout = child.findViewById(R.id.loading_activity_mainlayout);
		mLoadingTextView = (TextView) child
				.findViewById(R.id.loading_activity_loading_text);
		mLoadingBar = child.findViewById(R.id.loading_activity_loading_bar);
		mLoadingLayout.setOnClickListener(this);

		mListView = (ListView) child
				.findViewById(R.id.layout_calendar_week_list);

	}

	@Override
	protected void onPopCreat() {
		load(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_calender_img1:
			Log.e("info", "左");
			// 上一月
			if (month == 1) {
				year--;
				month = 12;
			} else {
				month--;
			}
			load(year, month);
			break;
		case R.id.head_calender_img2:
			Log.e("info", "右");
			if (month == 12) {
				year++;
				month = 1;
			} else {
				month++;
			}
			load(year, month);
			// 下一月

			break;
		}
	}

	private int year;

	private int month;

	public void load(int y, int m) {
		showLoading();
		year = y;
		month = m;
		Top_Date.setText(year + "年" + month + "月");
		GetResultListCallback listener = new GetResultListCallback() {

			@Override
			public void onFinished(Object o) {
				mList = (ArrayList<ReportCalendarWeekInfo>) o;
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessage(msg);

			}

			@Override
			public void onErro(Object o) {
				mHandler.sendEmptyMessage(1);

			}
		};
		CPControl.GetUserWeekPointResult(listener, year + "-" + month + "-02");
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				// 拉取数据成功
				//

				if (year == today.get(Calendar.YEAR)
						&& month >= today.get(Calendar.MONTH) + 1) {
					mImageViewR
							.setImageResource(R.drawable.arrow_calendar_right_unselected);
					mImageViewR.setClickable(false);

				} else {

					mImageViewR
							.setImageResource(R.drawable.arrow_calendar_right);
					mImageViewR.setClickable(true);

				}
				OnItemClickListener listener = new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ReportCalendarWeekInfo info = mList.get(position);
						String date = info.getStartDay();
						if (date != null && date.length() > 0) {
							String point = info.getPoint();
							if (point != null && point.length() > 0) {
								mOnCalendarWeekClick.onClick(date);
								dissmiss();
							}

						}
					}
				};
				mListView.setOnItemClickListener(listener);
				dissmissLoading();
				break;

			case 1:
				// 拉取数据成功
				erroLoading();
				break;
			}

		}
	};

	private void showLoading() {
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	private void dissmissLoading() {
		mLoadingLayout.setVisibility(View.GONE);
	}

	private void erroLoading() {
		mLoadingTextView.setText("获取数据失败");
		mLoadingBar.setVisibility(View.GONE);
	}

	public interface OnCalendarWeekClick {
		void onClick(String date);
	};
}
