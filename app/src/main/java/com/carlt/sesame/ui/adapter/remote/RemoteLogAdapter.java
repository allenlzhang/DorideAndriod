
package com.carlt.sesame.ui.adapter.remote;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.remote.RemoteLogInfo;

import java.util.ArrayList;

/**
 * 远程登录记录Adapter
 * 
 * @author daisy
 */
public class RemoteLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<RemoteLogInfo> mDataList;

    private Resources mResources;

    public RemoteLogAdapter(Context context, ArrayList<RemoteLogInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        mResources = context.getResources();
    }

    public void setmList(ArrayList<RemoteLogInfo> mList) {
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
            convertView = mInflater.inflate(R.layout.list_item_remotelog, null);
            convertView.setTag(mHolder);
            mHolder.mImgIcon = (ImageView)convertView.findViewById(R.id.item_remotelog_img_icon);
            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_remotelog_txt_name);
            mHolder.mTxtResult = (TextView)convertView.findViewById(R.id.item_remotelog_txt_result);
            mHolder.mTxtDevice = (TextView)convertView.findViewById(R.id.item_remotelog_txt_device);
            mHolder.mTxtTime = (TextView)convertView.findViewById(R.id.item_remotelog_txt_time);

        } else {
            mHolder = (Holder)convertView.getTag();
        }

        RemoteLogInfo mInfo = mDataList.get(position);
        String s;
        int type = mInfo.getLogtype();
        Log.e("info", "type==" + type);
        if (type > 0) {
            mHolder.mTxtName.setText(mInfo.getNames().get(type));
            Integer imgId;
            if (mInfo.getIcons().get(type) != null) {
               imgId = mInfo.getIcons().get(type);
               if (imgId > 0) {
                   mHolder.mImgIcon.setImageDrawable(mResources.getDrawable(imgId));
               }
            } 

        } else {
            mHolder.mTxtName.setText("--");
        }


        s = mInfo.getResult();
        if (s != null) {
            if (s.equals(RemoteLogInfo.result_success)) {
                mHolder.mTxtResult.setText("操作成功");
                mHolder.mTxtResult.setBackgroundResource(R.drawable.text_bg_green);
            } else {
                mHolder.mTxtResult.setText("操作失败");
                mHolder.mTxtResult.setBackgroundResource(R.drawable.text_bg_red);
            }
        }
        s = mInfo.getDevice_name();
        if (s != null && s.length() > 0) {
            mHolder.mTxtDevice.setText(s);
        } else {
            mHolder.mTxtDevice.setText("--");
        }

        s = mInfo.getLogtime();
        if (s != null && s.length() > 0) {
            mHolder.mTxtTime.setText(s);
        } else {
            mHolder.mTxtTime.setText("--");
        }
        return convertView;
    }

    class Holder {
        private ImageView mImgIcon;// 操作图标

        private TextView mTxtName;// 操作名称

        private TextView mTxtResult;// 操作结果

        private TextView mTxtDevice;// 操作来源

        private TextView mTxtTime;// 操作时间

    }
}
