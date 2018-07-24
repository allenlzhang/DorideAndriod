package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.car.HelpPhoneInfo;

import java.util.ArrayList;

/**
 * 求援Adapter
 * 
 * @author daisy
 */
public class ForHelpAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<HelpPhoneInfo> mHelpPhoneInfoList;

	public ForHelpAdapter(Context context,
			ArrayList<HelpPhoneInfo> mHelpPhoneInfoList) {
		mInflater = LayoutInflater.from(context);
		this.mHelpPhoneInfoList = mHelpPhoneInfoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHelpPhoneInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_for_help, null);
			convertView.setTag(mHolder);
			mHolder.lay = convertView.findViewById(R.id.list_item_for_help_lay);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_for_help_txt1);
			mHolder.mTextView2 = (TextView) convertView
					.findViewById(R.id.list_item_for_help_txt2);
			mHolder.mTextView3 = (TextView) convertView
					.findViewById(R.id.list_item_for_help_txt3);

			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.list_item_for_help_img);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		HelpPhoneInfo info = mHelpPhoneInfoList.get(arg0);
		if (info.isFlag()) {
			mHolder.lay.setVisibility(View.GONE);
			mHolder.mTextView1.setVisibility(View.VISIBLE);
			mHolder.mTextView1.setText(info.getName());
		} else {

			mHolder.lay.setVisibility(View.VISIBLE);
			mHolder.mTextView1.setVisibility(View.GONE);
			mHolder.mTextView2.setText(info.getName());
			String phone=info.getPhone();
			if(phone==null){
			    phone="";
			}
			mHolder.mTextView3.setText(info.getPhone());
		}

		return convertView;
	}

	class Holder {
		private TextView mTextView1;// 求援类别

		private TextView mTextView2;// 求援名称

		private TextView mTextView3;// 求援电话

		private ImageView mImageView;// 拨打电话图标

		private View lay;
	}

}
