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
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 勋章Adapter
 * 
 * @author daisy
 */
public class MedalAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<MedalInfo> mDataList;

	private AsyncImageLoader mAsyncImageLoader;

	private MedalInfo mMedalInfo;

	private int mDataListSize = 0;

	public MedalAdapter(Context context, ArrayList<MedalInfo> dataList) {
		mInflater = LayoutInflater.from(context);
		mDataList = dataList;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
		if (mDataList != null)
			mDataListSize = mDataList.size();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataListSize;
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
			convertView = mInflater.inflate(R.layout.grid_item_medal, null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.grid_item_reward_text1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.grid_item_reward_text2);
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.grid_item_reward_img);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mMedalInfo = mDataList.get(position);
		mHolder.mTextView1.setText(mMedalInfo.getName());
		mHolder.mTextView2.setText(mMedalInfo.getDescription());

		String url;
		if (mMedalInfo.isIsgot()) {
			url = mMedalInfo.getIconUrl2();
			Bitmap bitmap1 = mAsyncImageLoader.getBitmapByUrl(url);
			if (bitmap1 != null) {
				mHolder.mImageView.setImageBitmap(bitmap1);
			} else {
				mHolder.mImageView
						.setImageResource(R.drawable.icon_default_medal);
			}
		} else {
			url = mMedalInfo.getIconUrl1();
			Bitmap bitmap2 = mAsyncImageLoader.getBitmapByUrl(url);
			if (bitmap2 != null) {
				mHolder.mImageView.setImageBitmap(bitmap2);
			} else {
				mHolder.mImageView
						.setImageResource(R.drawable.icon_default_medal);
			}
		}

		return convertView;
	}

	class Holder {
		private TextView mTextView1;// 勋章名称

		private TextView mTextView2;// 勋章描述

		private ImageView mImageView;
	}

}
