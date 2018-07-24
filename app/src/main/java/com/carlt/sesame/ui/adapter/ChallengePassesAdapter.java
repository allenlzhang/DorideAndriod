
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.utility.GetTypeFace;

import java.util.ArrayList;

/**
 * 挑战关卡adapter
 * 
 * @author daisy
 */
public class ChallengePassesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<ChallengeInfo> mDataList;

    private int mColorGreen;

    private int mColorGray;

    public void setmDataList(ArrayList<ChallengeInfo> mDataList) {
        if (mDataList != null) {
            this.mDataList = mDataList;
            notifyDataSetChanged();
        }
    }

    public ChallengePassesAdapter(Context context, ArrayList<ChallengeInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        mColorGreen = context.getResources().getColor(R.color.text_color_green);
        mColorGray = context.getResources().getColor(R.color.text_color_gray2);
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
        Holder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_challenge, null);
            mHolder = new Holder();
            mHolder.mTxtNum = (TextView)convertView.findViewById(R.id.grid_item_challenge_num);
            mHolder.mTxtDes = (TextView)convertView.findViewById(R.id.grid_item_challenge_des);
            mHolder.mImg = (ImageView)convertView.findViewById(R.id.grid_item_challenge_img);
            mHolder.mView = convertView.findViewById(R.id.grid_item_challenge);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        ChallengeInfo mInfo = mDataList.get(position);

        mHolder.mTxtNum.setTypeface(GetTypeFace.typefaceBold(null));
        mHolder.mTxtNum.setText(mInfo.getSort() + "");

        mHolder.mTxtDes.setText(mInfo.getName());

        String status = mInfo.getStatus();
        if (status.equals(ChallengeInfo.STATUS_FINISHED)) {
            // 挑战已成功
            mHolder.mView.setBackgroundResource(R.drawable.challenge_item_bg_light);
            mHolder.mTxtNum.setTextColor(mColorGreen);
            mHolder.mImg.setImageResource(R.drawable.challenge_star);

        } else if (status.equals(ChallengeInfo.STATUS_UNFINISHED)) {
            // 挑战中
            mHolder.mView.setBackgroundResource(R.drawable.challenge_item_bg_light);
            mHolder.mTxtNum.setTextColor(mColorGreen);
            mHolder.mImg.setImageResource(R.drawable.challenge_star_empty);

        } else if (status.equals(ChallengeInfo.STATUS_UNLOCKED)) {
            // 未解锁
            mHolder.mView.setBackgroundResource(R.drawable.challenge_item_bg_dark);
            mHolder.mTxtNum.setTextColor(mColorGray);
            mHolder.mImg.setImageResource(R.drawable.challenge_lock);
        }
        return convertView;
    }

    class Holder {
        private TextView mTxtNum;// 关卡-数字

        private TextView mTxtDes;// 关卡-数字

        private ImageView mImg;// 关卡-底部图片

        private View mView;// 整个item的Lyaout
    }

}
