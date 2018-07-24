package com.carlt.sesame.ui.activity.career;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.ui.activity.base.BaseLoadingView;
import com.carlt.sesame.ui.adapter.RewardAdapter;

import java.util.ArrayList;

/**
 * 我的驾驶证-tab3页面(我的奖品)
 * 
 * @author daisy
 */
public class RewardView extends BaseLoadingView {

	private ListView mListView;

	private View mLay;

	private View mBtn;

	private RewardAdapter mAdapter;

	private Context mContext;

	private ArrayList<PrizeInfo> mArrayList;

	public RewardView(Context context) {
		super(context);
		setContent(R.layout.layout_career_reward);
		mContext = context;
		init();
		// LoadData();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.layout_career_reward_list);
		mLay = findViewById(R.id.layout_career_reward_lay);
		mBtn = findViewById(R.id.layout_career_reward_btn);
		OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mContext,
						SecretaryTipsActivity.class);
				mIntent.putExtra(SecretaryTipsActivity.TIPS_TITLE, "奖品活动");
				mIntent.putExtra(SecretaryTipsActivity.TIPS_TYPE,
						SecretaryMessageInfo.C1_T3);
				mContext.startActivity(mIntent);

			}
		};
		mBtn.setOnClickListener(l);

	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent mIntent = new Intent(mContext, RewardDetailActivity.class);
			mIntent.putExtra(RewardDetailActivity.REWARDID,
					mArrayList.get(position).getId());
			mContext.startActivity(mIntent);
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

		mAdapter = new RewardAdapter(mContext, mArrayList);
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
	public void refreshImage(String url, Bitmap mBitmap) {
		super.refreshImage(url, mBitmap);
		if (mArrayList != null) {
			for (int j = 0; j < mArrayList.size(); j++) {
				if (mArrayList.get(j).getIconUrl().equals(url)) {
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
}
