
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.community.SOSInfo;

import java.util.ArrayList;

/**
 * 申报纪录页面Adapter
 * 
 * @author daisy
 */
public class DeclarationListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<SOSInfo> mDataList;

    private Resources mResources;

    public DeclarationListAdapter(Context context, ArrayList<SOSInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        mResources = context.getResources();
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    public void setmDataList(ArrayList<SOSInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_declaration, null);

            mHolder.mTxtTime = (TextView)convertView
                    .findViewById(R.id.list_item_declaration_txt_time);
            mHolder.mTxtStatus = (TextView)convertView
                    .findViewById(R.id.list_item_declaration_txt_status);
            mHolder.mTxtContent = (TextView)convertView
                    .findViewById(R.id.list_item_declaration_txt_content);
            mHolder.mTxtPic = (TextView)convertView
                    .findViewById(R.id.list_item_declaration_txt_pic);

            mHolder.mImgPos = (ImageView)convertView
                    .findViewById(R.id.list_item_declaration_img_pos);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        SOSInfo mInfo = mDataList.get(position);
        String s;

        s = mInfo.getCreate_time();
        if (s != null && s.length() > 0) {
            mHolder.mTxtTime.setText(s);
        }

        int i;
        i = mInfo.getState();
        if (i == SOSInfo.STATE_WAIT) {
            mHolder.mTxtStatus.setText("等待4S店回复");
            mHolder.mTxtStatus.setTextColor(mResources.getColor(R.color.text_color_red_rose));
        } else if (i == SOSInfo.STATE_REPLYED) {
            mHolder.mTxtStatus.setText(" 4S店已回复");
            mHolder.mTxtStatus.setTextColor(mResources.getColor(R.color.text_color_green));
        }

        s = mInfo.getInfo();
        if (s != null && s.length() > 0) {
            mHolder.mTxtContent.setText(s);
        }

        i = mInfo.getNeed_sos();
        if (i == SOSInfo.SOS_YES) {
            mHolder.mImgPos.setVisibility(View.VISIBLE);
        } else if (i == SOSInfo.SOS_NO) {
            mHolder.mImgPos.setVisibility(View.INVISIBLE);
        }

        i = mInfo.getImagesCount();
        if (i > 0) {
            mHolder.mTxtPic.setVisibility(View.VISIBLE);
            mHolder.mTxtPic.setText(i + "");
        } else {
            mHolder.mTxtPic.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class Holder {

        private TextView mTxtTime;// 申报时间

        private TextView mTxtStatus;// 申报状态

        private TextView mTxtContent;// 申报内容

        private TextView mTxtPic;// 图片数量

        private ImageView mImgPos;// 位置图标

    }

}
