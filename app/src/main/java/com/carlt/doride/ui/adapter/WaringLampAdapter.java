package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.car.WaringLampItemInfo;

import java.util.List;

/**
 * Created by Marlon on 2018/3/31.
 */

public class WaringLampAdapter extends BaseAdapter {
    private Context                  mContext;
    private List<WaringLampItemInfo> mList;

    private LayoutInflater inflater;

    public WaringLampAdapter(Context context, List<WaringLampItemInfo> mlist) {
        this.mContext = context;
        this.mList = mlist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.main_test_item, null);
            viewHolder.ivIcon = view.findViewById(R.id.ivIcon);
            viewHolder.ivState = view.findViewById(R.id.ivState);
            viewHolder.tvName = view.findViewById(R.id.tvName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        WaringLampItemInfo info = mList.get(i);
        if (TextUtils.isEmpty(info.getTxt())) {
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }
        viewHolder.ivState.setImageResource(info.getIconState());
        viewHolder.ivIcon.setImageResource(info.icon);
        viewHolder.tvName.setText(info.getTxt());
//        viewHolder.tvName.setTextColor(mContext.getColor());
        return view;
    }

    static class ViewHolder {
        ImageView ivIcon;
        ImageView ivState;
        TextView  tvName;
    }

}
