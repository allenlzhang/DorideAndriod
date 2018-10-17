
package com.carlt.chelepie.view.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.doride.R;

import java.util.List;

/**
 * 系统wifiAdapter
 * 
 * @author daisy
 */
public class WifiInfoAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<ScanResult> mDataList;

	public WifiInfoAdapter(Context context, List<ScanResult> dataList) {
		mInflater = LayoutInflater.from(context);
		this.mDataList = dataList;
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
		return mDataList.get(position);
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
			convertView = mInflater.inflate(R.layout.list_item_wifi, null);
			convertView.setTag(mHolder);

			mHolder.mTxtSSID = (TextView) convertView.findViewById(R.id.item_wifi_txtssid);
			mHolder.mImgIntensity = (ImageView) convertView.findViewById(R.id.item_wifi_img);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		ScanResult mInfo = mDataList.get(position);
		String s = mInfo.SSID;
		if (s != null && s.length() > 0) {
			mHolder.mTxtSSID.setText(s);
		}
		int level = mInfo.level;
		if (Math.abs(level) >= 100) {
			mHolder.mImgIntensity.setImageResource(R.drawable.wifi_lock_one);
		} else if (Math.abs(level) >= 50 && Math.abs(level) < 100) {
			mHolder.mImgIntensity.setImageResource(R.drawable.wifi_lock_two);
		} else {
			mHolder.mImgIntensity.setImageResource(R.drawable.wifi_lock_three);
		}
		return convertView;
	}

	class Holder {
		private TextView mTxtSSID;// wifi的SSID
		private ImageView mImgIntensity;// wifi信号强度图

	}
}
