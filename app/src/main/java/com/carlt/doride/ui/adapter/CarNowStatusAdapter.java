package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.car.CarNowStatusInfo;
import com.carlt.doride.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Marlon on 2018/3/31.
 */

public class CarNowStatusAdapter extends BaseAdapter {

    private ArrayList<CarNowStatusInfo.CarNowStatusItemInfo> mList;

    private LayoutInflater inflater;

    public CarNowStatusAdapter(Context context, ArrayList<CarNowStatusInfo.CarNowStatusItemInfo> mList) {
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CarNowStatusInfo.CarNowStatusItemInfo getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_car_now_status,null);
            viewHolder.mTxtName = view.findViewById(R.id.item_car_condition_txt_name);
            viewHolder.mTxtValue = view.findViewById(R.id.item_car_condition_txt_value);
            viewHolder.mTxtCompany = view.findViewById(R.id.item_car_condition_txt_unit);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CarNowStatusInfo.CarNowStatusItemInfo info = mList.get(i);



        if (!StringUtils.isEmpty(info.getName())) {
            viewHolder.mTxtName.setText(info.getName());
        }else {
            viewHolder.mTxtName.setText("--");
        }

        if (!StringUtils.isEmpty(info.getValue())){
            viewHolder.mTxtValue.setText(info.getValue());
        }else {
            viewHolder.mTxtValue.setText("--");
        }

        if (!StringUtils.isEmpty(info.getCompany())){
            viewHolder.mTxtCompany.setText(info.getCompany());
        }else {
            viewHolder.mTxtCompany.setText("");
    }

        return view;
    }

    class ViewHolder{
        TextView mTxtName;
        TextView mTxtValue;
        TextView mTxtCompany;
    }

}
