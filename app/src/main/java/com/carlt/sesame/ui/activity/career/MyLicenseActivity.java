package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseLoadingView;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyLicenseActivity extends LoadingActivityWithTitle implements
		OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ViewPager mViewpager;

	private TextView mTab1;

	private TextView mTab2;

	private TextView mTab3;

	private View cursor;

	private int cursorWith = 0;

	private int currentPage;

	private int color_default;

	private int color_selected;

	private List<BaseLoadingView> childViewList;

	private Intent mIntent;

	public final static String MY_LICENSE_TAB_IDEX = "my_license_tab_idex";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylicense_lay);
		setTitleView(R.layout.head_back);
		mIntent = getIntent();
		currentPage = mIntent.getIntExtra(MY_LICENSE_TAB_IDEX, 0);
		initTitle();
		init();
		LoadData();

	}

	@Override
	protected void LoadSuccess(Object data) {
		txtRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				childViewList.get(currentPage).onShare();
			}
		});
		childViewList = new ArrayList<BaseLoadingView>();

		LicenseInfoView layout1 = new LicenseInfoView(MyLicenseActivity.this);
		ReportSummaryView layout2 = new ReportSummaryView(
				MyLicenseActivity.this);
		RewardView layout3 = new RewardView(MyLicenseActivity.this);

		childViewList.add(layout1);
		childViewList.add(layout2);
		childViewList.add(layout3);

		PagerAdapter a = new ViewPagerAdapter(childViewList);
		mViewpager.setAdapter(a);
		mViewpager.setOnPageChangeListener(OnPageChangeListener);
		ChangeView(currentPage);

		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		// TODO Auto-generated method stub
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		cursorWith = DorideApplication.ScreenWith / 3;
		new Thread() {

			@Override
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listener.onFinished(null);
			}

		}.start();

	}

	@Override
	protected void onResume() {
		ChangeView(currentPage);
		super.onResume();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("我的生涯");
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
		mViewpager = (ViewPager) findViewById(R.id.mylicense_lay_vp);
		mTab1 = (TextView) findViewById(R.id.mylicense_tab1);
		mTab2 = (TextView) findViewById(R.id.mylicense_tab2);
		mTab3 = (TextView) findViewById(R.id.mylicense_tab3);

		mTab1.setText("驾驶证");
		mTab2.setText("行车生涯");
		mTab3.setText("我的奖品");

		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);

		cursor = findViewById(R.id.food_taboo_cursor);
		color_default = getResources().getColor(R.color.viewpager_default);
		color_selected = getResources().getColor(R.color.viewpager_selected);
	}

	private void ChangeTab(int arg0) {
		switch (arg0) {
		case 0:
			mTab1.setTextColor(color_selected);
			mTab2.setTextColor(color_default);
			mTab3.setTextColor(color_default);
			txtRight.setVisibility(View.VISIBLE);
			break;
		case 1:
			mTab1.setTextColor(color_default);
			mTab2.setTextColor(color_selected);
			mTab3.setTextColor(color_default);
			txtRight.setVisibility(View.VISIBLE);
			break;
		case 2:
			mTab1.setTextColor(color_default);
			mTab2.setTextColor(color_default);
			mTab3.setTextColor(color_selected);
			txtRight.setVisibility(View.GONE);
			break;

		}
	}

	private void ChangeCursor(int arg0) {
		TranslateAnimation animation = null;

		if (currentPage < arg0) {

			animation = new TranslateAnimation(cursorWith * currentPage,
					cursorWith * arg0, 0, 0);
		} else {
			animation = new TranslateAnimation(cursorWith * currentPage,
					cursorWith * arg0, 0, 0);
		}
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);

	}

	private void ChangeView(final int arg0) {

		// 滑块滑动
		ChangeCursor(arg0);
		// 字体更换
		ChangeTab(arg0);
		// 切屏
		mViewpager.setCurrentItem(arg0);
		// 记录当前页面
		currentPage = arg0;
		if (childViewList != null && childViewList.size() > arg0) {
			childViewList.get(arg0).OnShow();
		}

	}

	private ViewPager.OnPageChangeListener OnPageChangeListener = new OnPageChangeListener() {
		public void onPageSelected(int arg0) {
			ChangeView(arg0);

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mylicense_tab1:
			// 标签1按钮
			if (currentPage != 0) {

				ChangeView(0);
			}

			break;

		case R.id.mylicense_tab2:
			// 标签2按钮
			if (currentPage != 1) {

				ChangeView(1);
			}

			break;

		case R.id.mylicense_tab3:
			// 标签2按钮
			if (currentPage != 2) {

				ChangeView(2);
			}

			break;	

		}
	}

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (childViewList != null) {
			for (int j = 0; j < childViewList.size(); j++) {
				childViewList.get(j).refreshImage(url, mBitmap);
			}
		}

	}

}
