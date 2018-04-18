
package com.carlt.yema.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.data.car.CarModeInfo;

import java.util.ArrayList;

/**
 * 车型Adapter
 * 
 * @author daisy
 */
public class CarTypeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private static final int FATORY_YEAR=0;

    private static final int FATORY_DETAIL=1;

    private ArrayList<CarModeInfo> mDataList;

    public CarTypeAdapter(Context context,ArrayList<CarModeInfo> listInfo) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = listInfo;
    }

    public void setmList(ArrayList<CarModeInfo> mList) {
        this.mDataList = mList;
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public CarModeInfo getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        CarModeInfo modeInfo=getItem(position);
        if (!TextUtils.isEmpty(modeInfo.getFactory_year())){
            return FATORY_YEAR;
        }else {
            return FATORY_DETAIL;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        CarModeInfo mCarModeInfo=getItem(position);
        ViewHolder mViewHolder=null;
        ViewHolderYear mViewHolderYear=null;
        int type=getItemViewType(position);
        if (null == convertView) {
            switch (type) {
                case FATORY_YEAR:
                    convertView=mInflater.inflate(R.layout.list_item_car_factory_year,null);
                    mViewHolderYear=new ViewHolderYear();
                    mViewHolderYear.car_factory_year= (TextView) convertView.findViewById(R.id.car_factory_year);
                    convertView.setTag(mViewHolderYear);
                    break;
                case FATORY_DETAIL:
                    convertView=mInflater.inflate(R.layout.list_item_car_type,null);
                    mViewHolder=new ViewHolder();
                    mViewHolder.car_type_name= (TextView) convertView.findViewById(R.id.car_type_name);
                    convertView.setTag(mViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case FATORY_YEAR:
                    mViewHolderYear= (ViewHolderYear) convertView.getTag();
                    break;
                case FATORY_DETAIL:
                    mViewHolder= (ViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case FATORY_YEAR:
                mViewHolderYear.car_factory_year.setText(mCarModeInfo.getFactory_year());
                break;
            case FATORY_DETAIL:
                mViewHolder.car_type_name.setText(mCarModeInfo.getTitle());
                break;
        }
        return convertView;
    }

    static  class ViewHolder {
        TextView car_type_name;// 车款名称
    }
    static  class ViewHolderYear {
        TextView car_factory_year;//出厂时间
    }
}
