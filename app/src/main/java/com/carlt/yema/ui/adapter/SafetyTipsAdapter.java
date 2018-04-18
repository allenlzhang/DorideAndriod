
package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.car.SecretaryMessageInfo;

import java.util.ArrayList;

/**
 * 安防提醒Adapter
 * 
 * @author daisy
 */
public class SafetyTipsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<SecretaryMessageInfo> mDataList;

    public SafetyTipsAdapter(Context context, ArrayList<SecretaryMessageInfo> dataList) {
        mInflater = LayoutInflater.from(context);

        mDataList = dataList;
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_car_safety, null);
            convertView.setTag(mHolder);
            mHolder.mTextName = (TextView)convertView.findViewById(R.id.item_car_safety_txt_name);
            mHolder.mTextTime = (TextView)convertView.findViewById(R.id.item_car_safety_txt_time);
            mHolder.mTextDes = (TextView)convertView.findViewById(R.id.item_car_safety_txt_des);

        } else {
            mHolder = (Holder)convertView.getTag();
        }
        SecretaryMessageInfo mInfo = mDataList.get(position);

        mHolder.mTextName.setText(mInfo.getTitle());
        mHolder.mTextDes.setText(mInfo.getContent());
        mHolder.mTextTime.setText(mInfo.getCreatedate());

        return convertView;
    }

    class Holder {
        private TextView mTextName;// 安防名称

        private TextView mTextTime;// 安防时间

        private TextView mTextDes;// 安防描述

    }

}
