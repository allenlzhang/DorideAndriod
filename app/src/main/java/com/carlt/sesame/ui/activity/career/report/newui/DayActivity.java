package com.carlt.sesame.ui.activity.career.report.newui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.CarLogInfo;
import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.report.CalendarDay;
import com.carlt.sesame.ui.activity.career.report.CalendarDay.OnCalendarDayClick;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.ui.pull.PullToRefreshListView;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyTimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("InflateParams")
public class DayActivity extends LoadingActivityWithTitle implements
		OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

	private ImageView mImgHead;

	private TextView mTxtName;

	private ImageView mImgGender;

	private TextView mTxtDriveInfo;

	private TextView mTxtScore;

	private LinearLayout mLineTagContainer;

	private TextView mTxtMiles;

	private TextView mTxtTime;

	private TextView mTxtOil;

	private TextView mTxtAvgOil;

	private TextView mTxtAvgSpeed;

	private TextView mTxtMaxSpeed;

	private PullToRefreshListView mPullToRefresh;

	private ListView listView;

	LayoutInflater mInflate;

	String dayInitialValue = "";

	private final static String nullDate = "0000-00-00";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_day2);
		setTitleView(R.layout.head_back);

		initTitle();
		init();

		try {
			dayInitialValue = getIntent().getStringExtra(
					ReportActivity.DAY_INITIAL);
		} catch (Exception e) {
		}
		if (dayInitialValue == null || dayInitialValue.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
			dayInitialValue = format.format(Calendar.getInstance().getTime());
		}
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		txtRight.setText("晒一晒");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mInflate = LayoutInflater.from(this);
		mImgHead = (ImageView) findViewById(R.id.day_img_headImg);
		mTxtName = (TextView) findViewById(R.id.day_txt_userName);
		mImgGender = (ImageView) findViewById(R.id.day_img_genderImg);
		mTxtDriveInfo = (TextView) findViewById(R.id.day_txt_driveInfo);
		mTxtScore = (TextView) findViewById(R.id.day_txt_driveScore);
		mLineTagContainer = (LinearLayout) findViewById(R.id.day_line_tagContainer);
		mTxtMiles = (TextView) findViewById(R.id.day_txt_miles);
		mTxtTime = (TextView) findViewById(R.id.day_txt_time);
		mTxtOil = (TextView) findViewById(R.id.day_txt_oils);
		mTxtAvgOil = (TextView) findViewById(R.id.day_txt_avgOils);
		mTxtAvgSpeed = (TextView) findViewById(R.id.day_txt_avgSpeeds);
		mTxtMaxSpeed = (TextView) findViewById(R.id.day_txt_maxSpeeds);
		mPullToRefresh = (PullToRefreshListView) findViewById(R.id.day_refresh_logList);
		listView = mPullToRefresh.getRefreshableView();
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);

		txtRight.setVisibility(View.GONE);
		txtRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 晒一晒
				// friends_share_img
