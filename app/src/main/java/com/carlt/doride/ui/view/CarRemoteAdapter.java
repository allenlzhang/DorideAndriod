package com.carlt.doride.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.remote.RemoteFunInfo;

import java.util.ArrayList;


public class CarRemoteAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<RemoteFunInfo> mRemoteFunInfos;

    public CarRemoteAdapter(Context context, ArrayList<RemoteFunInfo> mRemoteFunInfos) {
        mInflater = LayoutInflater.from(context);
        this.mRemoteFunInfos = mRemoteFunInfos;
    }

    public void setmRemoteFunInfos(ArrayList<RemoteFunInfo> mRemoteFunInfos) {
        this.mRemoteFunInfos = mRemoteFunInfos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRemoteFunInfos.size();
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
            convertView = mInflater.inflate(R.layout.list_item_car_remote, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_car_remote_txt_name);
            mHolder.mImgIcon = (ImageView)convertView.findViewById(R.id.item_car_remote_img_icon);
        } else {
            mHolder = (Holder)convertView.getTag();
        }
        RemoteFunInfo info = mRemoteFunInfos.get(position);
        mHolder.mTxtName.setText(info.getName());
        mHolder.mImgIcon.setImageResource(info.getIcon_id());
        return convertView;
    }

    class Holder {
        private TextView mTxtName;// 名称

        private ImageView mImgIcon;// 图标
    }
}