package com.carlt.sesame.ui.activity.career;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.LicenseLevelAdapter;
import com.carlt.sesame.utility.MyParse;

import java.util.ArrayList;

/**
 * 驾驶证等级页面
 * 
 * @author daisy
 */
public class LicenseLevelActivity extends LoadingActivityWithTitle {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ListView mListView; // 驾驶证等级列表

	private LicenseLevelAdapter mAdapter;

	private ArrayList<LicenceLevelInfo> mArrayList;

	private int mLevel;// 驾驶证等级

	public final static String LICENSE_LEVEL = "license_level";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_license_level);
		setTitleView(R.layout.head_back);
		mLevel = MyParse.parseInt(getIntent().getStringExtra(LICENSE_LEVEL));
		initTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("驾驶证等级");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mListView = (ListView) findViewById(R.id.activity_career_license_level_list);
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
		mArrayList = (ArrayList<LicenceLevelInfo>) data;
		mAdapter = new LicenseLevelAdapter(this, mArrayList, mLevel);
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void LoadErro(Object erro) {

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetLicenceLevelResult(listener);
	}

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
}
