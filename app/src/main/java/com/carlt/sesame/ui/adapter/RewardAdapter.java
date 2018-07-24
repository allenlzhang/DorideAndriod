package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 奖品Adapter
 * 
 * @author daisy
 */
public class RewardAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<PrizeInfo> mDataList;

	private AsyncImageLoader mAsyncImageLoader;

	public RewardAdapter(Context context, ArrayList<PrizeInfo> dataList) {
		mInflater = LayoutInflater.from(context);
		this.mDataList = dataList;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
	}

	@Override
	public int getCount() {

		if (mDataList != null)
			return mDataList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDataList != null)
			return mDataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_reward, null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_reward_text1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.list_item_reward_text2);
			mHolder.mTextView3 = (TextView) convertView
					.findViewById(R.id.list_item_reward_text3);
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.list_item_reward_img);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		PrizeInfo mPrizeInfo = mDataList.get(position);
		String url = mPrizeInfo.getIconUrl();

		mHolder.mTextView1.setText(mPrizeInfo.getName() + "");
		mHolder.mTextView2.setText("到期时间：" + mPrizeInfo.getExpiredate());
		mHolder.mTextView3.setText(mPrizeInfo.getDescription() + "");
		if (mPrizeInfo.getDescription().length() > 30) {
			mHolder.mTextView3.setText(mPrizeInfo.getDescription().substring(0,
					30)
					+ "...");
		}

		if (url != null && !url.equals("")) {
			Bitmap bitmap1 = mAsyncImageLoader.getBitmapByUrl(url);
			if (bitmap1 != null) {
				mHolder.mImageView.setImageBitmap(bitmap1);
			} else {
				mHolder.mImageView
						.setImageResource(R.drawable.icon_default_reward);
			}
		} else {
			mHolder.mImageView.setImageResource(R.drawable.icon_default_reward);
		}

		return convertView;
	}

	class Holder {
		private TextView mTextView1;// 奖品名称

		private TextView mTextView2;// 奖品到期日期

		private TextView mTextView3;// 奖品描述

		private ImageView mImageView;// 奖品icon
	}

}
