
package com.carlt.sesame.ui.adapter.remote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.SesameLoginInfo;

/**
 * 远程功能Adapter
 * 
 * @author daisy
 */
public class RemoteFunctionsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private Context mContext;

    private String deviceType;
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    // private final static int[] imgIds = {
    // R.drawable.horm, R.drawable.trunck, R.drawable.air_condition,
    // R.drawable.air_condition_close, R.drawable.open_aircylinder,
    // R.drawable.close_aircylinder, R.drawable.open_window,
    // R.drawable.close_window,
    // R.drawable.close_lock, R.drawable.open_lock, R.drawable.open_seatheat,
    // R.drawable.close_seatheat
    // };

    
    private final static int[] imgIds = {
            R.drawable.horm, R.drawable.air_condition, R.drawable.air_condition_close,
            R.drawable.trunck, R.drawable.open_lock, R.drawable.close_lock
    };

    private final static int[] imgIdAfters = {
            R.drawable.horm, R.drawable.trunck, R.drawable.open_lock, R.drawable.close_lock
    };

    // private final static String[] names = { "远程闪灯鸣笛", "远程开启后备箱", "远程启动",
    // "远程熄火", "远程开启空调", "远程关闭空调", "远程开窗", "远程关窗", "远程落锁", "远程解锁" };
    // private final static String[] names = {
    // "闪灯鸣笛", "开启后备箱", "开启空调", "关闭空调", "开启空气净化", "关闭空气净化", "开窗", "关窗", "解锁",
    // "落锁", "开启座椅加热",
    // "关闭座椅加热"
    // };

    private final static String[] names = {
            "闪灯鸣笛", "开启空调", "关闭空调", "开启后备箱", "解锁", "落锁"
    };

    private final static String[] nameAfters = {
            "闪灯鸣笛", "开启后备箱", "解锁", "落锁"
    };

    public RemoteFunctionsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        if (deviceType.equals(SesameLoginInfo.DEVICETYPE_BEFORE)
                || deviceType.equals(SesameLoginInfo.DEVICETYPE_AFTER2016)) {
            return names.length;
        } else {
            return nameAfters.length;
        }
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
            convertView = mInflater.inflate(R.layout.grid_remote_item, null);
            convertView.setTag(mHolder);
            mHolder.mImageView = (ImageView)convertView.findViewById(R.id.remote_item_img_icon);
            mHolder.mTextView = (TextView)convertView.findViewById(R.id.remote_item_txt_name);

        } else {
            mHolder = (Holder)convertView.getTag();
        }

        if (deviceType.equals(SesameLoginInfo.DEVICETYPE_BEFORE)
                || deviceType.equals(SesameLoginInfo.DEVICETYPE_AFTER2016)) {
            mHolder.mImageView.setImageResource(imgIds[position]);
            mHolder.mTextView.setText(names[position]);
        } else {
            mHolder.mImageView.setImageResource(imgIdAfters[position]);
            mHolder.mTextView.setText(nameAfters[position]);
        }

        return convertView;
    }

    class Holder {
        private ImageView mImageView;// 功能图标

        private TextView mTextView;// 功能名称

    }

}
