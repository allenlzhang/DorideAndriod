package com.carlt.sesame.ui.activity.career.report;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.ui.activity.base.BaseFragmentActivity;
import com.carlt.sesame.ui.activity.career.report.CalendarDay.OnCalendarDayClick;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReportDayActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private DirectionalViewPager pager;

	private ReportSwitchBar mReportSwitchBar;

	FragmentDayAdapter adapter;
	
	private View mLoadingLayout;

	private TextView mLoadingTextView;

	private View mLoadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_lay);
		mLoadingLayout = findViewById(R.id.loading_activity_loading_lay);
		mLoadingTextView = (TextView) findViewById(R.id.loading_activity_loading_text);
		mLoadingBar = findViewById(R.id.loading_activity_loading_bar);

		pager = (DirectionalViewPager) findViewById(R.id.activity_report_lay_pager);
		pager.setOrientation(DirectionalViewPager.VERTICAL);
		pager.setOnPageChangeListener(l);

		adapter = new FragmentDayAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		mReportSwitchBar = (ReportSwitchBar) findViewById(R.id.activity_report_lay_switchbar);
		mReportSwitchBar.Load(adapter.getCount());
		mReportSwitchBar.change(0);
		initTitle();

		String dayInitialValue = "";
		try {
			dayInitialValue = getIntent().getStringExtra(
					ReportActivity.DAY_INITIAL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (dayInitialValue == null || dayInitialValue.equals("")) {
			dayInitialValue = LoginInfo.getLately_day();
			if(TextUtils.isEmpty(LoginInfo.getLately_day())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
				dayInitialValue = format.format(Calendar.getInstance().getTime());
			}
					
		}
		loadData(dayInitialValue);
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		txtRight.setVisibility(View.GONE);
		txtRight.setText("晒一晒");

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void loadData(String date) {
		if (null != date && date.length() > 0) {
			title.setText(date + "日行车报告");
		}
		
		CPControl.GetDayReportResult(date, listener);
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {

			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				final ReportDayInfo mReportDayInfo = (ReportDayInfo) msg.obj;

				pager.setCurrentItem(0);
				adapter.LoadData(msg.obj);
				mLoadingLayout.setVisibility(View.GONE);
				txtRight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 晒一晒
						// friends_share_img
//						ShareControl.share(ReportDayActivity.class,
//								ReportDayActivity.this,
//								mReportDayInfo.getShareTitle(),
//								mReportDayInfo.getShareText(),
//								mReportDayInfo.getShareLink(),
//								R.drawable.friends_share_img);

						CPControl.GetReportShareResult(
								mReportDayInfo.getShareTitle(),
								mReportDayInfo.getShareText(),
								mReportDayInfo.getShareLink(),
								mReportDayInfo.getReportDate(),
								CPControl.REPORT_DAY);

					}
				});
				break;

			case 1:
				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				mLoadingTextView.setText(mBaseResponseInfo.getInfo());
				mLoadingBar.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};

	OnPageChangeListener l = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			mReportSwitchBar.change(arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		}
	}

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
		CalendarDay mCalendarDay = new CalendarDay(ReportDayActivity.this,
				mOnCalendarDayClick);

		mCalendarDay.showMenu();
	}

	OnCalendarDayClick mOnCalendarDayClick = new OnCalendarDayClick() {

		@Override
		public void onClick(String date) {
			loadData(date);

		}
	};

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (adapter != null) {
			adapter.refreshImage(url, mBitmap);
		}
	}

}
