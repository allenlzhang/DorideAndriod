package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.car.CarModeInfo;

import java.util.ArrayList;

/**
 * Created by marller on 2018\3\30 0030.
 */

public class CarModeAdapter extends BaseAdapter {

    private ArrayList<CarModeInfo> modeInfos;
    private Context context;
    private LayoutInflater inflate;

    public CarModeAdapter(Context context, ArrayList<CarModeInfo> modeInfos) {
        this.modeInfos = modeInfos;
        this.context=context;
        inflate=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return modeInfos.size();
    }

    @Override
    public CarModeInfo getItem(int i) {
        return modeInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        CarModeInfo carModeInfo=getItem(i);
        if (view == null) {
            view = inflate.inflate(R.layout.car_mode_item_layout, null);
            vh = new ViewHolder();
            vh.carMode = view.findViewById(R.id.car_mode_txt);
            view.setTag(vh);
        } else {
            vh= (ViewHolder) view.getTag();
        }
        vh.carMode.setText(carModeInfo.getTitle());
        return view;
    }

    static class ViewHolder{
        TextView carMode;
    }
}
