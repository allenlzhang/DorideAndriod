package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.car.SaftyMsgInfo;
import com.carlt.doride.data.home.InformationMessageInfo;
import com.carlt.doride.utils.StringUtils;

import java.util.List;

/**
 * Created by Marlon on 2018/3/30.
 */

public class CarSaftyAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<SaftyMsgInfo> mList;

    public CarSaftyAdapter(Context mContext, List<SaftyMsgInfo> mList) {
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SaftyMsgInfo getItem(int i) {
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
            view = inflater.inflate(R.layout.item_car_safty_list, null);
            viewHolder.mImg = view.findViewById(R.id.item_car_safty_img);
            viewHolder.mTxtTitle = view.findViewById(R.id.item_car_safty_txt_title);
            viewHolder.mTxtMsg = view.findViewById(R.id.item_car_safty_txt_msg);
            viewHolder.mTxtDate = view.findViewById(R.id.item_car_safty_txt_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SaftyMsgInfo info = mList.get(i);
        if (!StringUtils.isEmail(info.getTitle())) {
            viewHolder.mTxtTitle.setText(info.getTitle());
        } else {
            viewHolder.mTxtTitle.setText("");
        }
        if (!StringUtils.isEmpty(info.getClass2())) {
            switch (Integer.parseInt(info.getClass2())) {
                case InformationMessageInfo.C1_T2_T1:
                    viewHolder.mImg.setImageResource(R.drawable.icon_shake);
                    break;
                //                case InformationMessageInfo.C1_T2_T2:
                //                    viewHolder.mImg.setImageResource(R.drawable.icon_collision_alarm);
                //                    break;
                case InformationMessageInfo.C1_T2_T3:
                    viewHolder.mImg.setImageResource(R.drawable.icon_tire_pressure);
                    break;
                case InformationMessageInfo.C1_T2_T4:
                    viewHolder.mImg.setImageResource(R.drawable.icon_power_shortage);
                    break;
                case InformationMessageInfo.C1_T2_T5:
                    viewHolder.mImg.setImageResource(R.drawable.icon_start_the_alarm);
                    break;
                case InformationMessageInfo.C1_T2_T6:
                    viewHolder.mImg.setImageResource(R.drawable.icon_door_and_window);
                    break;
                case InformationMessageInfo.C1_T2_T7:
                    viewHolder.mImg.setImageResource(R.drawable.icon_trailer);
                    break;
                case InformationMessageInfo.C1_T2_T8:
                    viewHolder.mImg.setImageResource(R.drawable.icon_security_warning);
                    break;
                case InformationMessageInfo.C1_T2_T9:
                    viewHolder.mImg.setImageResource(R.drawable.icon_flameout);
                    break;
                case InformationMessageInfo.C1_T2_T10:
                    viewHolder.mImg.setImageResource(R.drawable.icon_collision_alarm);
                    break;
                default:
                    viewHolder.mImg.setImageResource(R.drawable.icon_security_warning);
                    break;
            }
        }

        if (!StringUtils.isEmail(info.getContent())) {
            viewHolder.mTxtMsg.setText(info.getContent());
        } else {
            viewHolder.mTxtMsg.setText("");
        }
        if (!StringUtils.isEmail(info.getCreatedate())) {
            viewHolder.mTxtDate.setText(info.getCreatedate());
        } else {
            viewHolder.mTxtDate.setText("");
        }
        return view;
    }

    static class ViewHolder {
        ImageView mImg;
        TextView  mTxtTitle;
        TextView  mTxtMsg;
        TextView  mTxtDate;
    }

}
