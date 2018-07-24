
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.utility.GetTypeFace;

import java.util.ArrayList;

/**
 * 挑战关卡adapter（新版）
 * 
 * @author daisy
 */
public class ChallengePassesAdapterNew extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<ChallengeInfo> mDataList;

    private OnChallengeItemListener mItemListener;

    public void setmDataList(ArrayList<ChallengeInfo> mDataList) {
        if (mDataList != null) {
            this.mDataList = mDataList;
            notifyDataSetChanged();
        }
    }

    public ChallengePassesAdapterNew(Context context, OnChallengeItemListener itemListener) {
        mInflater = LayoutInflater.from(context);
        mItemListener = itemListener;
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
        final Holder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_challenge_new, null);
            mHolder = new Holder();
            mHolder.mTxtNum = (TextView)convertView.findViewById(R.id.item_challenge_txt_name);
            mHolder.mTxtDes = (TextView)convertView.findViewById(R.id.item_challenge_txt_des);
            mHolder.mImg = (ImageView)convertView.findViewById(R.id.item_challenge_img_icon);
            mHolder.mImgCup = (ImageView)convertView.findViewById(R.id.item_challenge_img_cup);

            mHolder.mView = convertView.findViewById(R.id.item_challenge_layout);
            mHolder.mViewNull = convertView.findViewById(R.id.item_challenge_view_null);
            mHolder.mViewSelected = convertView.findViewById(R.id.item_challenge_view_selected);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        int i = position % 2;
        final ChallengeInfo mInfo = mDataList.get(position);

        if (mInfo.isNull()) {
            mHolder.mView.setVisibility(View.GONE);
            mHolder.mViewNull.setVisibility(View.VISIBLE);
            mHolder.mViewSelected.setVisibility(View.GONE);

            mHolder.mViewNull.setOnClickListener(null);

        } else {
            mHolder.mView.setVisibility(View.VISIBLE);
            mHolder.mViewNull.setVisibility(View.GONE);

            mHolder.mTxtNum.setTypeface(GetTypeFace.typefaceBold(null));
            mHolder.mTxtNum.setText("第" + mInfo.getSort() + "关");

            String des = mInfo.getName();
            String status = mInfo.getStatus();

            mHolder.mView.setOnClickListener(null);
            int type = mInfo.getType();
            if (status.equals(ChallengeInfo.STATUS_UNLOCKED)) {
                // 未解锁
                mHolder.mImgCup.setVisibility(View.GONE);
                mHolder.mImg.setImageResource(R.drawable.challenge_lock);
                mHolder.mTxtDes.setVisibility(View.GONE);

                mHolder.mViewSelected.setVisibility(View.GONE);

            } else {
                mHolder.mViewSelected.setVisibility(View.VISIBLE);
                mHolder.mViewSelected.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mItemListener.onItemClick(mInfo);
                    }
                });
                if (status.equals(ChallengeInfo.STATUS_FINISHED)) {
                    // 挑战已成功
                    mHolder.mImgCup.setVisibility(View.VISIBLE);
                } else if (status.equals(ChallengeInfo.STATUS_UNFINISHED)) {
                    // 挑战中
                    mHolder.mImgCup.setVisibility(View.GONE);
                }
                switch (type) {
                    case ChallengeInfo.TYPE_OIL:
                        // 省油模式:定油量,挑战里程
                        if (i == 0) {
                            // 偶数显示绿色图标
                            mHolder.mImg.setImageResource(R.drawable.challenge_oil_green);
                        } else {
                            // 奇数显示红色图标
                            mHolder.mImg.setImageResource(R.drawable.challenge_oil_red);
                        }

                        break;

                    case ChallengeInfo.TYPE_ACCELERATE:
                        // 加速模式:定车速,挑战加速时间
                        if (i == 0) {
                            mHolder.mImg.setImageResource(R.drawable.challenge_accelerate_green);
                        } else {
                            mHolder.mImg.setImageResource(R.drawable.challenge_accelerate_red);
                        }
                        break;
                    case ChallengeInfo.TYPE_TIME:
                        // 省时模式:定行程,挑战行驶时间
                        if (i == 0) {
                            mHolder.mImg.setImageResource(R.drawable.challenge_time_green);
                        } else {
                            mHolder.mImg.setImageResource(R.drawable.challenge_time_red);
                        }
                        break;
                    case ChallengeInfo.TYPE_SCORE:
                        // 评分模式:定行程,挑战驾驶评分
                        if (i == 0) {
                            mHolder.mImg.setImageResource(R.drawable.challenge_score_green);
                        } else {
                            mHolder.mImg.setImageResource(R.drawable.challenge_score_red);
                        }
                        break;
                }

                mHolder.mTxtDes.setVisibility(View.VISIBLE);
                mHolder.mTxtDes.setText(des);
            }
        }
        return convertView;
    }

    class Holder {
        private TextView mTxtNum;// 关卡-数字

        private TextView mTxtDes;// 关卡-描述

        private ImageView mImg;// 关卡-图标

        private ImageView mImgCup;// 奖杯图标

        private View mView;// 有数据时的View

        private View mViewNull;// 无数据的view

        private View mViewSelected;// 选择后的View

    }

    public interface OnChallengeItemListener {
        void onItemClick(ChallengeInfo info);
    };
}
