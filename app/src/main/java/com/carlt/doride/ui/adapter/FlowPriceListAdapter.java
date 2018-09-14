package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.flow.FlowPriceInfo;

import java.util.List;


/**
 * 流量价格Adapter
 * @author daisy
 */
public class FlowPriceListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<FlowPriceInfo> mDataList;

    private Context mContext;

    String text;

    public FlowPriceListAdapter(Context context, List<FlowPriceInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        setmList(dataList);
    }

    public void setmList(List<FlowPriceInfo> mList) {
        this.mDataList = mList;
    }

    public void setText(String s) {
        this.text = s;
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public FlowPriceInfo getItem(int position) {
        if (mDataList != null && mDataList.size() > 0) {
            return mDataList.get(position);
        }
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
            convertView = mInflater.inflate(R.layout.adapter_item_flow_price,
                    null);
            convertView.setTag(mHolder);
            mHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            mHolder.tvDes = (TextView) convertView.findViewById(R.id.tvDes);
            mHolder.tvRecharge = (TextView) convertView.findViewById(R.id.tvRecharge);
            mHolder.tvFlowInfo = (TextView) convertView
                    .findViewById(R.id.tvFlowInfo);

        } else {
            mHolder = (Holder) convertView.getTag();
        }
        final int pos = position;
        FlowPriceInfo info = mDataList.get(position);
        mHolder.tvPrice.setText(info.price);
        mHolder.tvDes.setText(info.packDes);
        mHolder.tvFlowInfo.setText(info.flowTerm);
        if (mClickListener != null) {
            mHolder.tvRecharge.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    mClickListener.onTvRechargeClick(mDataList, pos);
                }
            });
        }

        return convertView;
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
        notifyDataSetChanged();
    }

    private TvRechargeClickListener mClickListener;

    public void setOnTvRechargeListener(TvRechargeClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    static class Holder {

        private TextView tvPrice;// 价格

        private TextView tvDes;// 价格描述
        private TextView tvFlowInfo;// 套餐描述
        private TextView tvRecharge;// 套餐描述

    }

    public interface TvRechargeClickListener {
        void onTvRechargeClick(List<FlowPriceInfo> mDataList, int pos);

    }

}
