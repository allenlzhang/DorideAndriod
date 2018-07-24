package com.carlt.sesame.ui.activity.career.report;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.ui.activity.base.BaseFragmentActivity;
import com.carlt.sesame.ui.activity.career.report.CalendarWeek.OnCalendarWeekClick;
import com.carlt.sesame.utility.MyTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportWeekActivity extends BaseFragmentActivity implements
		OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private DirectionalViewPager pager;

	private ReportSwitchBar mReportSwitchBar;

	FragmentWeekAdapter adapter;

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
		adapter = new FragmentWeekAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);
		mReportSwitchBar = (ReportSwitchBar) findViewById(R.id.activity_report_lay_switchbar);
		mReportSwitchBar.Load(adapter.getCount());
		mReportSwitchBar.change(0);
		initTitle();

		String weekInitialValue = "";
		try {
			weekInitialValue = getIntent().getStringExtra(
					ReportActivity.WEEK_INITIAL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (weekInitialValue == null || weekInitialValue.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar today = Calendar.getInstance();
			today.add(Calendar.DATE, -7);
			weekInitialValue = format.format(today.getTime());
		}
		loadData(weekInitialValue);

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

	private void loadData(String date) {
		if (null != date && date.length() > 0) {
			StringBuffer mStringBuffer = new StringBuffer();

			String firstdayOfWeek = MyTimeUtil.getFirstdayOfWeek(date);
			int number = MyTimeUtil.getWeekOfMonth(date);
			if (firstdayOfWeek != null && firstdayOfWeek.length() > 0
					&& number > 0) {
				String[] s1 = firstdayOfWeek.split("-");
				if (s1.length > 2) {
					mStringBuffer.append(s1[0]);
					mStringBuffer.append("年");
					mStringBuffer.append(s1[1]);
					mStringBuffer.append("月第");
					mStringBuffer.append(number);
					mStringBuffer.append("周行车报告");
				}
				title.setText(mStringBuffer.toString());
			} else {
				title.setText("周报");
			}

		} else {
			title.setText("周报");
		}

		CPControl.GetWeekReportResult(date, listener);
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

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
				final ReportWeekInfo mReportWeekInfo = (ReportWeekInfo) msg.obj;

				adapter.LoadData(msg.obj);
				pager.setCurrentItem(0);
				mLoadingLayout.setVisibility(View.GONE);

				txtRight.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 晒一晒
//						ShareControl.share(ReportWeekActivity.class,
//								ReportWeekActivity.this,
//								mReportWeekInfo.getShareTitle(),
//								mReportWeekInfo.getShareText(),
//								mReportWeekInfo.getShareLink(),
//								R.drawable.friends_share_img);

						CPControl.GetReportShareResult(
								mReportWeekInfo.getShareTitle(),
								mReportWeekInfo.getShareText(),
								mReportWeekInfo.getShareLink(),
								mReportWeekInfo.getReportDate(),
								CPControl.REPORT_WEEK);

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
		CalendarWeek mCalendarWeek = new CalendarWeek(ReportWeekActivity.this,
				mOnCalendarWeekClick);
		mCalendarWeek.showMenu();
	}

	private OnCalendarWeekClick mOnCalendarWeekClick = new OnCalendarWeekClick() {

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
