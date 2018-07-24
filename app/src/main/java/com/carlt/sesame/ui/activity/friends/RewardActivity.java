package com.carlt.sesame.ui.activity.friends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.RewardDetailActivity;
import com.carlt.sesame.ui.activity.career.SecretaryTipsActivity;
import com.carlt.sesame.ui.adapter.RewardAdapter;

import java.util.ArrayList;

/**
 * 我的奖品
 * 
 * @author daisy
 */
public class RewardActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ListView mListView;

	private View mLay;

	private View mBtn;

	private RewardAdapter mAdapter;

	private ArrayList<PrizeInfo> mArrayList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_career_reward);
		setTitleView(R.layout.head_back);

		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("我的奖品");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.layout_career_reward_list);
		mLay =  findViewById(R.id.layout_career_reward_lay);
		mBtn =  findViewById(R.id.layout_career_reward_btn);

		OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(RewardActivity.this,
						SecretaryTipsActivity.class);
				mIntent.putExtra(SecretaryTipsActivity.TIPS_TITLE, "奖品活动");
				mIntent.putExtra(SecretaryTipsActivity.TIPS_TYPE,
						SecretaryMessageInfo.C1_T3);
				startActivity(mIntent);

			}
		};
		mBtn.setOnClickListener(l);

	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent mIntent = new Intent(RewardActivity.this,
					RewardDetailActivity.class);
			mIntent.putExtra(RewardDetailActivity.REWARDID,
					mArrayList.get(position).getId());
			startActivity(mIntent);
		}
	};

	@Override
	protected void LoadSuccess(Object data) {
		mArrayList = (ArrayList<PrizeInfo>) data;

		int size = mArrayList.size();
		if (size == 0) {
			mListView.setVisibility(View.GONE);
			mLay.setVisibility(View.VISIBLE);
		} else {
			mLay.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}

		mAdapter = new RewardAdapter(RewardActivity.this, mArrayList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetPrizeListResult(listener);
	}

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

}
