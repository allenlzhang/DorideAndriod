package com.carlt.sesame.ui.activity.career.order;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.career.DayOrderStateInfo;
import com.carlt.sesame.ui.activity.career.report.ReportDateView.OnItemClick;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyParse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeView extends LinearLayout implements OnClickListener {
	private Context context;

	private View mLoadingLayout;

	private TextView mLoadingTextView;

	private View mLoadingBar;

	private OnItemClick mOnItemClick;

	private ListView mListView;

	private ArrayList<DayOrderStateInfo> mDayOrderStateInfoList;

	public TimeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.order_rili_time, this,
				true);
		// 声明控件，并绑定事件
		mListView = (ListView) findViewById(R.id.rili_time_list);
		// Loading控件
		mLoadingLayout = findViewById(R.id.loading_activity_mainlayout);
		mLoadingTextView = (TextView) findViewById(R.id.loading_activity_loading_text);
		mLoadingBar = findViewById(R.id.loading_activity_loading_bar);
		mLoadingLayout.setOnClickListener(this);

	}

	private int year;

	private int month;

	private int day;

	public void load(String date) {
		flag = true;
		showLoading();
		year = MyParse.parseInt(date.split("-")[0]);
		month = MyParse.parseInt(date.split("-")[1]);
		day = MyParse.parseInt(date.split("-")[2]);

		if (mOnItemClick != null) {
			mOnItemClick.onTextChange(year + "年" + month + "月" + day + "日");
			mOnItemClick.onTitleStateChange(year, month, day);
		}

		CPControl.GetDayOrderStateResult(year + "-" + month + "-" + day,
				listener111);
	}

	GetResultListCallback listener111 = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {

			mDayOrderStateInfoList = (ArrayList<DayOrderStateInfo>) o;
			Message msg = new Message();
			msg.what = 0;
			mHandler.sendEmptyMessage(0);

		}

		@Override
		public void onErro(Object o) {
			mHandler.sendEmptyMessage(1);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				// 拉取数据成功
				TimeViewAdapter adapter = new TimeViewAdapter(context,
						mDayOrderStateInfoList);
				mListView.setAdapter(adapter);
				mListView.setOnItemClickListener(listener);
				dissmissLoading();
				flag = false;
				break;

			case 1:
				// 拉取数据成功
				erroLoading();
				flag = false;
				break;
			}

		}
	};
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DayOrderStateInfo info = mDayOrderStateInfoList.get(position);
			if (mOnItemClick != null) {
				if (info != null && !info.isFlag()) {
					if (info.getTotal() - info.getUsed() > 0) {
						StringBuffer sb = new StringBuffer();
						sb.append(year);
						sb.append("-");
						sb.append(month);
						sb.append("-");
						sb.append(day);
						sb.append(",");
						sb.append(info.getTime());
						mOnItemClick.onClick(sb.toString());
					}

				}

			}
		}
	};
	private boolean flag = false;

	// 下一日
	public void nextDay() {

		if (!flag) {
			flag = true;
			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, day);
			c.add(Calendar.DATE, 1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Log.e("info", "==" + format.format(c.getTime()));
			load(format.format(c.getTime()));
		}
	}

	// 上一日
	public void preDay() {
		if (!flag) {
			flag = true;
			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, day);
			c.add(Calendar.DATE, -1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Log.e("info", "==" + format.format(c.getTime()));
			load(format.format(c.getTime()));
		}
	}

	@Override
	public void onClick(View arg0) {

	}

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

	public void setmOnItemClick(OnItemClick mOnItemClick) {
		this.mOnItemClick = mOnItemClick;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

}
