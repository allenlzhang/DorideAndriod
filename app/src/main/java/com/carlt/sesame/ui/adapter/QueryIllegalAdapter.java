
package com.carlt.sesame.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.ViolationInfo;

import java.util.ArrayList;

/**
 * 违章查询Adapter
 * 
 * @author daisy
 */
public class QueryIllegalAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private Activity mActivity;

    private ArrayList<ViolationInfo> mDataList;

    private int size;

    private OnBtnClickListener mBtnClickListener;

    public QueryIllegalAdapter(Activity context, ArrayList<ViolationInfo> dataList,
            OnBtnClickListener btnClickListener) {
        mInflater = LayoutInflater.from(context);
        mActivity = context;
        mDataList = dataList;
        mBtnClickListener = btnClickListener;
        if (mDataList != null) {
            size = mDataList.size();
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null)
            return mDataList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_query_illegal, null);
            convertView.setTag(mHolder);
            mHolder.mTextView1 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt1);
            mHolder.mTextView2 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt2);
            mHolder.mTextView3 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt3);
            mHolder.mTextView4 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt4);
            mHolder.mTextView5 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt5);
            mHolder.mTextView6 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt6);
            mHolder.mTextView7 = (TextView)convertView
                    .findViewById(R.id.list_item_query_illegal_txt7);

            mHolder.mImgState = (ImageView)convertView
                    .findViewById(R.id.list_item_query_illegal_img_state);

            mHolder.mView = convertView.findViewById(R.id.list_item_query_illegal_relative);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        final ViolationInfo mViolationInfo = mDataList.get(position);
        if (mViolationInfo.getArea() != null) {
            mHolder.mTextView1.setText(mViolationInfo.getArea() + "");
        }
        if (mViolationInfo.getAct() != null) {
            mHolder.mTextView2.setText(mViolationInfo.getAct() + "");
        }
        if (mViolationInfo.getDate() != null) {
            mHolder.mTextView3.setText(mViolationInfo.getDate() + "");
        }
        // if (mViolationInfo.getFen() != null) {
        // mHolder.mTextView4.setText("扣" + mViolationInfo.getFen() + "分");
        // }
        // if (mViolationInfo.getMoney() != null) {
        // mHolder.mTextView5.setText("罚" + mViolationInfo.getMoney() + "元");
        // }

        if (mViolationInfo.getHandled().equals(ViolationInfo.HANDLED)) {
            // 已处理
            mHolder.mImgState.setImageResource(R.drawable.icon_handled);
        } else if (mViolationInfo.getHandled().equals(ViolationInfo.HANDLED_NO)) {
            // 未处理
            mHolder.mImgState.setImageResource(R.drawable.icon_unhandled);
        }

        mHolder.mTextView6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBtnClickListener.onClickInfo(mViolationInfo);
            }
        });

        mHolder.mTextView7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBtnClickListener.onClickShare(mViolationInfo, mHolder.mView);

            }
        });

        return convertView;
    }

    class Holder {
        private TextView mTextView1;// 违章地点

        private TextView mTextView2;// 违章描述

        private TextView mTextView3;// 违章日期

        private TextView mTextView4;// 违章扣分

        private TextView mTextView5;// 违章罚款

        private TextView mTextView6;// 点击跳转至地图

        private TextView mTextView7;// 点击吐槽

        private ImageView mImgState;// 处理状态

        private View mView;// 违章信息描述layout
    }

    public interface OnBtnClickListener {
        void onClickInfo(ViolationInfo violationInfo);

        void onClickShare(ViolationInfo violationInfo, View shareView);
    }
}
