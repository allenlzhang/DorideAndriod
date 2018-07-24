
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.car.ProvinceInfo;

import java.util.ArrayList;

/**
 * 违章查询设置省份信息Adapter
 * 
 * @author daisy
 */
public class QueryIllegalSelectProvinceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<ProvinceInfo> mDataList;

    public QueryIllegalSelectProvinceAdapter(Context context, ArrayList<ProvinceInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
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

        convertView = mInflater.inflate(R.layout.grid_item_illegal_select, null);
        TextView mTextView = (TextView)convertView.findViewById(R.id.grid_item_illegal_select_txt);

        ProvinceInfo info = mDataList.get(position);
        if (info.getName() != null) {
        	String name = info.getName();
        	if(name.length() > 4){
        		name = name.substring(0, 4);
        	}
            mTextView.setText(name);
        }
        mTextView.setTag(info);

        return convertView;
    }

}
