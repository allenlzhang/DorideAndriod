
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.set.FeeLogInfo;

import java.util.ArrayList;

/**
 * 续费记录Adapter
 * 
 * @author daisy
 */
public class FeeLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<FeeLogInfo> mDataList;

    public FeeLogAdapter(Context context, ArrayList<FeeLogInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
    }

    public void setmList(ArrayList<FeeLogInfo> mList) {
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
            convertView = mInflater.inflate(R.layout.list_item_feelog, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_feelog_txt_name);
            mHolder.mTxtDatepay = (TextView)convertView.findViewById(R.id.item_feelog_txt_datepay);
            mHolder.mTxtFee = (TextView)convertView.findViewById(R.id.item_feelog_txt_fee);
            mHolder.mTxtDatefromto = (TextView)convertView
                    .findViewById(R.id.item_feelog_txt_datefromto);

        } else {
            mHolder = (Holder)convertView.getTag();
        }

        FeeLogInfo mInfo = mDataList.get(position);
        String s;
        s = mInfo.getName();
        if (s != null && s.length() > 0) {
            mHolder.mTxtName.setText(s);
        } else {
            mHolder.mTxtName.setText("--");
        }

        s = mInfo.getDatePay();
        if (s != null) {
            mHolder.mTxtDatepay.setText(s);
        }else{
            mHolder.mTxtDatepay.setText("--");
        }

        s = mInfo.getFee();
        if (s != null && s.length() > 0) {
            mHolder.mTxtFee.setText(s);
        } else {
            mHolder.mTxtFee.setText("--");
        }

        s = mInfo.getDateFromto();
        if (s != null && s.length() > 0) {
            mHolder.mTxtDatefromto.setText(s);
        } else {
            mHolder.mTxtDatefromto.setText("--");
        }
        return convertView;
    }

    class Holder {
        private TextView mTxtName;// 手机名称

        private TextView mTxtDatepay;// 付款时间

        private TextView mTxtFee;// 金额

        private TextView mTxtDatefromto;// 服务时间（***至***）

    }
}
