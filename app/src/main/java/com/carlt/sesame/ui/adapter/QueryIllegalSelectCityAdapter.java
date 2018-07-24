
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.car.CityInfo;

import java.util.ArrayList;

/**
 * 违章查询设置城市信息Adapter
 * 
 * @author daisy
 */
public class QueryIllegalSelectCityAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<CityInfo> mDataList;

    public void setmDataList(ArrayList<CityInfo> mDataList) {
        this.mDataList = mDataList;
    }

    public QueryIllegalSelectCityAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        convertView = mInflater.inflate(R.layout.grid_item_illegal_select_city, null);
        TextView mTextView = (TextView)convertView.findViewById(R.id.grid_item_illegal_select_txt);

        CityInfo info = mDataList.get(position);
        if (info.getName() != null) {
//        	String name = info.getName();
//        	if(name.length() > 4){
//        		name = name.substring(0, 4);
//        	}
            mTextView.setText(info.getName());
        }
        mTextView.setTag(info);

        return convertView;
    }

}
