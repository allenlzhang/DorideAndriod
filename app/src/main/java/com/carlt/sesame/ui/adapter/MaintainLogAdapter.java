package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.MaintainLogInfo;

import java.util.ArrayList;

/**
 * 养护提醒Adapter
 * 
 * @author daisy
 */
public class MaintainLogAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<MaintainLogInfo> mDataList;

	public MaintainLogAdapter(Context context,
			ArrayList<MaintainLogInfo> mDataList) {
		mInflater = LayoutInflater.from(context);
		this.mDataList = mDataList;
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_maintain_log,
					null);
			convertView.setTag(mHolder);
			mHolder.mImg = (ImageView) convertView
					.findViewById(R.id.item_maintain_log_img);
			mHolder.mTxtTitle = (TextView) convertView
					.findViewById(R.id.item_maintain_log_title);
			mHolder.mTxtContent = (TextView) convertView
					.findViewById(R.id.item_maintain_log_content);
			mHolder.mTxtCommend = (TextView) convertView
					.findViewById(R.id.item_maintain_log_commend);

			mHolder.titlelay = convertView
					.findViewById(R.id.item_maintain_log_titlelay);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		MaintainLogInfo mInfo = mDataList.get(position);

		mHolder.mTxtTitle.setText(mInfo.getTitle());
		mHolder.mTxtContent.setText(mInfo.getRemarks());

		if (mInfo.getIsCommend() == MaintainLogInfo.COMMEND_NO) {
			mHolder.mTxtCommend.setVisibility(View.GONE);
		} else {
			mHolder.mTxtCommend.setVisibility(View.VISIBLE);
		}

		if (mInfo.getIsOpen() == MaintainLogInfo.CLOSE) {
			mHolder.mImg.setImageResource(R.drawable.list_item_maintain_log_x);
			mHolder.mTxtContent.setVisibility(View.GONE);
		} else {
			mHolder.mImg.setImageResource(R.drawable.list_item_maintain_log_s);
			mHolder.mTxtContent.setVisibility(View.VISIBLE);
		}

		mHolder.titlelay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeOpen(position);

			}
		});

		return convertView;
	}

	private void changeOpen(int index) {
		if (mDataList != null && mDataList.size() > index) {
			if (MaintainLogInfo.CLOSE == mDataList.get(index).getIsOpen()) {
				mDataList.get(index).setIsOpen(MaintainLogInfo.OPEN);
			} else {
				mDataList.get(index).setIsOpen(MaintainLogInfo.CLOSE);
			}

		}

		notifyDataSetChanged();
	}

	class Holder {
		private ImageView mImg;// 箭头图标

		private TextView mTxtTitle;// 标题

		private TextView mTxtContent;// 內容

		private TextView mTxtCommend;// 是否推荐

		private View titlelay;

	}

}
