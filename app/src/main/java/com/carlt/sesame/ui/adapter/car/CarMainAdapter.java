package com.carlt.sesame.ui.adapter.car;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.CarMainFuncInfo;
import com.carlt.sesame.ui.adapter.CarListAdapter.OnItemBtnClick;

import java.util.ArrayList;

/**
 * 座驾主页功能Adaper
 * 
 * @author Daisy
 * 
 */
public class CarMainAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<CarMainFuncInfo> mDataList;

	private OnItemBtnClick mItemBtnClick;

	public CarMainAdapter(Context context ) {
		mInflater = LayoutInflater.from(context);

	}

	
	public void setmDataList(ArrayList<CarMainFuncInfo> mDataList) {

		this.mDataList = mDataList;
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mDataList != null) {
			return mDataList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		if(mDataList!=null){
			return mDataList.get(position).getId();
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.grid_item_carmain, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView) convertView.findViewById(R.id.carmain_txt_name);
            mHolder.mImgIcon = (ImageView)convertView.findViewById(R.id.carmain_img_icon);
            mHolder.mImgDot = (ImageView)convertView.findViewById(R.id.carmain_img_dot);
        } else {
            mHolder = (Holder)convertView.getTag();
        }
        CarMainFuncInfo mInfo=mDataList.get(position);
        String s;
        s=mInfo.getName();
        if(!TextUtils.isEmpty(s)){
        	mHolder.mTxtName.setText(s);
        }else{
        	mHolder.mTxtName.setText("");
        }
        mHolder.mImgIcon.setImageResource(mInfo.getIcon());
        if(mInfo.isShowDot()){
        	mHolder.mImgDot.setVisibility(View.VISIBLE);
        }else{
        	mHolder.mImgDot.setVisibility(View.GONE);
        }
        if( mInfo.hasPermissions ){
			mHolder.mTxtName.setTextColor(Color.BLACK);
			mHolder.mImgIcon.setAlpha(255);
		}else {
			mHolder.mTxtName.setTextColor(Color.parseColor("#999999"));
            mHolder.mImgIcon.setAlpha(120);
		}
		return convertView;
	}
	
	class Holder {

        private TextView mTxtName;// 名称

        private ImageView mImgIcon;// 图标

        private ImageView mImgDot;//红点
    }

}
