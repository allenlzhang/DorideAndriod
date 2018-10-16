package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.CheckFaultInfo;

import java.util.ArrayList;

/**
 * 远程诊断故障问题Adapter
 * @author daisy
 *
 */
public class RemoteFaultAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<CheckFaultInfo> mDataList;

	public RemoteFaultAdapter(Context context,
			ArrayList<CheckFaultInfo> dataList) {
		mInflater = LayoutInflater.from(context);
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		if(mDataList!=null){
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
			convertView = mInflater.inflate(R.layout.list_item_remote, null);
			convertView.setTag(mHolder);
			mHolder.mTextView1 = (TextView) convertView
					.findViewById(R.id.list_item_remote_txt);

		} else {
			mHolder = (Holder) convertView.getTag();
		}
		CheckFaultInfo info = mDataList.get(position);
		StringBuffer mStringBuffer=new StringBuffer();

		mStringBuffer.append("[");
		mStringBuffer.append(info.getScope());
		mStringBuffer.append("]");
		mStringBuffer.append("故障代码：");
		mStringBuffer.append(info.getCode());
		mStringBuffer.append(" ");
		mStringBuffer.append(info.getCn());
		mStringBuffer.append(",");
		mStringBuffer.append(info.getContent());
		
		mHolder.mTextView1.setText(mStringBuffer);
		return convertView;
	}

	class Holder {
		private TextView mTextView1;
	}
}
