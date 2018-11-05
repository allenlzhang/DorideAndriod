
package com.carlt.chelepie.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.carlt.chelepie.data.recorder.PieAttrInfo;
import com.carlt.doride.R;
import com.carlt.doride.eventbus.FullActivityProssEvent;
import com.carlt.doride.utils.MyParse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * 我的车乐拍-下载列表
 * 
 * @author Administrator
 */
public class PieAttrAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<PieAttrInfo> mDataList;

    public PieAttrAdapter(Context context, ArrayList<PieAttrInfo> mDataList) {
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_pieattr, null);
            convertView.setTag(mHolder);

            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_pieattr_txt_config);
            mHolder.mSeekBar = (SeekBar)convertView.findViewById(R.id.item_pieattr_sb_config);

        } else {
            mHolder = (Holder)convertView.getTag();
        }

        if (position < getCount()) {

            final PieAttrInfo mPieAttrInfo = mDataList.get(position);
            String s;
            s = mPieAttrInfo.getName();
            if (s != null && s.length() > 0) {
                mHolder.mTxtName.setText(s);
            } else {
                mHolder.mTxtName.setText("*");
            }

			s = mPieAttrInfo.getValue() + "";
            if (s != null && s.length() > 0) {
                int pro = MyParse.parseInt(s);
                mHolder.mSeekBar.setProgress(pro);
            } else {
                mHolder.mSeekBar.setProgress(0);
            }

            mHolder.mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mPieAttrInfo.setValue(seekBar.getProgress());
                    EventBus.getDefault().post(new FullActivityProssEvent(0));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }
            });
        }

        return convertView;
    }

    class Holder {
        private TextView mTxtName;// 文件名

        private SeekBar mSeekBar;// seekbar

    }
}
