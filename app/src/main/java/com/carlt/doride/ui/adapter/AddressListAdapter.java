package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.carlt.doride.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址结果Adapter
 * 
 * @author daisy
 */
public class AddressListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Tip> mDataList;

	private Context mContext;

	AMapLocation current;
	String text;

	public AddressListAdapter(Context context, List<Tip> dataList, AMapLocation cc) {
		mInflater = LayoutInflater.from(context);
		this.current = cc;
		setmList(dataList);
	}

	public void setmList(List<Tip> mList) {
		if(mList != null && mList.size() > 0){
			List<Tip> deletes = new ArrayList<Tip>();
			for (Tip tip : mList) {
				if(tip.getPoint() == null){
					deletes.add(tip);
				}
			}
			
			mList.removeAll(deletes);			
			this.mDataList = mList;
		}
	}

	public void setText(String s) {
		this.text = s;
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
		if (mDataList != null && mDataList.size() > 0) {
			return mDataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_address, null);
			convertView.setTag(mHolder);
			mHolder.mTxtName = (TextView) convertView.findViewById(R.id.item_address_txt_name);
			mHolder.mTxtDetail = (TextView) convertView.findViewById(R.id.item_address_txt_detail);
			mHolder.mTxtDistance = (TextView) convertView.findViewById(R.id.item_address_txt_distance);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		Tip mInfo = mDataList.get(position);
		String s = mInfo.getName();
		if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(s) && s.contains(text)) {
			s = s.replace(text, "<font color='#3d93fd'>" + text + "</font>");
			mHolder.mTxtName.setText(Html.fromHtml(s));
		} else {
			mHolder.mTxtName.setText(s + "");
		}
		mHolder.mTxtDetail.setText(mInfo.getAddress());
		LatLonPoint llp = mInfo.getPoint();
		float[] ff = new float[4];
		AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp.getLatitude(), llp.getLongitude(), ff);
		float dis = ff[0] / 1000;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		mHolder.mTxtDistance.setText(decimalFormat.format(dis) + "km");
		return convertView;
	}

	public void clear() {
		if (mDataList != null) {
			mDataList.clear();
		}
		notifyDataSetChanged();
	}

	class Holder {
		private TextView mTxtName;// 地址

		private TextView mTxtDetail;// 地址详情

		private TextView mTxtDistance;// 距离

	}
}
