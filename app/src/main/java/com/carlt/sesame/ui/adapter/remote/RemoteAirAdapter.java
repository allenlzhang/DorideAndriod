package com.carlt.sesame.ui.adapter.remote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.ui.view.UUAirConditionDialog;

import java.util.ArrayList;

/**
 * 远程空调Adapter
 * 
 * @author daisy
 */
public class RemoteAirAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private Context mContext;

	private ArrayList<RemoteFunInfo> mDataList;

	public void setmDataList(ArrayList<RemoteFunInfo> mDataList) {
		this.mDataList = mDataList;
		notifyDataSetChanged();
	}

	public RemoteAirAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
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
			convertView = mInflater.inflate(R.layout.grid_remote_item_air, null);
			convertView.setTag(mHolder);
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.remote_item_air_img_icon);
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.remote_item_air_txt_name);

		} else {
			mHolder = (Holder) convertView.getTag();
		}

		if (mDataList != null) {
			RemoteFunInfo mInfo = mDataList.get(position);
			mHolder.mImageView.setImageResource(mInfo.getIcon_id());
			mHolder.mTextView.setText(mInfo.getName());
			if (mInfo.isSelect()) {
				mHolder.mTextView.setTextColor(UUAirConditionDialog.COLOR_FONT_SELECT);
				mHolder.mImageView.setImageResource(mInfo.getIcon_id_seleced());
			} else {
				mHolder.mTextView.setTextColor(UUAirConditionDialog.COLOR_FONT_NORMAL);
				mHolder.mImageView.setImageResource(mInfo.getIcon_id_seleced_no());
			}
		}

		return convertView;
	}

	class Holder {
		private ImageView mImageView;// 功能图标
		private TextView mTextView;// 功能名称
	}

}
