
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.car.CarStatuInfo;

import java.util.ArrayList;

/**
 * 实时车况Adapter
 * 
 * @author daisy
 */
public class CarConditionListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<CarStatuInfo> mCarStatuInfoList;

    public CarConditionListAdapter(Context context, ArrayList<CarStatuInfo> mCarStatuInfoList) {
        mInflater = LayoutInflater.from(context);
        this.mCarStatuInfoList = mCarStatuInfoList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCarStatuInfoList.size();
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
            convertView = mInflater.inflate(R.layout.list_item_car_condition, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView)convertView
                    .findViewById(R.id.item_car_condition_txt_name);
            mHolder.mTxtValue = (TextView)convertView
                    .findViewById(R.id.item_car_condition_txt_value);
            mHolder.mTxtUnit = (TextView)convertView.findViewById(R.id.item_car_condition_txt_unit);
            mHolder.mLay=convertView.findViewById(R.id.item_car_condition_lay);
        } else {
            mHolder = (Holder)convertView.getTag();
        }
        CarStatuInfo info = mCarStatuInfoList.get(position);
        String s;
        s=info.getName();
        if(s!=null&&!s.equals("")&&!s.equals("null")){
        	mHolder.mTxtName.setText(s);
        }else{
        	mHolder.mTxtName.setText("--");
        }
        s=info.getValue();
        if(s!=null&&!s.equals("")&&!s.equals("null")){
        	mHolder.mTxtValue.setText(s);
        }else{
        	mHolder.mTxtValue.setText("--");
        }
        s=info.getUnit();
		// if(s!=null&&!s.equals("")&&!s.equals("null")){
		// mHolder.mTxtUnit.setText(s);
		// }else{
		// mHolder.mTxtUnit.setText("--");
		// }
        if(s!=null){
        	mHolder.mTxtUnit.setText(s);
        }
        mHolder.mLay.setOnClickListener(l);
        return convertView;
    }

    OnClickListener l = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    };

    class Holder {
        private TextView mTxtName;

        private TextView mTxtValue;

        private TextView mTxtUnit;
        
        private View mLay;
    }

}
