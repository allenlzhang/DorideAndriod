package com.carlt.sesame.ui.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

import java.util.ArrayList;

public class ImgDetialActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewpager;

	private int currentPage;

	private ArrayList<BaseImgDetialView> childViewList;

	private ArrayList<String> imgList;

	private Intent mIntent;

	private View titleLay;
	private TextView title;
	private View delete;
	private ImgDetialPagerAdapter adapter;

	public final static String INDEX = "index";
	public final static String IMGLIST = "imgList";
	public final static String DELETE = "delete";
	private boolean hasDelete = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img_detial_activity);
		mIntent = getIntent();
		imgList = mIntent.getStringArrayListExtra(IMGLIST);
		currentPage = mIntent.getIntExtra(INDEX, 0);
		hasDelete = mIntent.getBooleanExtra(DELETE, false);
		Log.e("info", "currentPage==" + currentPage);
		init();

	}

	@Override
	protected void onResume() {
		ChangeView(currentPage);
		super.onResume();
	}

	private void init() {

		titleLay = findViewById(R.id.img_detial_title);
		ImageView back = (ImageView) findViewById(R.id.img_detial_title_img1);
		title = (TextView) findViewById(R.id.img_detial_title_txt1);
		title.setText("故障照片");
		delete = findViewById(R.id.img_detial_title_img2);
		back.setOnClickListener(this);
		delete.setOnClickListener(this);
		if (hasDelete) {
			titleLay.setVisibility(View.VISIBLE);
		} else {
			titleLay.setVisibility(View.GONE);
		}

		mViewpager = (ViewPager) findViewById(R.id.img_detial_lay_vp);
		childViewList = new ArrayList<BaseImgDetialView>();
		for (int i = 0; i < imgList.size(); i++) {
			BaseImgDetialView object = new BaseImgDetialView(
					ImgDetialActivity.this, imgList.get(i));
			childViewList.add(object);
		}
		adapter = new ImgDetialPagerAdapter(childViewList);
		mViewpager.setAdapter(adapter);
		mViewpager.setOnPageChangeListener(OnPageChangeListener);
		ChangeView(currentPage);
	}

	private void ChangeView(final int arg0) {
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

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		int i = imgList.indexOf(url);
		childViewList.get(i).OnImgFinisher(url, mBitmap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_detial_title_img1:
			finish();
			break;
		case R.id.img_detial_title_img2:
			// 删除
			// childViewList.remove(currentPage);
			// imgList.remove(currentPage);

			childViewList.remove(currentPage);
			imgList.remove(currentPage);
			ChangeView(currentPage);
			adapter.notifyDataSetChanged();
			if (childViewList.size() == 0) {
				finish();
			}
			break;

		}
	}

	private final static int RESULT_CODE = 200;

	@Override
	public void finish() {
		Intent i = new Intent();
		i.putStringArrayListExtra(IMGLIST, imgList);
		setResult(RESULT_CODE, i);
		super.finish();
	}

}
