package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.flow.FlowPriceInfo;

import java.util.ArrayList;

/**
 * Created by marller on 2018\4\11 0011.
 */

public class PackageTypeAdapter extends BaseAdapter {

	private Context                  context;
	private ArrayList<String>        mTrafficTypeInfo;
	private LayoutInflater           inflater;
	private ArrayList<FlowPriceInfo> priceDatas;

	public PackageTypeAdapter(Context context,
			ArrayList<String> mTrafficTypeInfo,
			ArrayList<FlowPriceInfo> priceDatas) {
		this.context = context;
		this.mTrafficTypeInfo = mTrafficTypeInfo;
		this.priceDatas = priceDatas;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mTrafficTypeInfo == null ? 0 : mTrafficTypeInfo.size();
	}

	@Override
	public String getItem(int position) {
		return mTrafficTypeInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ArrayList<FlowPriceInfo> getFlowPriceDataList() {
		return priceDatas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		String mTrafficTypeInfo = getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.package_type_model, null);
			vh = new ViewHolder();
			vh.packageType = (TextView) convertView
					.findViewById(R.id.package_type);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
			vh.packageType
					.setBackgroundResource(R.drawable.btn_traffic_selected);
			vh.packageType.setTextColor(Color.WHITE);
		} else {
			vh.packageType
					.setBackgroundResource(R.drawable.btn_traffic_selected_no);
			vh.packageType.setTextColor(Color.parseColor("#666666"));
		}
		// vh.packageType.setText(htmlTextParse(context.getResources().getString(R.string.traffic_buy_info),
		// mTrafficTypeInfo.getName(), mTrafficTypeInfo.getPrice()));
		vh.packageType.setText(mTrafficTypeInfo);
		return convertView;
	}

	static class ViewHolder {
		TextView packageType;
	}

	private String htmlTextParse(String htmlText, String size, float price) {
		String content = Html.fromHtml(htmlText).toString();
		String formatedContent = String.format(content, size, price);
		return formatedContent.replace("\\n", "\n");
	}

	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}
}
