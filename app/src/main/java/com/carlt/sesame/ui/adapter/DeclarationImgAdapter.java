package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.carlt.sesame.R;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 故障申报图片Adapter
 * 
 * @author daisy
 */
public class DeclarationImgAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private AsyncImageLoader mAsyncImageLoader;

	private ArrayList<String> mDataList;

	public DeclarationImgAdapter(Context context, ArrayList<String> dataList) {
		mInflater = LayoutInflater.from(context);
		mDataList = dataList;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.list_item_declaration_img,
					null);
			convertView.setTag(mHolder);
			mHolder.mImg = (ImageView) convertView
					.findViewById(R.id.list_item_declaration_img_img);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		String imgUrl = mDataList.get(position);
		if (imgUrl != null && imgUrl.length() > 0) {
			Bitmap bitmap1 = mAsyncImageLoader.getBitmapByUrl(imgUrl);
			if (bitmap1 != null) {
				mHolder.mImg.setImageBitmap(bitmap1);
			} else {
				// 设置默认图片
				mHolder.mImg.setBackgroundColor(Color.argb(0, 246, 246, 246));
			}
		} else {
			// 设置默认图片
			mHolder.mImg.setBackgroundColor(Color.argb(0, 246, 246, 246));
		}
		return convertView;
	}

	class Holder {
		private ImageView mImg;// 故障图片
	}
}