//				ShareControl.share(ReportDayActivity.class, DayActivity.this,
//						mReportDayInfo.getShareTitle(),
//						mReportDayInfo.getShareText(),
//						mReportDayInfo.getShareLink(),
//						R.drawable.friends_share_img);

				CPControl.GetReportShareResult(mReportDayInfo.getShareTitle(),
						mReportDayInfo.getShareText(),
						mReportDayInfo.getShareLink(),
						mReportDayInfo.getReportDate(), CPControl.REPORT_DAY);

			}
		});

		mTxtName.setText(LoginInfo.getRealname());
		Log.e("info", "LoginInfo.getAvatar_img()==" + LoginInfo.getAvatar_img());
		if (!LoginInfo.getAvatar_img().equals("")) {
			Bitmap mBitmap = mAsyncImageLoader.getBitmapByUrl(LoginInfo
					.getAvatar_img());
			if (mBitmap != null) {
				// 设置头像
				mImgHead.setImageBitmap(mBitmap);
			} else {
				mImgHead.setImageResource(R.drawable.icon_default_head);
			}

		} else {
			mImgHead.setImageResource(R.drawable.icon_default_head);
		}
		if (LoginInfo.getGender().equals(LoginInfo.GENDER_NAN)) {
			mImgGender.setImageResource(R.drawable.icon_sex_male);
		} else if (LoginInfo.getGender().equals(LoginInfo.GENDER_NV)) {
			mImgGender.setImageResource(R.drawable.icon_sex_female);
		} else {
			mImgGender.setImageResource(R.drawable.icon_sex_secret);
		}

		int count = 0;
		if (carLogInfos != null && carLogInfos.size() > 0) {
			count = carLogInfos.get(0).getListCount();
		}

		mPullToRefresh.setPullLoadEnabled(false);
		mPullToRefresh.setPullRefreshEnabled(false);
		LogAdapter mAdapter = new LogAdapter();
		listView.setAdapter(mAdapter);
		ReportDayInfo mDayInfo = mReportDayInfo;
		// mTxtDriveInfo.setText("今日行车" + count + "次，平均驾驶得分");
		// mTxtScore.setText(mDayInfo.getPoint());
		mTxtDriveInfo.setText("今日行车" + count + "次");
		mTxtAvgOil.setText(mDayInfo.getAvgFuel() + "kWh");
		mTxtOil.setText(mDayInfo.getSumFuel());
		mTxtMiles.setText(mDayInfo.getSumMiles() + "km");
		mTxtTime.setText(mDayInfo.getSumTime() + "h");
		mTxtAvgSpeed.setText(mDayInfo.getAvgSpeed() + "km/h");
		mTxtMaxSpeed.setText(mDayInfo.getMaxSpeed() + "km/h");

		if (mDayInfo.getTag() != null && mDayInfo.getTag().length() > 0) {
			String[] tags = mDayInfo.getTag().split(",");
			if (tags != null) {
				count = tags.length;
				int childs = mLineTagContainer.getChildCount();
				for (int i = 0; i < childs - count; i++) {
					mLineTagContainer.getChildAt(childs - 1 - i).setVisibility(
							View.GONE);
				}

				for (int i = 0; i < count; i++) {
					TextView txt = (TextView) mLineTagContainer.getChildAt(i);
					txt.setText(tags[i]);
				}
			} else {
				mLineTagContainer.setVisibility(View.GONE);
			}

		} else {
			mLineTagContainer.setVisibility(View.GONE);
		}

	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		if (null != dayInitialValue && dayInitialValue.length() > 0
				&& !dayInitialValue.equals(nullDate)) {
			title.setText(dayInitialValue + "日行车日志");
		} else {
			dayInitialValue = MyTimeUtil.getDateFormat3();
			title.setText(dayInitialValue + "日行车日志");
		}

		CPControl.GetDayReportResult(dayInitialValue, listener_day);
		CPControl.GetCarLogResult(dayInitialValue, listener_day_log);
	}

	private GetResultListCallback listener_day = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private GetResultListCallback listener_day_log = new GetResultListCallback() {

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private boolean isSuccessDay;// 日报信息是否拉取成功

	private boolean isSuccessLog;// 行车日志是否拉取成功

	private ReportDayInfo mReportDayInfo;

	private ArrayList<CarLogInfo> carLogInfos;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				isSuccessDay = false;
				LoadErro(msg.obj);
				break;
			case 1:
				isSuccessDay = true;
				mReportDayInfo = (ReportDayInfo) msg.obj;
				if (isSuccessDay && isSuccessLog) {
					LoadSuccess(null);
				}
				break;

			case 2:
				LoadErro(msg.obj);
				break;
			case 3:
				isSuccessLog = true;
				carLogInfos = (ArrayList<CarLogInfo>) msg.obj;
				if (isSuccessDay && isSuccessLog) {
					LoadSuccess(null);
				}
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == ReportActivity.MENU) {
			selectDate();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 弹出日期选择框
	 */
	private void selectDate() {
		CalendarDay mCalendarDay = new CalendarDay(DayActivity.this,
				mOnCalendarDayClick);

		mCalendarDay.showMenu();
	}

	OnCalendarDayClick mOnCalendarDayClick = new OnCalendarDayClick() {

		@Override
		public void onClick(String date) {
			dayInitialValue = date;
			LoadData();

		}
	};

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (url != null && url.equals(LoginInfo.getAvatar_img())
				&& mBitmap != null) {
			mImgHead.setImageBitmap(mBitmap);
		} else if (url != null && url.equals(LoginInfo.getCarlogo())
				&& mBitmap != null) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.listItem_day_txt_gpsTrack:
			// // 跳转至轨迹回放页面
			Intent mIntent = new Intent(DayActivity.this,
					GpsTrailActivity.class);
			CarLogInfo mCarLogInfo = (CarLogInfo) v.getTag();
			mIntent.putExtra(GpsTrailActivity.CAR_LOG_INFO, mCarLogInfo);
			mIntent.putExtra(ReportActivity.DAY_INITIAL, dayInitialValue);
			startActivity(mIntent);
			break;
		}

	}

	public class LogAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (carLogInfos != null) {
				return carLogInfos.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (carLogInfos != null) {
				return carLogInfos.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder = null;
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = mInflate.inflate(R.layout.list_item_report_day,
						null);
				mHolder.mDriveInfo = (TextView) convertView
						.findViewById(R.id.listItem_day_driveInfo);
				mHolder.mScore = (TextView) convertView
						.findViewById(R.id.listItem_day_driveScore);
				mHolder.mTimeAndMiles = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_timeAndMiles);
				mHolder.mHurrl1 = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_hurry1);
				mHolder.mHurrl2 = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_hurry2);
				mHolder.mHurrl3 = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_hurry3);
				mHolder.mHurrl4 = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_hurry4);
				mHolder.mInfoOil = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_infoOil);
				mHolder.mAvgOil = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_infoAvgOil);
				mHolder.mAvgSpeed = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_infoSpeed);
				mHolder.mMaxSpeed = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_infoMaxSpeed);
				mHolder.mGpsTrack = (TextView) convertView
						.findViewById(R.id.listItem_day_txt_gpsTrack);

				Log.e("info",
						"LoginInfo.isGpsDevice()==" + LoginInfo.isGpsDevice());
				if (LoginInfo.isGpsDevice()) {
					mHolder.mGpsTrack.setVisibility(View.VISIBLE);
				} else {
					mHolder.mGpsTrack.setVisibility(View.INVISIBLE);
				}

				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			CarLogInfo cInfo = carLogInfos.get(position);
			mHolder.mScore.setText(cInfo.getPoint());
			String driveInfo = DayActivity.this.getResources().getString(
					R.string.listItem_day_driveInfo,
					position + 1 + "/" + carLogInfos.size());
			mHolder.mDriveInfo.setText(driveInfo);

			String startTime = MyTimeUtil.getTime(cInfo.getStarttime());
			String endTime = MyTimeUtil.getTime(cInfo.getStopTime());
			String timeAndMiles = DayActivity.this.getResources().getString(
					R.string.listItem_day_timeAndMile, startTime, endTime,
					cInfo.getTime(), cInfo.getMiles());
			mHolder.mTimeAndMiles.setText(timeAndMiles);
			mHolder.mHurrl1.setText(cInfo.getBrake());
			mHolder.mHurrl2.setText(cInfo.getTurn());
			mHolder.mHurrl3.setText(cInfo.getSpeedup());
			mHolder.mHurrl4.setText(cInfo.getOverspeed());
			mHolder.mInfoOil.setText(cInfo.getFuel());
			mHolder.mAvgOil.setText(cInfo.getAvgfuel() + "kWh");
			mHolder.mAvgSpeed.setText(cInfo.getAvgspeed() + "km/h");
			mHolder.mMaxSpeed.setText(cInfo.getMaxspeed() + "km/h");
			mHolder.mGpsTrack.setOnClickListener(DayActivity.this);
			mHolder.mGpsTrack.setTag(cInfo);
			return convertView;
		}

		class ViewHolder {
			TextView mDriveInfo;

			TextView mScore;

			TextView mTimeAndMiles;

			TextView mHurrl1;

			TextView mHurrl2;

			TextView mHurrl3;

			TextView mHurrl4;

			TextView mInfoOil;

			TextView mAvgOil;

			TextView mAvgSpeed;

			TextView mMaxSpeed;

			TextView mGpsTrack;
		}

	}

}
