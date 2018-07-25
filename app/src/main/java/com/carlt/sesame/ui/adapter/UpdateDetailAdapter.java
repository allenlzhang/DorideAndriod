package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.usercenter.VersionLog;
import com.carlt.sesame.utility.StringUtils;

import java.util.List;

/**
 * Created by Marlon on 2018/4/9.
 */

public class UpdateDetailAdapter extends BaseAdapter{
    private List<VersionLog> list;
    private LayoutInflater inflater;

    public UpdateDetailAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<VersionLog> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.updatalog_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mTxtContent = (TextView) convertView.findViewById(R.id.update_log_txt_content);
            viewHolder.mTxtDate = (TextView) convertView.findViewById(R.id.update_log_txt_date);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VersionLog info = list.get(position);
        if (!StringUtils.isEmpty(info.getVersion())) {
            viewHolder.mTxtContent.setText("芝麻乐园" + info.getVersion() + "主要更新");
        }else {
            viewHolder.mTxtContent.setText("");
        }
        String dateString = info.getCreatedateString();
        if (!StringUtils.isEmpty(dateString)) {
            viewHolder.mTxtDate.setText(dateString);
        }else {
            viewHolder.mTxtDate.setText("");
        }
        return convertView;
    }
    private class ViewHolder{
        TextView mTxtContent;
        TextView mTxtDate;
    }



}
