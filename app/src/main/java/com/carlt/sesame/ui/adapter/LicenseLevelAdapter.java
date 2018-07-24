package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 驾驶证等级Adapter
 * 
 * @author daisy
 */
public class LicenseLevelAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<LicenceLevelInfo> mDataList;

	private AsyncImageLoader mAsyncImageLoader;

	private int mDataListSize = 0;

	private int mLevel;// 驾驶证等级

	private Resources mResources;

	private String url;

	public LicenseLevelAdapter(Context context,
                               ArrayList<LicenceLevelInfo> dataList, int level) {
		mInflater = LayoutInflater.from(context);
		mDataList = dataList;
		mLevel = level;
		mResources = context.getResources();

		mAsyncImageLoader = AsyncImageLoader.getInstance();
		if (mDataList != null)
			mDataListSize = mDataList.size();
	}

	@Override
	public int getCount() {
		return mDataListSize;
	}

	@Override
	public Object getItem(int position) {
		if (mDataList != null) {
			return mDataList.get(position);
		}
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
			convertView = mInflater.inflate(R.layout.list_item_license_level,
					null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_license_level_text1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.list_item_license_level_text2);
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.list_item_license_level_img);
			mHolder.mView1 = convertView
					.findViewById(R.id.list_item_license_level_line1);
			mHolder.mView2 = convertView
					.findViewById(R.id.list_item_license_level_line2);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		LicenceLevelInfo licenceLevelInfo = mDataList.get(position);
		mHolder.mTextView1.setText(licenceLevelInfo.getName());
		mHolder.mTextView2.setText("升级条件：积分" + licenceLevelInfo.getPoint()
				+ "以上");

		if (position < mLevel) {
			url = licenceLevelInfo.getIconUrl2();
			mHolder.mView1.setBackgroundColor(mResources
					.getColor(R.color.blue_light));
			mHolder.mView2.setBackgroundColor(mResources
					.getColor(R.color.blue_light));
		} else if (position == mLevel) {
			url = licenceLevelInfo.getIconUrl1();
			mHolder.mView1.setBackgroundColor(mResources
					.getColor(R.color.blue_light));
			mHolder.mView2
					.setBackgroundColor(mResources.getColor(R.color.gray));
		} else {
			url = licenceLevelInfo.getIconUrl1();
			mHolder.mView1
					.setBackgroundColor(mResources.getColor(R.color.gray));
			mHolder.mView2
					.setBackgroundColor(mResources.getColor(R.color.gray));
		}

		Bitmap bitmap1 = mAsyncImageLoader.getBitmapByUrl(url);
		if (bitmap1 != null) {
			mHolder.mImageView.setImageBitmap(bitmap1);
		} else {
			mHolder.mImageView.setImageResource(R.drawable.level_default_l);
		}

		return convertView;
	}

	class Holder {
		private TextView mTextView1;// 等级序列

		private TextView mTextView2;// 等级名称

		private ImageView mImageView;// 等级Icon

		private View mView1;// 蓝色线上半部分

		private View mView2;// 蓝色线下半部分
	}

}
