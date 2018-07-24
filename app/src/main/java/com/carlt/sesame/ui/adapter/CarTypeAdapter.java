
package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 车型Adapter
 * 
 * @author daisy
 */
public class CarTypeAdapter extends BaseAdapter {

    private ArrayList<CarModeInfo> mDataList;

    private LayoutInflater mInflater;

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    public void setmDataList(ArrayList<CarModeInfo> mDataList) {
        this.mDataList = mDataList;
    }

    public CarTypeAdapter(Context context, ArrayList<CarModeInfo> dataList) {
        mDataList = dataList;
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
            convertView = mInflater.inflate(R.layout.list_item_index_car_type, null);
            mHolder = new Holder();

            mHolder.mViewNormal = convertView.findViewById(R.id.list_item_index_car_type_layout);

            mHolder.mTextView1 = (TextView)convertView
                    .findViewById(R.id.list_item_index_car_type_txt1);
            mHolder.mTextView2 = (TextView)convertView
                    .findViewById(R.id.list_item_index_car_type_txt2);
            mHolder.mTextViewSub = (TextView)convertView
                    .findViewById(R.id.list_item_index_car_type_subtitle);
            mHolder.mImageView = (ImageView)convertView
                    .findViewById(R.id.list_item_index_car_type_img);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        CarModeInfo mInfo = mDataList.get(position);
        int type = mInfo.getType();
        switch (type) {
            case CarModeInfo.TYPE_FIRST:
                // 车型列表-一级
                mHolder.mTextViewSub.setVisibility(View.GONE);
                mHolder.mViewNormal.setVisibility(View.VISIBLE);

                mHolder.mTextView2.setText(mInfo.getTitle());
                String urlImg = mInfo.getCarlogo();
                mHolder.mImageView.setVisibility(View.VISIBLE);
                if (urlImg != null && urlImg.length() > 0) {
                    if (mAsyncImageLoader.getBitmapByUrl(urlImg) != null) {
                        mHolder.mImageView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(urlImg));
                    } else {
                        mHolder.mImageView.setImageResource(R.drawable.default_car_small);
                    }
                } else {
                    mHolder.mImageView.setImageResource(R.drawable.default_car_small);
                }
                break;

            case CarModeInfo.TYPE_SECOND:
                // 车型列表-二级
                if (mInfo.isTitleSub()) {
                    mHolder.mTextViewSub.setVisibility(View.VISIBLE);
                    mHolder.mViewNormal.setVisibility(View.GONE);

                    String sub = mInfo.getTitle();
                    if (sub != null && sub.length() > 0) {
                        mHolder.mTextViewSub.setText(sub);
                    }
                } else {
                    mHolder.mTextViewSub.setVisibility(View.GONE);
                    mHolder.mViewNormal.setVisibility(View.VISIBLE);
                    
                    mHolder.mTextView2.setText(mInfo.getTitle());
                }
                mHolder.mImageView.setVisibility(View.GONE);
                break;
            case CarModeInfo.TYPE_THIRD:
                // 车型列表-三级
                if (mInfo.isTitleSub()) {
                    mHolder.mTextViewSub.setVisibility(View.VISIBLE);
                    mHolder.mViewNormal.setVisibility(View.GONE);

                    String sub = mInfo.getTitle();
                    if (sub != null && sub.length() > 0) {
                        mHolder.mTextViewSub.setText(sub+"款");
                    }
                } else {
                    mHolder.mTextViewSub.setVisibility(View.GONE);
                    mHolder.mViewNormal.setVisibility(View.VISIBLE);
                    
                    mHolder.mTextView2.setText(mInfo.getTitle());
                }
                mHolder.mImageView.setVisibility(View.GONE);
                break;
        }

        mHolder.mTextView2.setTag(mInfo);

        return convertView;
    }

    class Holder {
        private TextView mTextView1;// 车型的title

        private TextView mTextView2;// 车型的名称

        private ImageView mImageView;// 车型图标

        private View mViewNormal;// 正常item

        private TextView mTextViewSub;// 副标题文字
    }
}
