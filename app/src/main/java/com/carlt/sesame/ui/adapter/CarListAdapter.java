
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.CarInfo;

import java.util.ArrayList;

/**
 * 车辆列表Adapter
 * 
 * @author daisy
 */
public class CarListAdapter extends BaseAdapter {
    private Resources mResources;

    private LayoutInflater mInflater;

    private ArrayList<CarInfo> mDataList;

    private OnItemBtnClick mItemBtnClick;

    public CarListAdapter(Context context, ArrayList<CarInfo> mDataList,
            OnItemBtnClick mItemBtnClick) {
        mResources = context.getResources();
        mInflater = LayoutInflater.from(context);
        this.mDataList = mDataList;
        this.mItemBtnClick = mItemBtnClick;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDataList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_car, null);
            convertView.setTag(mHolder);
            mHolder.mViewMain = convertView.findViewById(R.id.item_car_lay_main);
            mHolder.mTxtCarno = (TextView)convertView.findViewById(R.id.item_car_txt_carno);
            mHolder.mTxtType = (TextView)convertView.findViewById(R.id.item_car_txt_type);
            mHolder.mImgDel = (ImageView)convertView.findViewById(R.id.item_car_img_del);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        final CarInfo info = mDataList.get(position);
        String carNo = info.getCarNo();
        if (carNo != null && !carNo.equals("")) {
            mHolder.mTxtCarno.setText(carNo);
        }
        String type = info.getType();
        if (type.equals(CarInfo.TYPE_MY)) {
            mHolder.mTxtType.setText("我的常用车辆");
            mHolder.mTxtType.setTextColor(Color.parseColor("#ffffff"));
            mHolder.mTxtType.setBackgroundResource(R.drawable.btn_violation_blue);
        } else if (type.equals(CarInfo.TYPE_OTHER)) {
            mHolder.mTxtType.setTextColor(mResources.getColor(R.color.text_color_blue_light));
            mHolder.mTxtType.setText("设置为我的车辆");
            mHolder.mTxtType.setBackgroundResource(R.drawable.btn_violation_bg);
        }

        OnClickListener mClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_car_txt_type:
                        // 设置为我的车辆
                        mItemBtnClick.onToBeMycar(position, info);
                        break;

                    case R.id.item_car_img_del:
                        // 删除违章车辆信息
                        mItemBtnClick.onDelete(position, info);
                        break;
                    case R.id.item_car_lay_main:
                        // 跳转至违章信息填写页面
                        mItemBtnClick.onToFill(position, info);
                        break;
                }

            }
        };
        mHolder.mImgDel.setOnClickListener(mClickListener);
        if (type.equals(CarInfo.TYPE_MY)) {
            mHolder.mTxtType.setClickable(false);
            mHolder.mTxtType.setOnClickListener(null);
        } else if (type.equals(CarInfo.TYPE_OTHER)) {
            mHolder.mTxtType.setClickable(true);
            mHolder.mTxtType.setOnClickListener(mClickListener);
        }

        mHolder.mViewMain.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mHolder.mImgDel.getVisibility() == View.VISIBLE) {
                    mHolder.mImgDel.setVisibility(View.GONE);
                } else {
                    mHolder.mImgDel.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        mHolder.mViewMain.setOnClickListener(mClickListener);

        return convertView;
    }

    class Holder {
        private View mViewMain;// item整体

        private TextView mTxtCarno;// 车牌号

        private TextView mTxtType;// 车辆状态（是车主车辆，是他人车辆）

        private ImageView mImgDel;// 删除图标
    }

    public interface OnItemBtnClick {
        /**
         * 删除违章车辆信息
         */
        void onDelete(int position, CarInfo mCarInfo);

        void onToBeMycar(int position, CarInfo mCarInfo);

        void onToFill(int position, CarInfo mCarInfo);
    }

}
